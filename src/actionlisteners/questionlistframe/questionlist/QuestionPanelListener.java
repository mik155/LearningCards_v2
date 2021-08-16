package actionlisteners.questionlistframe.questionlist;

import actionlisteners.settingsframe.chapterlist.ChapterPanelListener;
import database.chapter.question.QuestionRepresentation;
import database.chapter.question.QuestionState;
import gui.entryframe.EntryFrame;
import gui.newquestionframe.NewQuestionFrame;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Path;

import static database.Database.database;


public class QuestionPanelListener implements MouseListener, ActionListener
{
    private static QuestionPanel lastClickedPanel = null;
    final private QuestionPanel clickedPanel;
    private static long time;

    public QuestionPanelListener(QuestionPanel qPanel)
    {
        clickedPanel = qPanel;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        long actualTime = System.currentTimeMillis();
        if(lastClickedPanel == null)
        {
            lastClickedPanel = clickedPanel;
            clickedPanel.setClickedColor();
        }
        else if(!lastClickedPanel.equals(clickedPanel))
        {
            lastClickedPanel.setDefaultColor();
            lastClickedPanel = clickedPanel;
            clickedPanel.setClickedColor();
        }
        else if(lastClickedPanel.equals(clickedPanel))
        {
            long timeDiff = actualTime - time;
            if(timeDiff <= 500)
            {
                if(NewQuestionFrame.openedNewQuestionFrame != null)
                    NewQuestionFrame.openedNewQuestionFrame.dispose();
                QuestionRepresentation qRep = database.getQuestionRepresentation(clickedPanel.getChapterPath(), clickedPanel.getQuestionPath());
                String question = qRep.getQuestionText();
                String answer = qRep.getAnswerText();
                NewQuestionFrame.openedNewQuestionFrame = new NewQuestionFrame(clickedPanel, ChapterPanelListener.getLastClickedPanel(), question, answer);
             }
            else
            {
                lastClickedPanel.setClickedColor();
            }
        }
        time = actualTime;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            Path questionPath = clickedPanel.getQuestionPath();
            Path chapterPath = clickedPanel.getChapterPath();

            JButton clickedButton = (JButton) object;
            if(clickedButton.getText().equals("RESET"))
            {
                database.setQuestionState(chapterPath, questionPath, QuestionState.NO_ANSWER);
                clickedPanel.update(database.getQuestionRepresentation(chapterPath, questionPath));
                if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame.isVisible())
                    SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
                EntryFrame.entryFrame.update();
            }
        }
        else if(object instanceof JCheckBox)
        {
            Path questionPath = clickedPanel.getQuestionPath();
            Path chapterPath = clickedPanel.getChapterPath();

            JCheckBox checkBox = (JCheckBox) object;
            database.active(chapterPath, questionPath, checkBox.isSelected());

            clickedPanel.update(database.getQuestionRepresentation(chapterPath, questionPath));
            if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame.isVisible())
                SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
            EntryFrame.entryFrame.update();
        }
    }

    public static QuestionPanel getLastClickedPanel()
    {
        return lastClickedPanel;
    }

    public static void resetLastClickedPanel()
    {
        lastClickedPanel = null;
    }
}
