package gui.settingsframe;

import javax.swing.*;
import java.awt.*;

public class QuestionList extends JPanel
{
    public QuestionList()
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.LIGHT_GRAY);
    }

    public void addChapterPanel(ChapterPanel chapterPanel)
    {
        add(chapterPanel);
    }
}
