package gui.questionListFrame;

import database.chapter.ChapterRepresentation;
import database.chapter.question.QuestionRepresentation;
import gui.settingsframe.ChapterPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class QuestionList extends JPanel
{
    public QuestionList(LinkedList<QuestionRepresentation> questionListRep)
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        if(questionListRep != null)
        {
            QuestionPanel questionPanel = null;
            for(QuestionRepresentation qRep : questionListRep)
            {
                questionPanel = new QuestionPanel(qRep.getQuestionPath(), qRep.getChapterPath(),
                        qRep.getDescription(), qRep.getQuestionState(), qRep.isActive());
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

    public void setQuestionList(LinkedList questionListRep)
    {
        removeAll();
        if(questionListRep != null)
        {
            Iterator iterator = questionListRep.listIterator();
            while (iterator.hasNext()) {
                QuestionRepresentation qRep = (QuestionRepresentation) iterator.next();
                QuestionPanel questionPanel = new QuestionPanel(qRep.getQuestionPath(), qRep.getChapterPath(),
                        qRep.getDescription(), qRep.getQuestionState(), qRep.isActive());
                add(questionPanel);
            }
        }
        revalidate();
        repaint();
    }
}
