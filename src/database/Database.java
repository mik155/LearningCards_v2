package database;

import Utils.Utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class Database {
    public static Database database;

    private Path path;
    private List chapterList;

    private int activeQuestions;
    private int correntAnsweredQuestions;
    private int incorrectAnsweredQuestions;

    public Database(final Path initDirectory, Mode mode) {
        database = null;
        chapterList = null;

        File file = new File(initDirectory.toString());
        if (mode.open()) {
            //read database from disk
            if (file.exists() && file.isDirectory()) {
                path = initDirectory;
                chapterList = new LinkedList<Chapter>();
                downloadChaptersFromDirectory(path);
                database = this;
            }
        } //if(mode.open())

        if (mode.create()) {
            //create new database
            if (file.isDirectory()) {
                String dirName = Utils.getFreeFileName(initDirectory, "LearningCardsDatabase");
                String absolutePath = initDirectory.toString() + File.separator + dirName;
                File databaseDirectory = new File(absolutePath);
                if (databaseDirectory.mkdir()) {
                    path = Paths.get(absolutePath);
                    chapterList = new LinkedList<Question>();
                    database = this;
                    activeQuestions = 0;
                    correntAnsweredQuestions = 0;
                    incorrectAnsweredQuestions = 0;
                }
            }
        } //if(mode.create())

    } //Database(final Path initDirectory, Mode mode)

    public Path addNewQuestion(final Path chapterPath, final String questionText, final String answerText) {
        Chapter chapter = getChapter(chapterPath);
        if (!chapter.equals(null)) {
            return chapter.addNewQuestion(questionText, answerText);
        }
        return null;
    }

    public Path addNewChapter(final String chapterName)
    {
        
    }

    private Chapter getChapter(final Path chapterPath)
    {
        Iterator iterator = chapterList.iterator();
        Chapter chapter = null;
        while (iterator.hasNext())
        {
            chapter = (Chapter) iterator.next();
            if(chapter.getPath().equals(chapterPath))
                return chapter;
        }
        return null;
    }

    private void downloadChaptersFromDirectory(final Path dirPath)
    {
        File file = new File(dirPath.toString());
        if(file.exists() && file.isDirectory())
        {
            File [] files = file.listFiles();
            for(File f : files)
            {
                if(isChapterName(f.toPath().getFileName().toString()))
                {
                    Chapter newChapter = Chapter.readChapter(dirPath);
                    if(newChapter == null)
                        continue;
                    chapterList.add(newChapter);
                    activeQuestions += newChapter.getActiveQuestionsNumber();
                    correntAnsweredQuestions += newChapter.getCorrectAnsweredQuestions();
                    incorrectAnsweredQuestions += newChapter.getInCorrectAnsweredQuestions();
                } //if(isChapterName(f.toPath().getFileName().toString()))
            } //for
        } // if(file.exists() && file.isDirectory())
    }

    private boolean isChapterName(final String name)
    {
        return name.matches("^chapter");
    }
}
