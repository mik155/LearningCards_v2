package gui.settings;


import gui.settingsframe.ChapterPanel;
import gui.settingsframe.SettingsFrame;

import java.nio.file.Paths;

public class SettingsFrameTest
{
    public static void main(String [] args)
    {
        SettingsFrame settingsFrame = new SettingsFrame(null);
        settingsFrame.setVisible(true);
        settingsFrame.addChapterPanel(new ChapterPanel("chapter_1" ,  Paths.get(""), 0 ,0 ,0));
        settingsFrame.addChapterPanel(new ChapterPanel("chapter_2" ,  Paths.get(""), 0 ,0 ,0));
        settingsFrame.addChapterPanel(new ChapterPanel("chapter_3" ,  Paths.get(""), 0 ,0 ,0));
        settingsFrame.addChapterPanel(new ChapterPanel("chapter_4" ,  Paths.get(""), 0 ,0 ,0));

    }
}
