package actionlisteners.newchapterframe;

import database.Database;
import gui.entryframe.EntryFrame;
import gui.newchapterframe.NewChapterFrame;
import gui.settingsframe.ChapterPanel;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class NewChapterFrameListener implements ActionListener
{
    private final NewChapterFrame newChapterFrame;

    public NewChapterFrameListener(NewChapterFrame nChapterFrame)
    {
        newChapterFrame = nChapterFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object source = actionEvent.getSource();
        if(source instanceof JButton)
        {
            JButton pressedButton = (JButton) source;
            if(pressedButton.getText().equals("OK"))
            {
                String chapterName = newChapterFrame.getText();
                if(chapterName.length() >= 20)
                    chapterName = chapterName.substring(0, 17) + "...";
                Path chapterPath = Database.database.addNewChapter(chapterName);
                if(chapterPath == null)
                    newChapterFrame.dispose();
                else
                {
                    ChapterPanel chapterPanel = new ChapterPanel(chapterName, chapterPath, 0, 0, 0);
                    EntryFrame.entryFrame.update();
                    SettingsFrame.settingsFrame.addChapterPanel(chapterPanel);
                    newChapterFrame.dispose();
                }
            }
            else if(pressedButton.getText().equals("CANCEL"))
                newChapterFrame.dispose();

        }
    }
}
