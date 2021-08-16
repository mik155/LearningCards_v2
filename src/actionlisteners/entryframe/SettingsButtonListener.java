package actionlisteners.entryframe;

import database.Database;
import gui.settingsframe.SettingsFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsButtonListener implements  ActionListener
{
    private SettingsFrame settingsFrame = null;

    public SettingsButtonListener()
    {

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {//TO DO
        if(settingsFrame == null)
        {
            settingsFrame = new SettingsFrame(Database.database.getChapterListRepresentation());
            settingsFrame.setFocusable(true);
        }
        else if(!settingsFrame.isVisible())
        {
            settingsFrame = new SettingsFrame(Database.database.getChapterListRepresentation());
            settingsFrame.setFocusable(true);
        }


    }
}
