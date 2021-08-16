package gui.questionListFrame;

import Utils.Utils;
import actionlisteners.questionlistframe.QuestionListFrameListener;
import actionlisteners.questionlistframe.KeyBinding;
import database.chapter.question.QuestionRepresentation;
import gui.entryframe.EntryFrame;
import gui.settingsframe.ChapterPanel;
import gui.settingsframe.SettingsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.LinkedList;

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

    public static QuestionListFrame openedQuestionListFrame;

    private QuestionList questionList;
    private JScrollPane jScrollPane;
    private JPanel bottomPanel;
    private JButton addNewQuestionButton;
    private JButton setAllActiveButton;
    private JButton setAllNotActiveButton;
    private JButton resetAnswerButton;

    final private ChapterPanel chapterPanel;

    public QuestionListFrame(ChapterPanel cPanel, LinkedList<QuestionRepresentation> chapterListRep)
    {
        openedQuestionListFrame = this;
        chapterPanel = cPanel;
        setjScrollPane(chapterListRep);
        add(jScrollPane, BorderLayout.CENTER);
        setBottomPanel();
        add(bottomPanel, BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setResizable(false);
        setLocation(Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setActionListeners();
        setVisible(true);
    }

    public void setQuestionList(LinkedList<QuestionRepresentation> representation)
    {
        questionList.setQuestionList(representation);
    }


    public void addQuestionPanel(QuestionPanel questionPanel)
    {
        questionList.addQuestionPanel(questionPanel);
    }

    public Path getChapterPath()
    {
        return chapterPanel.getPath();
    }

    private void setjScrollPane(LinkedList<QuestionRepresentation> chapterListRep)
    {
        questionList = new QuestionList(chapterListRep);
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
        bottomPanel.add(addNewQuestionButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(setAllActiveButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(setAllNotActiveButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(resetAnswerButton);
        bottomPanel.add(Box.createHorizontalGlue());
    }

    private void setButtons()
    {
        addNewQuestionButton = new JButton("+");
        setAllActiveButton = new JButton("☒");
        setAllNotActiveButton = new JButton("☐");
        resetAnswerButton = new JButton("<html><font color='blue'>" + "☐" + "</font></html>");


        SettingsFrame.stylyzeSettingsBottomMenuButton(addNewQuestionButton);
        SettingsFrame.stylyzeSettingsBottomMenuButton(setAllActiveButton);
        SettingsFrame.stylyzeSettingsBottomMenuButton(setAllNotActiveButton);
        SettingsFrame.stylyzeSettingsBottomMenuButton(resetAnswerButton);

        addNewQuestionButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        addNewQuestionButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        setAllActiveButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        setAllActiveButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        setAllNotActiveButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        setAllNotActiveButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));

        resetAnswerButton.setPreferredSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
        resetAnswerButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, EntryFrame.BUTTON_HEIGTH));
    }

    private void setActionListeners()
    {
        addNewQuestionButton.addActionListener(new QuestionListFrameListener(chapterPanel));
        setAllActiveButton.addActionListener(new QuestionListFrameListener(chapterPanel));
        setAllNotActiveButton.addActionListener(new QuestionListFrameListener(chapterPanel));
        resetAnswerButton.addActionListener(new QuestionListFrameListener(chapterPanel));

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "PRESSED DELETE");
        getRootPane().getActionMap().put("PRESSED DELETE", new KeyBinding(chapterPanel, questionList));
    }

}
