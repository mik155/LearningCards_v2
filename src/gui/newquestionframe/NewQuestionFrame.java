package gui.newquestionframe;

import actionlisteners.newquestionframe.NewQuestionFrameListener;
import gui.entryframe.EntryFrame;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class NewQuestionFrame extends JFrame
{
    public static final int FRAME_WIDTH = EntryFrame.FRAME_WIDTH;
    public static final int FRAME_HEIGTH = EntryFrame.FRAME_HEIGTH;

    public static final int TEXT_AREA_WIDTH = EntryFrame.TEXT_AREA_WIDTH;
    public static final int TEXT_AREA_HEIGTH = EntryFrame.TEXT_AREA_WIDTH;

    public static final int BOTTOM_MENU_WIDTH = EntryFrame.BOTTOM_MENU_WIDTH;
    public static final int BOTTOM_MENU_HEIGTH = EntryFrame.BOTTOM_MENU_HEIGTH;

    public static NewQuestionFrame openedNewQuestionFrame = null;

    private QuestionPanel questionPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JScrollPane questionScrollPane;
    private JScrollPane answerScrollPane;
    private JTextArea questionTextArea;
    private JTextArea answerTextArea;

    private ChapterPanel chapterPanel;


    public NewQuestionFrame(QuestionPanel qPanel, ChapterPanel cPanel)
    {
        chapterPanel = cPanel;
        questionPanel = qPanel;
        openedNewQuestionFrame = this;
        setLocation(Utils.Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        add(Box.createRigidArea(new Dimension(0, 30)), BorderLayout.PAGE_START);
        add(getCenterMenu(), BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.LINE_START);
        add(Box.createRigidArea(new Dimension(40, 0)), BorderLayout.LINE_END);
        add(getBottomMenu(), BorderLayout.PAGE_END);

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setActionListeners();

        setVisible(true);
    }

    public String getQuestionText()
    {
        return questionTextArea.getText();
    }

    public String getAnswerText()
    {
        return answerTextArea.getText();
    }

    public ChapterPanel getChapterPanel() {
        return chapterPanel;
    }

    private JPanel getCenterMenu()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


        JLabel questionLabel = new JLabel("• Question:", SwingConstants.RIGHT);
        questionLabel.setFont(new Font(questionLabel.getFont().getName(), Font.PLAIN, 20));
        setQuestionTextArea();

        JLabel answerLabel = new JLabel("• Answer:", SwingConstants.LEFT);
        answerLabel.setFont(new Font(answerLabel.getFont().getName(), Font.PLAIN, 20));
        setAnswerTextArea();

        panel.add(questionLabel);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(questionScrollPane);
        panel.add(Box.createRigidArea(new Dimension(0,30)));
        panel.add(answerLabel);
        panel.add(Box.createRigidArea(new Dimension(0,15)));
        panel.add(answerScrollPane);

        return panel;
    }

    private void setQuestionTextArea()
    {
        questionTextArea = new JTextArea();
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);

        questionScrollPane = new JScrollPane(questionTextArea);
        questionScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void setAnswerTextArea()
    {
        answerTextArea = new JTextArea();
        answerTextArea.setLineWrap(true);
        answerTextArea.setWrapStyleWord(true);

        answerScrollPane = new JScrollPane(answerTextArea);
        answerScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private JPanel getBottomMenu()
    {
        okButton = new JButton("OK");
        EntryFrame.stylyzeButton(okButton);

        cancelButton = new JButton("CANCEL");
        EntryFrame.stylyzeButton(cancelButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(43, 0)));
        panel.add(okButton);
        panel.add(Box.createRigidArea(new Dimension(30, 0)));
        panel.add(cancelButton);

        panel.setPreferredSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));
        panel.setMaximumSize(new Dimension(BOTTOM_MENU_WIDTH, BOTTOM_MENU_HEIGTH));

        return panel;
    }

    private void setActionListeners()
    {
        okButton.addActionListener(new NewQuestionFrameListener(questionPanel));
        cancelButton.addActionListener(new NewQuestionFrameListener(questionPanel));
    }
}
