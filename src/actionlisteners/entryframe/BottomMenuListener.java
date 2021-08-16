package actionlisteners.entryframe;

import actionlisteners.settingsframe.chapterlist.ChapterPanelListener;
import database.Database;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import gui.entryframe.EntryFrame;
import gui.entryframe.TEXT_AREA_DISPLAY;
import gui.questionListFrame.QuestionListFrame;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class BottomMenuListener implements ActionListener
{
    final private Database database;
    final private EntryFrame entryFrame;

    public BottomMenuListener(EntryFrame entFrame)
    {
        database = Database.database;
        entryFrame = entFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            JButton pressedButton = (JButton) object;
            if(pressedButton.getText().equals("✓"))
                correctAnswerButtonPressed();
            if(pressedButton.getText().equals("x"))
                inCorrectAnswerButtonPressed();
            if(pressedButton.getText().equals("←"))
                prevQuestionButtonPressed();
            if(pressedButton.getText().equals("→"))
                nextQuestionButtonPressed();
            if(pressedButton.getText().equals("?"))
                answerButtonPressed();
            }
    }

    private void correctAnswerButtonPressed()
    {
        if(database.getActiveQuestionsNumber() > database.getAnsweredQuestions())
        {
            Path chapterPath = database.getLastReturnedChapterPath();
            Path questionPath = database.getLastReturnedQuestionPath();
            if (chapterPath != null && questionPath != null)
                database.setQuestionState(chapterPath, questionPath, QuestionState.CORRECT);
            Question newQuestion = database.nextQuestion();
            if (newQuestion != null)
                entryFrame.setText(newQuestion.getQuestionText());
            else
                entryFrame.setText("No question's left.");
            entryFrame.setCounter(database.getCorrectAnsweredQuestions(),
                    database.getInCorrectAnsweredQuestions(),
                    database.getActiveQuestionsNumber());

            if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
                SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());

            if(QuestionListFrame.openedQuestionListFrame != null && QuestionListFrame.openedQuestionListFrame.isVisible())
            {
                Path cPath = ChapterPanelListener.getLastClickedChapterPanel().getPath();
                QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(cPath));
            }
        }
    }

    private void inCorrectAnswerButtonPressed()
    {
        if(database.getActiveQuestionsNumber() > database.getAnsweredQuestions())
        {
            Path chapterPath = database.getLastReturnedChapterPath();
            Path questionPath = database.getLastReturnedQuestionPath();
            if (chapterPath != null && questionPath != null)
                database.setQuestionState(chapterPath, questionPath, QuestionState.IN_CORRECT);
            Question newQuestion = database.nextQuestion();
            if (newQuestion != null)
                entryFrame.setText(newQuestion.getQuestionText());
            else
                entryFrame.setText("No question's left.");
            entryFrame.setCounter(database.getCorrectAnsweredQuestions(),
                    database.getInCorrectAnsweredQuestions(),
                    database.getActiveQuestionsNumber());

            if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
                SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());

            if(QuestionListFrame.openedQuestionListFrame != null && QuestionListFrame.openedQuestionListFrame.isVisible())
            {
                Path cPath = ChapterPanelListener.getLastClickedChapterPanel().getPath();
                QuestionListFrame.openedQuestionListFrame.setQuestionList(database.getQuestionListRepresentation(cPath));
            }
        }
    }

    private void nextQuestionButtonPressed()
    {
        Question newQuestion = database.nextQuestion();
        if(newQuestion != null)
            entryFrame.setText(newQuestion.getQuestionText());
        else
            entryFrame.setText("No question's left.");
    }

    private void prevQuestionButtonPressed()
    {
        Question newQuestion = database.previousQuestion();
        if(newQuestion != null)
            entryFrame.setText(newQuestion.getQuestionText());
        else
            entryFrame.setText("No question's left.");
    }

    private void answerButtonPressed()
    {
        Question question = database.getLastRetunedQuestion();
        if(question != null && question.isActive())
        {
            if(entryFrame.getTextAreaDisplayMode().answer())
            {
                entryFrame.setText(question.getQuestionText());
                entryFrame.setTextAreaDisplayMode(TEXT_AREA_DISPLAY.QUESTION);
            }
            else if(entryFrame.getTextAreaDisplayMode().question())
            {
                entryFrame.setText(question.getAnswerText());
                entryFrame.setTextAreaDisplayMode(TEXT_AREA_DISPLAY.ANSWER);
            }
        }
    }
}
