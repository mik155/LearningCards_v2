package gui;

import Utils.Utils;
import database.Database;
import database.Mode;
import database.chapter.question.Question;
import gui.entryframe.EntryFrame;
import gui.entryframe.TEXT_AREA_DISPLAY;
import org.junit.Before;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MainTest
{
    private static Path path;

    public static void main(String [] args)
    {
        path = Paths.get("/home/nicolaus/Desktop/JavaLearningCards/LearningCardsDatabase1").toAbsolutePath();
        Database database = new Database(path, Mode.OPEN);
        Question question = database.nextQuestion();

        EntryFrame entryFrame = new EntryFrame(database.getCorrectAnsweredQuestions(),
                database.getInCorrectAnsweredQuestions(),
                database.getActiveQuestionsNumber(),
                question != null ? question.getQuestionText() : null,
                TEXT_AREA_DISPLAY.QUESTION);
    }

    private static void setUp() {
        File directory = new File(Paths.get("").toAbsolutePath().toString());
        File[] files = directory.listFiles();
        if (files != null)
            for (File f : files)
                if (f.getName().equals("TestDirectory"))
                    fail();

        path = Paths.get("TestDirectory").toAbsolutePath();
        directory = new File(path.toString());
        assertTrue(directory.mkdir());
    }

    private static void cleanUp()
    {
        File directory = new File(path.toString());
        Utils.deleteDirectory(directory);
    }
}
