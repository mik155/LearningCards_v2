package gui.questionListFrame;

import database.chapter.question.QuestionState;
import gui.Counter;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class QuestionPanel extends JPanel
{
    private JLabel nameLabel;
    private QuestionStateLabel questionStateLabel;
    private JButton resetCounterButton;
    private JCheckBox checkBox;
    private Path path;

    public QuestionPanel(final String name, final  Path path)
    {
        setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
        setPreferredSize(new Dimension(gui.settingsframe.SettingsFrame.CHAPTER_PANEL_WIDTH, gui.settingsframe.SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMaximumSize(new Dimension(gui.settingsframe.SettingsFrame.CHAPTER_PANEL_WIDTH, gui.settingsframe.SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMinimumSize(new Dimension(gui.settingsframe.SettingsFrame.CHAPTER_PANEL_WIDTH, gui.settingsframe.SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 15));
        questionStateLabel = new QuestionStateLabel(QuestionState.NO_ANSWER);
        resetCounterButton = new JButton("RESET");
        SettingsFrame.stylyzeSettingsButton(resetCounterButton);
        checkBox = new JCheckBox();


        add(Box.createRigidArea(new Dimension(30, 0)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(questionStateLabel);
        add(Box.createRigidArea(new Dimension(30,0)));
        add(resetCounterButton);
        add(Box.createRigidArea(new Dimension(30,0)));
        add(checkBox);
        add(Box.createRigidArea(new Dimension(30,0)));
    }
}
