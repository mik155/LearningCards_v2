package actionlisteners.settingsframe;

import actionlisteners.settingsframe.chapterlist.ChapterPanelListener;
import database.Database;
import gui.entryframe.EntryFrame;
import gui.newchapterframe.NewChapterFrame;
import gui.settingsframe.ChapterList;
import gui.settingsframe.SettingsFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SettingsFrameListener implements ActionListener
{
    private ChapterList chapterList;
    private NewChapterFrame newChapterFrame;


    public SettingsFrameListener(ChapterList list)
    {
        chapterList = list;
        newChapterFrame = null;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            JButton pressedButton = (JButton) object;
            if(pressedButton.getText().equals("+"))
                newChapterButtonPressed();
            else if(pressedButton.getText().equals("☒"))
                activeButtonPressed();
            else if(pressedButton.getText().equals("☐"))
                deactiveButtonPressed();
            else
                resetAnswersButtonPressed();
        }
    }

    private void newChapterButtonPressed()
    {
        if(newChapterFrame == null || !newChapterFrame.isActive())
            newChapterFrame = new NewChapterFrame();
    }

    private void activeButtonPressed()
    {
        Database.database.activateAllQuestions();
        SettingsFrame.settingsFrame.setChapterList(Database.database.getChapterListRepresentation());
        EntryFrame.entryFrame.update();
    }

    private void deactiveButtonPressed()
    {
        Database.database.deactivateAllQuestions();
        SettingsFrame.settingsFrame.setChapterList(Database.database.getChapterListRepresentation());
        EntryFrame.entryFrame.update();
    }

    private void resetAnswersButtonPressed()
    {
        Database.database.resetAnswers();
        SettingsFrame.settingsFrame.setChapterList(Database.database.getChapterListRepresentation());
        EntryFrame.entryFrame.update();
    }
}
