package actionlisteners.questionlistframe;

import actionlisteners.questionlistframe.questionlist.QuestionPanelListener;
import database.Database;
import gui.entryframe.EntryFrame;
import gui.questionListFrame.QuestionList;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class KeyBinding extends AbstractAction
{
    private final QuestionList questionList;
    private final ChapterPanel clickedPanel;
    private final Database database = Database.database;

    public KeyBinding(ChapterPanel cPanel, QuestionList list)
    {
        clickedPanel = cPanel;
        questionList = list;
    }

    public void actionPerformed(ActionEvent actionEvent)
    {
        QuestionPanel markedPanel = QuestionPanelListener.getLastClickedPanel();
        if(markedPanel != null)
        {
            questionList.removeQuestionPanel(markedPanel);
            database.removeQuestion(markedPanel.getChapterPath(), markedPanel.getQuestionPath());
            QuestionPanelListener.resetLastClickedPanel();
            clickedPanel.update(database.getChapterRepresentation(clickedPanel.getPath()));
            EntryFrame.entryFrame.update();
        }
    }

}
