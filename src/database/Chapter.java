package database;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import Utils.Utils;

public class Chapter
{
    private List<Question> questionList;
    private String chapterName;
    private Path path;

    private int activeQuestionsNumber;

    public Chapter(final Path path, final String name)
    {
        createChapter(path, name);
    }

    public boolean createChapter(final Path path, final String name)
    {
        if(!Utils.ifContainsFile(path, name))
        {
            chapterName = name;
            questionList = new LinkedList<Question>();
            activeQuestionsNumber = 0;
            File newDirectory = new File(path.toString() + File.separator + name);
            newDirectory.mkdir();
            return true;
        }
        return false;
    }

    public void setName(final String name)
    {
        chapterName = name;
    }

    public String getName()
    {
        return chapterName;
    }
}
