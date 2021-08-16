package gui;

import javax.swing.*;
import java.awt.*;

public class Counter extends JPanel
{
    private int correctAnswered;
    private int incorrectAnswered;
    private int active;
    final private JLabel label;

    public Counter(int c, int ic, int a)
    {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        correctAnswered = c;
        incorrectAnswered = ic;
        active = a;

        label = new JLabel(getLabelText());
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 15));
        add(label);
    }

    /**
     * Sets counuter correct, incorrect, asctive questions numbers. Repaints counter.
     * @param correct number of correct answered questions
     * @param incorrect number of incorrect answered questions
     * @param act  number of active questions
     */
    public void setCounter(int correct, int incorrect, int act)
    {
        correctAnswered = correct;
        incorrectAnswered = incorrect;
        active = act;
        label.setText(getLabelText());
        repaint();
    }

    private String getLabelText()
    {
        String out = "<html>";
        out += "<font color='green'>" + "☑" + "</font> " + correctAnswered;
        out += "<font color='red'>" + "  ☒" + "</font> " + incorrectAnswered;
        out += "<font color='blue'>" + "  ☐" + "</font> " + active;
        out += "</html>";
        return out;
    }

    public String toString()
    {
        return getLabelText();
    }
}
