package gui.questionListFrame;

import database.chapter.question.QuestionState;

import javax.swing.*;
import java.awt.*;

public class QuestionStateLabel extends JPanel
{
    private QuestionState state;
    private JLabel label;

    public QuestionStateLabel(QuestionState questionState)
    {
        state = questionState;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        label = new JLabel(getLabelText());
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 15));
        add(label);
    }

    private String getLabelText()
    {
        if(state.correct())
            return "<html><font color='green'>" + "☑" + "</font></html>";
        if(state.inCorrect())
            return "<html><font color='red'>" + "☒" + "</font></html>";
        if(state.noAnswer())
            return "<html><font color='blue'>" + "☐" + "</font></html>";
        return "";
    }

    public void setStateLabel(QuestionState questionState)
    {
        state = questionState;
        label.setText(getLabelText());
    }

    public String toString()
    {
        return getLabelText();
    }
}


