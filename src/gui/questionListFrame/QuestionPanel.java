package gui.questionListFrame;

import actionlisteners.questionlistframe.questionlist.QuestionPanelListener;
import database.chapter.question.QuestionRepresentation;
import database.chapter.question.QuestionState;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class QuestionPanel extends JPanel
{
    private JLabel nameLabel;
    private QuestionStateLabel questionStateLabel;
    private JButton resetCounterButton;
    private JCheckBox checkBox;
    private Path questionPath;
    private Path chapterPath;

    public QuestionPanel( Path qPath,  Path cPath, String labelText, QuestionState state, boolean active)
    {
        questionPath = qPath;
        chapterPath = cPath;
        setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
        setPreferredSize(new Dimension(QuestionListFrame.QUESTION_PANEL_WIDTH, QuestionListFrame.QUESTION_PANEL_HEIGTH));
        setMaximumSize(new Dimension(QuestionListFrame.QUESTION_PANEL_WIDTH, QuestionListFrame.QUESTION_PANEL_HEIGTH));
        setMinimumSize(new Dimension(QuestionListFrame.QUESTION_PANEL_WIDTH, QuestionListFrame.QUESTION_PANEL_HEIGTH));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        setNameLabel(labelText);
        setQuestionStateLabel(state);
        setResetCounterButton();
        setCheckBox(active);

        add(Box.createRigidArea(new Dimension(30, 0)));
        add(nameLabel);
        add(Box.createRigidArea(new Dimension(20, 0)));
        add(questionStateLabel);
        add(Box.createRigidArea(new Dimension(30,0)));
        add(resetCounterButton);
        add(Box.createRigidArea(new Dimension(20,0)));
        add(checkBox);
        add(Box.createRigidArea(new Dimension(30,0)));
        setActionListeners();
    }

    public Path getChapterPath()
    {
        return chapterPath;
    }

    public Path getQuestionPath()
    {
        return questionPath;
    }

    /**
     * Changes chapter panel color
     * */
    public void setClickedColor()
    {
        Color color = new Color(188, 201, 191);
        setBackground(color);
        nameLabel.setBackground(color);
        questionStateLabel.setBackground(color);
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
        questionStateLabel.setBackground(color);
        resetCounterButton.setBackground(color);
        checkBox.setBackground(color);
        repaint();
    }

    public void update(QuestionRepresentation questionRepresentation)
    {
        nameLabel.setText(questionRepresentation.getDescription());
        questionStateLabel.setStateLabel(questionRepresentation.getQuestionState());
        if(questionRepresentation.isActive())
            checkBox.setSelected(true);
        else
            checkBox.setSelected(false);
        repaint();
    }

    private void setNameLabel(String labelText)
    {
        nameLabel = new JLabel(labelText);
        nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.PLAIN, 15));
        Dimension nameLabelSize = new Dimension(300, 50);
        nameLabel.setPreferredSize(nameLabelSize);
        nameLabel.setMaximumSize(nameLabelSize);
        nameLabel.setMinimumSize(nameLabelSize);
        nameLabel.setOpaque(true);
    }

    private void setQuestionStateLabel(QuestionState state)
    {
        questionStateLabel = new QuestionStateLabel(state);
        questionStateLabel.setPreferredSize(new Dimension(100, 50));
    }

    private void setResetCounterButton()
    {
        resetCounterButton = new JButton("RESET");
        ChapterPanel.stylyzeResetCounterButton(resetCounterButton);
    }

    private void setCheckBox(boolean active)
    {
        checkBox = new JCheckBox();
        checkBox.setOpaque(true);
        if(active)
            checkBox.setSelected(true);
        else
            checkBox.setSelected(false);
        checkBox.setOpaque(true);
    }

    private void setActionListeners()
    {
        addMouseListener(new QuestionPanelListener(this));
        resetCounterButton.addActionListener(new QuestionPanelListener(this));
        checkBox.addActionListener(new QuestionPanelListener(this));
    }
}
