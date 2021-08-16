package actionlisteners.newquestionframe;

import database.Database;
import database.chapter.question.QuestionState;
import gui.entryframe.EntryFrame;
import gui.newquestionframe.NewQuestionFrame;
import gui.questionListFrame.QuestionListFrame;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.SettingsFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class NewQuestionFrameListener implements ActionListener
{
    private static final Database database = Database.database;
    final private QuestionPanel clickedPanel;

    public NewQuestionFrameListener(QuestionPanel qPanel)
    {
        clickedPanel = qPanel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            JButton clickedButton = (JButton) object;
            if(clickedButton.getText().equals("OK"))
            {
                NewQuestionFrame newQuestionFrame = NewQuestionFrame.openedNewQuestionFrame;
                String questionText = newQuestionFrame.getQuestionText();
                String answerText = newQuestionFrame.getAnswerText();
                if(clickedPanel != null)
                {
                    Path qPath = clickedPanel.getQuestionPath();
                    Path cPath = clickedPanel.getChapterPath();

                    if (database.updateQuestion(cPath, qPath, questionText, answerText))
                        clickedPanel.update(database.getQuestionRepresentation(cPath, qPath));
                }
                else
                {
                    Path qPath;
                    Path cPath = newQuestionFrame.getChapterPanel().getPath();
                    qPath = database.addNewQuestion(cPath, questionText, answerText);
                    QuestionPanel qPanel = new QuestionPanel(qPath, cPath,questionText,
                            QuestionState.NO_ANSWER,true);
                    QuestionListFrame.openedQuestionListFrame.addQuestionPanel(qPanel);
                    if(SettingsFrame.settingsFrame != null && SettingsFrame.settingsFrame .isVisible())
                        SettingsFrame.settingsFrame.setChapterList(database.getChapterListRepresentation());
                    EntryFrame.entryFrame.update();
                }

                NewQuestionFrame.openedNewQuestionFrame.dispose();
                NewQuestionFrame.openedNewQuestionFrame = null;
            }
            else if(clickedButton.getText().equals("CANCEL"))
            {
                NewQuestionFrame.openedNewQuestionFrame.dispose();
                NewQuestionFrame.openedNewQuestionFrame = null;
            }
        }
    }
}
