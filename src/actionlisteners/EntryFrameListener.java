package actionlisteners;

import database.Database;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import gui.entryframe.EntryFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class EntryFrameListener implements ActionListener
{
    private Database database;
    private EntryFrame entryFrame;

    public EntryFrameListener(EntryFrame entFrame)
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
        }
    }

    private void correctAnswerButtonPressed()
    {
        Path chapterPath = database.getLastReturnedChapterPath();
        Path questionPath = database.getLastReturnedQuestionPath();
        if(chapterPath != null && questionPath != null)
            database.setQuestionState(chapterPath, questionPath, QuestionState.CORRECT);
        Question newQuestion = database.nextQuestion();
        if(newQuestion != null)
            entryFrame.setText(newQuestion.getQuestionText());
        else
            entryFrame.setText("No question's left.");
        entryFrame.setCounter(database.getCorrectAnsweredQuestions(),
                database.getInCorrectAnsweredQuestions(),
                database.getActiveQuestionsNumber());
    }
}
