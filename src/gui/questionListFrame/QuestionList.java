package gui.questionListFrame;

import database.chapter.question.QuestionRepresentation;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class QuestionList extends JPanel
{
    public QuestionList(LinkedList<QuestionRepresentation> questionListRep)
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        if(questionListRep != null)
        {
            QuestionPanel questionPanel;
            for(QuestionRepresentation qRep : questionListRep)
            {
                questionPanel = new QuestionPanel(qRep.getQuestionPath(), qRep.getChapterPath(),
                        qRep.getQuestionText(), qRep.getQuestionState(), qRep.isActive());
                add(questionPanel);
            }
        }
    }

    public void addQuestionPanel(QuestionPanel questionPanel)
    {
        add(questionPanel);
        revalidate();
        repaint();
    }
    public void removeQuestionPanel(QuestionPanel questionPanel)
    {
        remove(questionPanel);
        revalidate();
        repaint();
    }

    public void setQuestionList(LinkedList<QuestionRepresentation> questionListRep)
    {
        removeAll();
        if(questionListRep != null)
        {
            for(QuestionRepresentation qRep : questionListRep)
            {
                QuestionPanel questionPanel = new QuestionPanel(qRep.getQuestionPath(), qRep.getChapterPath(),
                        qRep.getQuestionText(), qRep.getQuestionState(), qRep.isActive());
                add(questionPanel);
            }
        }
        revalidate();
        repaint();
    }
}
