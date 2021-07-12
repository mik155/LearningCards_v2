package gui.entryframe;

import actionlisteners.EntryFrameListener;
import gui.Counter;
import Utils.Utils;

import javax.swing.*;
import java.awt.*;

public class EntryFrame extends JFrame
{
    public static final int FRAME_WIDTH = 550;
    public static final int FRAME_HEIGTH = 700;

    public static final int TEXT_AREA_WIDTH = 600;
    public static final int TEXT_AREA_HEIGTH = 600;

    public static final int BOTTOM_MENU_WIDTH = 600;
    public static final int BOTTOM_MENU_HEIGTH = FRAME_HEIGTH - TEXT_AREA_HEIGTH;

    public static final int BUTTON_WIDTH = 95;
    public static final int BUTTON_HEIGTH = 55;



    private JScrollPane scrollPane;
    private JTextArea jTextArea;

    private JPanel upperPanel;
    private Counter counter;
    private JPanel bottomMenu;
    private JButton correctAnswerButton;
    private JButton incorrectAnswerButton;
    private JButton prevQuestionButton;
    private JButton nextQuestionButton;
    private JButton settingsButton;

    public EntryFrame(int cor, int inc, int act)
    {
        setUpperPanel(cor,inc,act);
        setTextArea();
        setScrollPane();
        setBottomMenu();

        add(upperPanel, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomMenu, BorderLayout.PAGE_END);

        setActionListeners();
        setLocation(Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH,FRAME_HEIGTH));
        setVisible(true);
        setResizable(false);
    }

    /**
     * Sets text in JTextArea.
     * @param text String that wil be print in JTextArea.
     */
    public void setText(String text)
    {
        jTextArea.setText(text);
        jTextArea.repaint();
    }

    public void setCounter(int correct, int incorrect, int active)
    {
        counter.setCounter(correct, incorrect, active);
    }

    private void setActionListeners()
    {
        correctAnswerButton.addActionListener(new EntryFrameListener(this));
    }

    private void setUpperPanel(int correct, int incorrect, int active)
    {
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));
        counter = new Counter(correct,incorrect,active);

        upperPanel.add(Box.createRigidArea(new Dimension((int)(FRAME_WIDTH * 0.7), 0)));
        upperPanel.add(counter);
        upperPanel.add(Box.createRigidArea(new Dimension((int)(10), 0)));
    }

    private void setTextArea()
    {
        jTextArea = new JTextArea();
        jTextArea.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, TEXT_AREA_HEIGTH));
        jTextArea.setFont(new Font(jTextArea.getFont().getName(), Font.PLAIN, 15));
        jTextArea.setColumns(50);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setLineWrap(true);
    }

    private void setScrollPane()
    {
        scrollPane = new JScrollPane(jTextArea);
        scrollPane.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, TEXT_AREA_HEIGTH));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void setBottomMenu()
    {
        correctAnswerButton = new JButton("✓");
        correctAnswerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        correctAnswerButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(correctAnswerButton);

        incorrectAnswerButton = new JButton("x");
        incorrectAnswerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        incorrectAnswerButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(incorrectAnswerButton);

        prevQuestionButton = new JButton("←");
        prevQuestionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        prevQuestionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(prevQuestionButton);

        nextQuestionButton = new JButton("→");
        nextQuestionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        nextQuestionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(nextQuestionButton);

        settingsButton = new JButton("⚙");
        settingsButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        settingsButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(settingsButton);

        bottomMenu = new JPanel();
        bottomMenu.setLayout(new BoxLayout(bottomMenu, BoxLayout.LINE_AXIS));
        bottomMenu.setPreferredSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));


        bottomMenu.add(Box.createRigidArea(new Dimension(20,0)));
        bottomMenu.add(correctAnswerButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(incorrectAnswerButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(prevQuestionButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(nextQuestionButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(settingsButton);
        bottomMenu.add(Box.createRigidArea(new Dimension(20,0)));
    }

    public  static void stylyzeButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 25));
    }
}
