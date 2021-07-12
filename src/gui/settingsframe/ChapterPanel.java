package gui.settingsframe;

import javax.swing.*;

import gui.Counter;
import gui.entryframe.EntryFrame;

import java.awt.*;
import java.nio.file.Path;

public class ChapterPanel extends JPanel
{
    private JLabel nameLabel;
    private Counter counter;
    private JButton resetCounterButton;
    private JCheckBox checkBox;
    private Path path;

    public ChapterPanel(final String name, final Path chapterPath, int c, int ic, int an)
    {
        path = chapterPath;
        setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
        setPreferredSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMaximumSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMinimumSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 15));
        counter = new Counter(c, ic, an);
        resetCounterButton = new JButton("RESET");
        SettingsFrame.stylyzeSettingsButton(resetCounterButton);
        checkBox = new JCheckBox();


        add(Box.createRigidArea(new Dimension(30, 0)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(counter);
        add(Box.createRigidArea(new Dimension(30,0)));
        add(resetCounterButton);
        add(Box.createRigidArea(new Dimension(30,0)));
        add(checkBox);
        add(Box.createRigidArea(new Dimension(30,0)));
    }
}
