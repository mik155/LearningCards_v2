package gui;

import Utils.Utils;
import database.Database;
import database.Mode;
import database.chapter.question.Question;
import gui.entryframe.EntryFrame;
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
        setUp();
        Database database = new Database(path, Mode.CREATE);
        Path cpath = database.addNewChapter("chapter_1");
        database.addNewQuestion(cpath, "Pytanie nr 1.", "Odpowiedz 1");
        database.addNewQuestion(cpath, "Pytanie nr 2.", "Odpowiedz 2");
        database.addNewQuestion(cpath, "Pytanie nr 3.", "Odpowiedz 3");
        EntryFrame entryFrame = new EntryFrame(database.getCorrectAnsweredQuestions(),
                database.getInCorrectAnsweredQuestions(),
                database.getActiveQuestionsNumber());
        cleanUp();
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
