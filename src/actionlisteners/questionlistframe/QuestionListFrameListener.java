package actionlisteners.questionlistframe;

import actionlisteners.questionlistframe.questionlist.QuestionPanelListener;
import database.Database;
import gui.entryframe.EntryFrame;
import gui.newquestionframe.NewQuestionFrame;
import gui.questionListFrame.QuestionList;
import gui.questionListFrame.QuestionListFrame;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class QuestionListFrameListener implements ActionListener
{
    private QuestionList questionList;
    private NewQuestionFrame newQuestionFrame;
    private static final Database database = Database.database;
    private ChapterPanel chapterPanel;

    public QuestionListFrameListener(QuestionList list, ChapterPanel cPanel)
    {
        chapterPanel = cPanel;
        questionList = list;
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
            newQuestionFrame = new NewQuestionFrame(null ,chapterPanel);
    }

    private void activeButtonPressed()
    {
        if(QuestionListFrame.openedQuestionListFrame != null)
        {
            Path chapterPath = chapterPanel.getPath();
            database.activateAllQuestionsOfChapter(chapterPath);
            QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
            chapterPanel.update(database.getChapterRepresentation(chapterPath));
            EntryFrame.entryFrame.update();
        }
    }

    private void deactiveButtonPressed()
    {
        Path chapterPath = chapterPanel.getPath();
        database.deactivateAllQuestionsOfChapter(chapterPath);
        QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
        chapterPanel.update(database.getChapterRepresentation(chapterPath));
        EntryFrame.entryFrame.update();
    }

    private void resetAnswersButtonPressed()
    {
        Path chapterPath = chapterPanel.getPath();
        Database.database.resetChapterAnswers(chapterPath);
        QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(chapterPath));
        chapterPanel.update(database.getChapterRepresentation(chapterPath));
        EntryFrame.entryFrame.update();
    }
}
