package gui.settingsframe;

import database.chapter.ChapterRepresentation;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ChapterList extends JPanel
{
    public ChapterList(LinkedList<ChapterRepresentation> chapterListRep)
    {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.LIGHT_GRAY);
        setChapterList(chapterListRep);
    }

    /**
     * Repaints list of chapters
     * @param chapterListRep representation of chapters structure
     * */
    public void setChapterList(LinkedList<ChapterRepresentation> chapterListRep)
    {
        removeAll();
        if(chapterListRep != null)
        {
            for(ChapterRepresentation chapterRep : chapterListRep)
            {
                ChapterPanel chapterPanel = new ChapterPanel(chapterRep.getName(), chapterRep.getPath(),
                        chapterRep.getCorrect(), chapterRep.getInCorrect(), chapterRep.getActive());
                chapterPanel.setActive(chapterRep.getActive() > 0);
                add(chapterPanel);
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Adds new chapter panel to list.
     * @param chapterPanel ChapterPanel object that will be added to the list.
     * */
    public void addChapterPanel(ChapterPanel chapterPanel)
    {
        add(chapterPanel);
        revalidate();
        repaint();
    }

    /**
     * Removes new chapter panel to list.
     * @param chapterPanel ChapterPanel object that will be removed from the list.
     * */
    public void removeChapterPanel(ChapterPanel chapterPanel)
    {
        remove(chapterPanel);
        revalidate();
        repaint();
    }
}