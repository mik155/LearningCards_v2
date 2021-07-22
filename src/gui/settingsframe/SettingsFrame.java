package gui.settingsframe;

import Utils.Utils;
import actionlisteners.settingsframe.SettingsFrameListener;
import actionlisteners.settingsframe.chapterlist.KeyBinding;
import gui.Counter;
import gui.entryframe.EntryFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class SettingsFrame extends JFrame
{
    public static int FRAME_WIDTH = EntryFrame.FRAME_WIDTH;
    public static int FRAME_HEIGTH = EntryFrame.FRAME_HEIGTH;

    public static int CHAPTER_LIST_WIDTH = FRAME_WIDTH;
    public static int CHAPTER_LIST_HEIGTH = EntryFrame.TEXT_AREA_HEIGTH;

    public static int CHAPTER_PANEL_WIDTH = FRAME_WIDTH;
    public static int CHAPTER_PANEL_HEIGTH = 70;

    public static final int BOTTOM_MENU_WIDTH =  EntryFrame.FRAME_WIDTH;
    public static final int BOTTOM_MENU_HEIGTH = FRAME_HEIGTH - CHAPTER_LIST_HEIGTH;

    public static final int BUTTON_WIDTH = EntryFrame.BUTTON_WIDTH;
    public static final int BUTTON_HEIGTH = EntryFrame.BUTTON_HEIGTH;

    public static SettingsFrame settingsFrame;

    private ChapterList chapterList;
    private JScrollPane jScrollPane;
    private JPanel bottomPanel;
    private JButton addButton;
    private JButton activeAllButton;
    private JButton deactiveAllButton;
    private JButton resetCounterButton;

    public SettingsFrame(LinkedList chapterListRep)
    {
        settingsFrame = this;
        setjScrollPane(chapterListRep);
        add(jScrollPane, BorderLayout.CENTER);
        setBottomPanel();
        add(bottomPanel, BorderLayout.PAGE_END);

        chapterList.setBackground(Color.WHITE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setResizable(false);
        setLocation(Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setActionListeners();
        setVisible(true);
    }

    public void addChapterPanel(ChapterPanel chapterPanel)
    {
        chapterList.addChapterPanel(chapterPanel);
    }

    public void setChapterList(LinkedList representation)
    {
        chapterList.setChapterList(representation);
    }

    private void setjScrollPane(LinkedList chapterListRep)
    {
        chapterList = new ChapterList(chapterListRep);
        chapterList.setMinimumSize(new Dimension(CHAPTER_LIST_WIDTH, CHAPTER_LIST_HEIGTH));

        jScrollPane = new JScrollPane(chapterList);
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

    private void setBottomPanel()
    {
        setButtons();
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
        bottomPanel.setPreferredSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));

        int spaceBetweenButtons = 30;
        bottomPanel.add(Box.createRigidArea(new Dimension(20,0)));
        bottomPanel.add(addButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, 0)));
        bottomPanel.add(activeAllButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, 0)));
        bottomPanel.add(deactiveAllButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(spaceBetweenButtons, 0)));
        bottomPanel.add(resetCounterButton);
        bottomPanel.add(Box.createHorizontalGlue());
    }

    private void setButtons()
    {
        addButton = new JButton("+");
        activeAllButton = new JButton("☒");
        deactiveAllButton = new JButton("☐");
        String resetButtonText = "<html>";
        resetButtonText += "<font color='green'>" + "☑" + "</font> " + 0;
        resetButtonText += "<font color='red'>" + "  ☒" + "</font> " + 0;
        resetButtonText += "</html>";
         resetCounterButton = new JButton(resetButtonText);

        stylyzeSettingsBottomMenuButton(addButton);
        stylyzeSettingsBottomMenuButton(activeAllButton);
        stylyzeSettingsBottomMenuButton(deactiveAllButton);
        stylyzeSettingsBottomMenuButton(resetCounterButton);
        resetCounterButton.setFont(new Font(resetCounterButton.getName(), Font.PLAIN, 10));

        addButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        addButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));

        activeAllButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        activeAllButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, BUTTON_HEIGTH));

        deactiveAllButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        deactiveAllButton.setMaximumSize(new Dimension(EntryFrame.BUTTON_WIDTH, BUTTON_HEIGTH));

        resetCounterButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
        resetCounterButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGTH));
    }

    public static void stylyzeSettingsBottomMenuButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 25));
    }

    private void setActionListeners()
    {
        addButton.addActionListener(new SettingsFrameListener(chapterList));
        activeAllButton.addActionListener(new SettingsFrameListener(chapterList));
        deactiveAllButton.addActionListener(new SettingsFrameListener(chapterList));
        resetCounterButton.addActionListener(new SettingsFrameListener(chapterList));

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "PRESSED DELETE");
        getRootPane().getActionMap().put("PRESSED DELETE", new KeyBinding(chapterList));
    }
}