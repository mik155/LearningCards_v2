package actionlisteners.settingsframe.chapterlist;

import gui.entryframe.EntryFrame;
import gui.questionListFrame.QuestionListFrame;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Path;
import static database.Database.database;

public class ChapterPanelListener implements MouseListener, ActionListener
{

    final private ChapterPanel chapterPanel;
    private static ChapterPanel lastClickedChapterPanel;
    private static long time;

    public ChapterPanelListener(ChapterPanel panel)
    {
        chapterPanel = panel;
    }

    /**
     * Returns last clicked chapter panel.
     * @return last clicked panel
     * */
    public static ChapterPanel getLastClickedChapterPanel()
    {
        return lastClickedChapterPanel;
    }

    /**
     * Resets last clicked chapter panel.
     * */
    public static void resetLastClickedLabel()
    {
        lastClickedChapterPanel = null;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        long actualTime = System.currentTimeMillis();
        if(lastClickedChapterPanel == null)
        {
            lastClickedChapterPanel = chapterPanel;
            chapterPanel.setClickedColor();
        }
        else if(!lastClickedChapterPanel.equals(chapterPanel))
        {
            lastClickedChapterPanel.setDefaultColor();
            lastClickedChapterPanel = chapterPanel;
            chapterPanel.setClickedColor();
        }
        else if(lastClickedChapterPanel.equals(chapterPanel))
        {
            long timeDiff = actualTime - time;
            if(timeDiff <= 500)
            {
                Path cPAth = chapterPanel.getPath();
                if(QuestionListFrame.openedQuestionListFrame != null)
                    QuestionListFrame.openedQuestionListFrame.dispose();
                QuestionListFrame.openedQuestionListFrame =
                        new QuestionListFrame(chapterPanel, database.getQuestionListRepresentation(cPAth));
            }
            else
            {
                lastClickedChapterPanel.setClickedColor();
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
            JButton clickedButton = (JButton) object;
            if(clickedButton.getText().equals("RESET"))
            {
                Path chapterPath = chapterPanel.getPath();
                database.resetChapterAnswers(chapterPath);
                chapterPanel.update(database.getChapterRepresentation(chapterPath));
                if(QuestionListFrame.openedQuestionListFrame != null)
                    QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
                EntryFrame.entryFrame.update();
            }
        }
        else if(object instanceof JCheckBox)
        {
            JCheckBox checkBox = (JCheckBox) object;
            Path chapterPath = chapterPanel.getPath();

            if(!checkBox.isSelected())
                database.deactivateAllQuestionsOfChapter(chapterPath);

            else
                database.activateAllQuestionsOfChapter(chapterPath);

            chapterPanel.update(database.getChapterRepresentation(chapterPath));
            if(QuestionListFrame.openedQuestionListFrame != null)
                QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
            EntryFrame.entryFrame.update();
        }
    }

    public static ChapterPanel getLastClickedPanel()
    {
        return lastClickedChapterPanel;
    }

}
