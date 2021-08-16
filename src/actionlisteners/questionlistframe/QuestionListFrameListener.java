package actionlisteners.questionlistframe;

import database.Database;
import gui.entryframe.EntryFrame;
import gui.newquestionframe.NewQuestionFrame;
import gui.questionListFrame.QuestionListFrame;
import gui.settingsframe.ChapterPanel;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class QuestionListFrameListener implements ActionListener
{
    private static final Database database = Database.database;
    private NewQuestionFrame newQuestionFrame;
    final private ChapterPanel chapterPanel;

    public QuestionListFrameListener(ChapterPanel cPanel)
    {
        chapterPanel = cPanel;
        newQuestionFrame = null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            JButton pressedButton = (JButton) object;
            if(pressedButton.getText().equals("+"))
                newQuestionButtonPressed();
            else if(pressedButton.getText().equals("☒"))
                activeButtonPressed();
            else if(pressedButton.getText().equals("☐"))
                deactiveButtonPressed();
            else
                resetAnswersButtonPressed();
        }
    }

    private void newQuestionButtonPressed()
    {
        if(newQuestionFrame == null || !newQuestionFrame.isActive())
            newQuestionFrame = new NewQuestionFrame(null ,chapterPanel, null, null);
    }

    private void activeButtonPressed()
    {
        if(QuestionListFrame.openedQuestionListFrame != null)
        {
            Path chapterPath = chapterPanel.getPath();
            database.activateAllQuestionsOfChapter(chapterPath);
            QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
            if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
                SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
            EntryFrame.entryFrame.update();
        }
    }

    private void deactiveButtonPressed()
    {
        Path chapterPath = chapterPanel.getPath();
        database.deactivateAllQuestionsOfChapter(chapterPath);
        QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
        if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
            SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
        EntryFrame.entryFrame.update();
    }

    private void resetAnswersButtonPressed()
    {
        Path chapterPath = chapterPanel.getPath();
        Database.database.resetChapterAnswers(chapterPath);
        QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
        if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
            SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
        EntryFrame.entryFrame.update();
    }
}
