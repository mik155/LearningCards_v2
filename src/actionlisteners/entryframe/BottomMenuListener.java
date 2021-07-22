package actionlisteners.entryframe;

import database.Database;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import gui.entryframe.EntryFrame;
import gui.entryframe.TEXT_AREA_DISPLAY;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class BottomMenuListener implements ActionListener
{
    private Database database;
    private EntryFrame entryFrame;

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
