package gui.questionListFrame;

import Utils.Utils;
import database.chapter.question.QuestionState;
import gui.Counter;
import gui.entryframe.EntryFrame;
import gui.settingsframe.QuestionList;

import javax.swing.*;
import java.awt.*;

public class QuestionListFrame extends JFrame
{
    public static int FRAME_WIDTH = EntryFrame.FRAME_WIDTH;
    public static int FRAME_HEIGTH = EntryFrame.FRAME_HEIGTH;

    public static int QUESTION_LIST_WIDTH = FRAME_WIDTH;
    public static int QUESTION_LIST_HEIGTH = EntryFrame.TEXT_AREA_HEIGTH;

    public static int QUESTION_PANEL_WIDTH = FRAME_WIDTH;
    public static int QUESTION_PANEL_HEIGTH = 70;

    public static final int BOTTOM_MENU_WIDTH =  EntryFrame.FRAME_WIDTH;
    public static final int BOTTOM_MENU_HEIGTH = FRAME_HEIGTH - QUESTION_LIST_HEIGTH;

    private QuestionList questionList;
    private JScrollPane jScrollPane;
    private JPanel bottomPanel;
    private JButton addButton;
    private JButton removeButton;
    private JButton resetCounterButton;
    private JButton resetActButton;

    public QuestionListFrame()
    {
        setjScrollPane();
        add(jScrollPane, BorderLayout.CENTER);
        setBottomPanel();
        add(bottomPanel, BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setResizable(false);
        setLocation(Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addQuestionPanel(QuestionPanel questionPanel)
    {
        questionList.add(questionPanel);
        jScrollPane.repaint();
    }

    private void setjScrollPane()
    {
        questionList = new QuestionList();
        questionList.setMinimumSize(new Dimension(QUESTION_LIST_WIDTH, QUESTION_LIST_HEIGTH));

        jScrollPane = new JScrollPane(questionList);
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

    private void setBottomPanel()
    {
        setButtons();
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setPreferredSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));

        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(addButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(removeButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(resetCounterButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(resetActButton);
        bottomPanel.add(Box.createHorizontalGlue());
    }

    private void setButtons()
    {
        addButton = new JButton("+");
        removeButton = new JButton("-");
        Counter counter = new Counter(0,0,0);
        resetCounterButton = new JButton("<html><font color='blue' size='5'>" + "☐" + "</font></html>");
        resetActButton = new JButton("RESET");

        stylyzeSettingsButton(addButton);
        stylyzeSettingsButton(removeButton);
        stylyzeSettingsButton(resetCounterButton);
        stylyzeSettingsButton(resetActButton);
        resetCounterButton.setFont(new Font(resetActButton.getFont().getName(), Font.PLAIN, 10));

        addButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        addButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        removeButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        removeButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        resetCounterButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        resetCounterButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        resetActButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        resetActButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

    }

    public static void stylyzeSettingsButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 15));
    }

}