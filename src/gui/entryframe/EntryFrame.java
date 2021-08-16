package gui.entryframe;

import actionlisteners.entryframe.BottomMenuListener;
import actionlisteners.entryframe.SettingsButtonListener;
import database.Database;
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

    public static EntryFrame entryFrame;

    private JScrollPane scrollPane;
    private JTextArea jTextArea;
    private TEXT_AREA_DISPLAY text_area_display;

    private JPanel upperPanel;
    private Counter counter;
    private JPanel bottomMenu;
    private JButton correctAnswerButton;
    private JButton incorrectAnswerButton;
    private JButton answerButton;
    private JButton prevQuestionButton;
    private JButton nextQuestionButton;
    private JButton settingsButton;

    public EntryFrame(int cor, int inc, int act)
    {
        entryFrame = this;
        setUpperPanel(cor,inc,act);
        setTextArea();
        setScrollPane();
        setBottomMenu();
        text_area_display = null;

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

    public EntryFrame(int cor, int inc, int act, String text, TEXT_AREA_DISPLAY displayMode)
    {
        entryFrame = this;
        setUpperPanel(cor,inc,act);
        setTextArea();
        setScrollPane();
        setBottomMenu();
        jTextArea.setText(text);
        text_area_display = displayMode;

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

    /**
     * Sets state of counter.
     * @param correct Number of correct question's.
     * @param incorrect Number of incorrect question's
     * @param  active Number of active question's.
     */
    public void setCounter(int correct, int incorrect, int active)
    {
        counter.setCounter(correct, incorrect, active);
    }

    /**
     * Sets text_area_display_mode
     * @param displayMode TEXT_AREA_DISPLAY_MODE OBJECT that will be set.
     */
    public void setTextAreaDisplayMode(TEXT_AREA_DISPLAY displayMode)
    {
        text_area_display = displayMode;
    }

    /**
     * Returns text_area_diplay_mode.
     */
    public TEXT_AREA_DISPLAY getTextAreaDisplayMode()
    {
        return text_area_display;
    }

    public void update()
    {
        int co = Database.database.getCorrectAnsweredQuestions();
        int in = Database.database.getInCorrectAnsweredQuestions();
        int ac = Database.database.getActiveQuestionsNumber();
        setCounter(co, in, ac);
        if(Database.database.getActiveQuestionsNumber() > 0)
        {
            if(Database.database.getLastRetunedQuestion() == null)
                setText(Database.database.nextQuestion().getQuestionText());
            else if(!Database.database.getLastRetunedQuestion().isActive())
                setText(Database.database.nextQuestion().getQuestionText());
            else
                setText(Database.database.getLastRetunedQuestion().getQuestionText());
            entryFrame.setTextAreaDisplayMode(TEXT_AREA_DISPLAY.QUESTION);
        }
        else
            setText("No question's left.");
    }

    private void setActionListeners()
    {
        correctAnswerButton.addActionListener(new BottomMenuListener(this));
        incorrectAnswerButton.addActionListener(new BottomMenuListener(this));
        prevQuestionButton.addActionListener(new BottomMenuListener(this));
        nextQuestionButton.addActionListener(new BottomMenuListener(this));
        answerButton.addActionListener(new BottomMenuListener(this));
        settingsButton.addActionListener(new SettingsButtonListener());
    }

    private void setUpperPanel(int correct, int incorrect, int active)
    {
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.LINE_AXIS));

        counter = new Counter(correct,incorrect,active);
        counter.setMaximumSize(new Dimension(100,60));

        settingsButton = new JButton("⚙");
        settingsButton.setPreferredSize(new Dimension(55, 55));
        settingsButton.setMaximumSize(new Dimension(55, 55));
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setFont(new Font(settingsButton.getFont().getName(), Font.PLAIN, 20));

        upperPanel.add(Box.createRigidArea(new Dimension(55, 55)));
        upperPanel.add(Box.createHorizontalGlue());
        upperPanel.add(Box.createRigidArea(new Dimension(settingsButton.getWidth(), settingsButton.getHeight())));
        upperPanel.add(counter);
        upperPanel.add(Box.createHorizontalGlue());
        upperPanel.add(settingsButton);
    }

    private void setTextArea()
    {
        jTextArea = new JTextArea();
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

        answerButton = new JButton("?");
        answerButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        answerButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(answerButton);

        prevQuestionButton = new JButton("←");
        prevQuestionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        prevQuestionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(prevQuestionButton);

        nextQuestionButton = new JButton("→");
        nextQuestionButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        nextQuestionButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        stylyzeButton(nextQuestionButton);

        bottomMenu = new JPanel();
        bottomMenu.setLayout(new BoxLayout(bottomMenu, BoxLayout.LINE_AXIS));
        bottomMenu.setPreferredSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));


        bottomMenu.add(Box.createRigidArea(new Dimension(20,0)));
        bottomMenu.add(correctAnswerButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(incorrectAnswerButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(answerButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(prevQuestionButton);
        bottomMenu.add(Box.createHorizontalGlue());
        bottomMenu.add(nextQuestionButton);
        bottomMenu.add(Box.createHorizontalGlue());

    }

    public  static void stylyzeButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 25));
    }
}
