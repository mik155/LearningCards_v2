package actionlisteners.settingsframe.chapterlist;

import database.Database;
import gui.entryframe.EntryFrame;
import gui.questionListFrame.QuestionListFrame;
import gui.settingsframe.ChapterList;
import gui.settingsframe.ChapterPanel;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

public class KeyBinding extends AbstractAction
{
    private ChapterList chapterList;

    public KeyBinding(ChapterList list)
    {
        chapterList = list;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        ChapterPanel markedPanel = ChapterPanelListener.getLastClickedChapterPanel();
        if(markedPanel != null)
        {
            Path chapterPath = markedPanel.getPath();
            Database.database.removeChapter(chapterPath);
            chapterList.removeChapterPanel(markedPanel);
            ChapterPanelListener.resetLastClickedLabel();
            if(QuestionListFrame.openedQuestionListFrame.getChapterPath().equals(chapterPath))
            {
                QuestionListFrame.openedQuestionListFrame.dispose();
                QuestionListFrame.openedQuestionListFrame = null;
            }
            EntryFrame.entryFrame.update();
        }
    }
}

