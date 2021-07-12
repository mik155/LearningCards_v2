package gui.questionListFrame;

import javax.swing.*;
import java.awt.*;

public class QuestionList extends JPanel
{
    public QuestionList()
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.LIGHT_GRAY);
    }

    public void addQuestionPanel(QuestionPanel questionPanel)
    {
        add(questionPanel);
    }
}
