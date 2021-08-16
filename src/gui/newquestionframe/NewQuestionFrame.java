package gui.newquestionframe;

import actionlisteners.newquestionframe.NewQuestionFrameListener;
import gui.entryframe.EntryFrame;
import gui.questionListFrame.QuestionPanel;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.*;

public class NewQuestionFrame extends JFrame
{
    public static final int FRAME_WIDTH = EntryFrame.FRAME_WIDTH;
    public static final int FRAME_HEIGTH = EntryFrame.FRAME_HEIGTH;

    public static final int BOTTOM_MENU_WIDTH = EntryFrame.BOTTOM_MENU_WIDTH;
    public static final int BOTTOM_MENU_HEIGTH = EntryFrame.BOTTOM_MENU_HEIGTH;

    public static NewQuestionFrame openedNewQuestionFrame = null;

    private final QuestionPanel questionPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JScrollPane questionScrollPane;
    private JScrollPane answerScrollPane;
    private JTextArea questionTextArea;
    private JTextArea answerTextArea;
    private String questionText;
    private String answerText;

    final private ChapterPanel chapterPanel;


    public NewQuestionFrame(QuestionPanel qPanel, ChapterPanel cPanel, String qText, String aText)
    {
        questionText = qText;
        answerText = aText;
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
        if(questionText != null)
            questionTextArea.setText(questionText);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);

        questionScrollPane = new JScrollPane(questionTextArea);
        questionScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void setAnswerTextArea()
    {
        answerTextArea = new JTextArea();
        if(answerText != null)
            answerTextArea.setText(answerText);
        answerTextArea.setLineWrap(true);
        answerTextArea.setWrapStyleWord(true);

        answerScrollPane = new JScrollPane(answerTextArea);
        answerScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private JPanel getBottomMenu()
    {
        okButton = new JButton("OK");
        stylyzeButton(okButton);

        cancelButton = new JButton("CANCEL");
        stylyzeButton(cancelButton);

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

    private void stylyzeButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 15));
    }
}
