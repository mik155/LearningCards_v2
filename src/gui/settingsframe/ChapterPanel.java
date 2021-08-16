package gui.settingsframe;
import javax.swing.*;
import actionlisteners.settingsframe.chapterlist.ChapterPanelListener;
import database.chapter.ChapterRepresentation;
import gui.Counter;

import java.awt.*;
import java.nio.file.Path;

public class ChapterPanel extends JLabel
{
    final private JLabel nameLabel;
    final private Counter counter;
    final private JButton resetCounterButton;
    final private JCheckBox checkBox;
    final private Path path;

    public ChapterPanel(final String name, final Path chapterPath, int c, int ic, int an)
    {
        path = chapterPath;
        setBackground(UIManager.getColor("Panel.background"));
        setOpaque(true);
        setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
        setPreferredSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMaximumSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setMinimumSize(new Dimension(SettingsFrame.CHAPTER_PANEL_WIDTH, SettingsFrame.CHAPTER_PANEL_HEIGTH));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 15));
        nameLabel.setPreferredSize(new Dimension(200, 50));
        counter = new Counter(c, ic, an);
        resetCounterButton = new JButton("RESET");
        stylyzeResetCounterButton(resetCounterButton);
        checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        checkBox.setSelected(true);
        setActionListeners();

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

    /**
     * Changes chapter panel color
     * */
    public void setClickedColor()
    {
        Color color = new Color(188, 201, 191);
        setBackground(color);
        counter.setBackground(color);
        nameLabel.setBackground(color);
        resetCounterButton.setBackground(color);
        checkBox.setBackground(color);
        repaint();
    }

    /**
     * Changes chapter panel color
     * */
    public void setDefaultColor()
    {
        Color color = UIManager.getColor ( "Panel.background");
        setBackground(color);
        nameLabel.setBackground(color);
        counter.setBackground(color);
        resetCounterButton.setBackground(color);
        checkBox.setBackground(color);
        repaint();
    }

    /**
     * Returns path of related chapter.
     * @return path of related chapter
     * */
    public Path getPath()
    {
        return path;
    }

    /**
     * Sets counter of ChapterPanel
     * @param co number of correct answered questions
     * @param in number of incorrect answered questions
     * @param ac number of active questions
     **/
    public void setCounter(int co, int in, int ac)
    {
        counter.setCounter(co, in, ac);
    }

    /**
     * Sets checkbox as marked or not marked
     * @param f == true - marked, f == false - unmarked*/
    public void setActive(boolean f)
    {
        checkBox.setSelected(f);
    }

    /**
     * Sets button graphics.
     * */
    public static void stylyzeResetCounterButton(JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 15));
    }

    public void update(ChapterRepresentation rep)
    {
        setActive(rep.getActive() > 0);
        counter.setCounter(rep.getCorrect(), rep.getInCorrect(), rep.getActive());

    }

    private void setActionListeners()
    {
        addMouseListener(new ChapterPanelListener(this));
        resetCounterButton.addActionListener(new ChapterPanelListener(this));
        checkBox.addActionListener(new ChapterPanelListener(this));
    }


}
