package database;

import Utils.Utils;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DatabaseTest
{
    private static Path path;

    @Before
    public void setUp() {
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

    //PUBLIC METHODS
    @Test
    public void initDatabaseCREATE()
    {
        Database database = new Database(path, Mode.CREATE);
        assertTrue(Utils.ifContainsFile(path, "LearningCardsDatabase0"));

        try
        {
            Field field = database.getClass().getDeclaredField("path");
            field.setAccessible(true);
            Path databasePath = (Path) field.get(database);
            Path correctDatabasePath = Paths.get(path.toString() + File.separator + "LearningCardsDatabase0");
            assertTrue(databasePath.equals(correctDatabasePath));
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
        }

    }

    @Test
    public void addNewChapter()
    {
        Database database = new Database(path, Mode.CREATE);
        database.addNewChapter("chapter_1");

        Path databasePath = null;
        try
        {
            Field field = database.getClass().getDeclaredField("path");
            field.setAccessible(true);
            databasePath = (Path) field.get(database);
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
        }

        assertTrue(Utils.ifContainsFile(databasePath, "chapter_1"));
    }

    @Test
    public void addNewQuestionToChapter()
    {
        Database database = new Database(path, Mode.CREATE);
        Path chapterPath = database.addNewChapter("chapter_1");
        String questionText = "Why java is awesome ?";
        String answerText = "Nobody knows.";
        Path questionPath = database.addNewQuestion(chapterPath, questionText, answerText);

        File file  = new File(questionPath.toString());
        assertTrue(file.exists());
        assertTrue(chapterPath.equals(questionPath.getParent()));

        String qText = Utils.getTextFromFile(questionPath);
        Path answerFilePath = Paths.get(questionPath.toString() + "_answer");
        String aText = Utils.getTextFromFile(answerFilePath);
        assertTrue(qText.equals(questionText));
        assertTrue(aText.equals(answerText));
    }

    @Test
    public void addExistingChapter()
    {

        Database database = new Database(path, Mode.CREATE);

        Path chapterPath = database.addNewChapter("chapter_1");
        Path questionPath1 = database.addNewQuestion(chapterPath, "1", "+1");
        Path questionPath2 = database.addNewQuestion(chapterPath, "2", "+2");
        Path answerPath1 = Paths.get(questionPath1.toString() + "_answer");
        Path answerPath2 = Paths.get(questionPath2.toString() + "_answer");
        Path secondChapter = database.addNewChapter("chapter_1");

        Path databasePath = null;
        try
        {
            Field field = database.getClass().getDeclaredField("path");
            field.setAccessible(true);
            databasePath = (Path) field.get(database);
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
        }


        assertNull(secondChapter);
        assertTrue(Utils.ifContainsFile(databasePath, "chapter_1"));
        assertTrue(Utils.ifContainsFile(chapterPath, "question0"));
        assertTrue(Utils.ifContainsFile(chapterPath, "question0_answer"));
        assertTrue(Utils.ifContainsFile(chapterPath, "question1"));
        assertTrue(Utils.ifContainsFile(chapterPath, "question1_answer"));
        assertTrue(Utils.getTextFromFile(questionPath1).equals("1"));
        assertTrue(Utils.getTextFromFile(questionPath2).equals("2"));
        assertTrue(Utils.getTextFromFile(answerPath1).equals("+1"));
        assertTrue(Utils.getTextFromFile(answerPath2).equals("+2"));
    }

    @Test
    public void readDatabase()
    {

        Database orgDatabase = new Database(path, Mode.CREATE);
        Path path1 = orgDatabase.addNewChapter("chapter_1");
        Path path2 = orgDatabase.addNewChapter("chapter_2");
        Path path3 = orgDatabase.addNewChapter("chapter_3");

        Path qPath = orgDatabase.addNewQuestion(path1, "1", "+1");
        Path qPath2 = orgDatabase.addNewQuestion(path1, "2", "-2");
        Path qPath3 = orgDatabase.addNewQuestion(path3, "3", "-3");

        Path databasePath = null;
        try
        {
            Field field = orgDatabase.getClass().getDeclaredField("path");
            field.setAccessible(true);
            databasePath = (Path) field.get(orgDatabase);

            Database nDatabase = new Database(databasePath, Mode.OPEN);
            Path nDatabasePath = (Path) field.get(nDatabase);
            assertTrue(nDatabasePath.equals(databasePath));

        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
        }
    }

    @Test
    public void removeQuestion()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        Path cPath2 = database.addNewChapter("chapter_2");
        Path cPath3 = database.addNewChapter("chapter_3");

        database.addNewQuestion(cPath1, "a" , "b");
        database.addNewQuestion(cPath1, "c" , "d");
        database.addNewQuestion(cPath1, "e" , "f");

        Path questionPath = database.addNewQuestion(cPath2, "1", "2");
        database.addNewQuestion(cPath2, "3", "4");

        database.addNewQuestion(cPath3, "!", "@");
        database.addNewQuestion(cPath3, "#", "$");

        database.removeQuestion(cPath2, questionPath);

        File file = new File(questionPath.toString());
        assertFalse(file.exists());
    }

    @Test
    public void removeNotExistingQuestion()
    {
        Database database = new Database(path, Mode.CREATE);
        Path chapterPath = database.addNewChapter("chapter_1");
        Path questionPath = database.addNewQuestion(chapterPath, "1", "1");

        Path fakeChapterPath = Paths.get(chapterPath.toString() + "f");
        Path fakeQuestionPath = Paths.get(questionPath.toString() + "f");

        database.removeQuestion(fakeChapterPath, questionPath);
        File qFile = new File(questionPath.toString());
        File cDir = new File(chapterPath.toString());

        assertTrue(qFile.exists());
        assertTrue(cDir.exists());
        assertEquals(database.getActiveQuestionsNumber(), 1);
        assertEquals(database.getAnsweredQuestions(), 0);

        database.removeQuestion(chapterPath, fakeQuestionPath);

        assertTrue(qFile.exists());
        assertTrue(cDir.exists());
        assertEquals(database.getActiveQuestionsNumber(), 1);
        assertEquals(database.getAnsweredQuestions(), 0);
    }

    @Test
    public void removeChapter()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        Path cPath2 = database.addNewChapter("chapter_2");
        Path cPath3 = database.addNewChapter("chapter_3");

        Path dbPath = null;
        try
        {
            Field field = database.getClass().getDeclaredField("path");
            field.setAccessible(true);
            dbPath = (Path) field.get(database);
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e);
            fail("Can not get access to database Path.");
        }
        catch(IllegalAccessException e)
        {
            System.out.println(e);
            fail("Can not get access to database Path.");
        }

        database.addNewQuestion(cPath1, "1", "1");
        database.addNewQuestion(cPath1, "2", "2");
        database.addNewQuestion(cPath1, "3", "3");

        database.addNewQuestion(cPath2, "a", "b");
        database.addNewQuestion(cPath2, "c", "d");

        database.addNewQuestion(cPath3, "!", "@");
        database.addNewQuestion(cPath3, "#", "$");
        database.addNewQuestion(cPath3, "%", "^");

        assertEquals(8, database.getActiveQuestionsNumber());

        database.removeChapter(cPath2);

        assertEquals(6, database.getActiveQuestionsNumber());
        assertTrue(Utils.ifContainsFile(dbPath, "chapter_1"));
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_2"));
        assertTrue(Utils.ifContainsFile(dbPath, "chapter_3"));

        database.removeChapter(cPath2);

        assertEquals(6, database.getActiveQuestionsNumber());
        assertTrue(Utils.ifContainsFile(dbPath, "chapter_1"));
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_2"));
        assertTrue(Utils.ifContainsFile(dbPath, "chapter_3"));

        database.removeChapter(cPath1);

        assertEquals(3, database.getActiveQuestionsNumber());
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_1"));
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_2"));
        assertTrue(Utils.ifContainsFile(dbPath, "chapter_3"));

        database.removeChapter(cPath3);

        assertEquals(0, database.getActiveQuestionsNumber());
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_1"));
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_2"));
        assertFalse(Utils.ifContainsFile(dbPath, "chapter_3"));

    }

    @Test
    public void isActiveAndActive()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chapter_1");
        Path qPath1 = database.addNewQuestion(cPath, "q1", "a1");
        Path qPath2 = database.addNewQuestion(cPath, "q1", "a1");
        Path qPath3 = database.addNewQuestion(cPath, "q1", "a1");

        assertTrue(database.isActive(cPath, qPath1));
        assertTrue(database.isActive(cPath, qPath2));
        assertTrue(database.isActive(cPath, qPath3));

        database.active(cPath, qPath1, false);
        database.active(cPath, qPath3, false);

        assertFalse(database.isActive(cPath, qPath1));
        assertTrue(database.isActive(cPath, qPath2));
        assertFalse(database.isActive(cPath, qPath3));
    }

    @Test
    public void setStateAndGetGuestionState()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chapter_1");
        Path qPath1 = database.addNewQuestion(cPath, "q1", "a1");
        Path qPath2 = database.addNewQuestion(cPath, "q1", "a1");
        Path qPath3 = database.addNewQuestion(cPath, "q1", "a1");

        Path cPath2 = database.addNewChapter("chapter_2");
        Path qPath21 = database.addNewQuestion(cPath2, "q1", "a1");
        Path qPath22 = database.addNewQuestion(cPath2, "q1", "a1");
        Path qPath23 = database.addNewQuestion(cPath2, "q1", "a1");

        assertEquals(database.getAnsweredQuestions(), 0);
        assertEquals(database.getInCorrectAnsweredQuestions(), 0);
        assertEquals(database.getCorrectAnsweredQuestions(), 0);
        assertEquals(database.getActiveQuestionsNumber(), 6);

        database.setQuestionState(cPath, qPath1, QuestionState.CORRECT);
        database.setQuestionState(cPath, qPath2, QuestionState.IN_CORRECT);
        database.setQuestionState(cPath, qPath3, QuestionState.NO_ANSWER);

        assertEquals(database.getAnsweredQuestions(), 2);
        assertEquals(database.getInCorrectAnsweredQuestions(), 1);
        assertEquals(database.getCorrectAnsweredQuestions(), 1);
        assertEquals(database.getActiveQuestionsNumber(), 6);

        database.setQuestionState(cPath2, qPath21, QuestionState.CORRECT);
        database.setQuestionState(cPath2, qPath22, QuestionState.IN_CORRECT);
        database.setQuestionState(cPath2, qPath23, QuestionState.NO_ANSWER);

        assertEquals(database.getAnsweredQuestions(), 4);
        assertEquals(database.getInCorrectAnsweredQuestions(), 2);
        assertEquals(database.getCorrectAnsweredQuestions(), 2);
        assertEquals(database.getActiveQuestionsNumber(), 6);
    }

    //nextQuestion() tests
    @Test
    public void nextAllActiveLastNotEmpty()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        database.addNewQuestion(cPath1, "0", "1");
        database.addNewQuestion(cPath1, "1", "1");
        database.addNewQuestion(cPath1, "2", "1");

        database.addNewChapter("Empty chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "3", "1");
        database.addNewQuestion(cPath2, "4", "1");

        for(int i = 0; i < 5; i++)
        {
            Question question = database.nextQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }

        for(int i = 0; i < 5; i++)
        {
            Question question = database.nextQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }
    }

    @Test
    public void nextAllActiveLastEmpty()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        database.addNewQuestion(cPath1, "0", "1");
        database.addNewQuestion(cPath1, "1", "1");
        database.addNewQuestion(cPath1, "2", "1");

        database.addNewChapter("Empty chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "3", "1");
        database.addNewQuestion(cPath2, "4", "1");

        database.addNewChapter("Empty chapter2");

        for(int i = 0; i < 5; i++)
        {
            Question question = database.nextQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }

        for(int i = 0; i < 5; i++)
        {
            Question question = database.nextQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }
    }

    @Test
    public void nextAllActiveAnswered()
    {
        Database database = new Database(path, Mode.CREATE);
        boolean [] notAnswered = {true, true, false, false ,true, false};
        nextAllActiveCorrectAnsweredTest(notAnswered, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered1 = {false, true, false, false ,true, true};
        nextAllActiveCorrectAnsweredTest(notAnswered1, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered2 = {false, true, false, false ,true, false};
        nextAllActiveCorrectAnsweredTest(notAnswered2, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered3 = {false, false, false, false ,false, false};
        nextAllActiveCorrectAnsweredTest(notAnswered3, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered4 = {true, true, true, true ,true, true};
        nextAllActiveCorrectAnsweredTest(notAnswered4, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered5 = {false};
        nextAllActiveCorrectAnsweredTest(notAnswered5, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered6 = {true};
        nextAllActiveCorrectAnsweredTest(notAnswered6, database);
        cleanUp();
        setUp();
    }

    private void nextAllActiveCorrectAnsweredTest(boolean [] notAnswered, Database database)
    {
        Path cPath = database.addNewChapter("chapter_1");
        int len = notAnswered.length;
        for(int i = 0; i < len; i++)
        {
            Path qPath = database.addNewQuestion(cPath, Integer.toString(i), Integer.toString(i));
            if(!notAnswered[i])
                database.setQuestionState(cPath, qPath, QuestionState.CORRECT);
        }

        for(int i = 0 ; i < len; i++)
        {
            if(notAnswered[i])
            {
                Question question = database.nextQuestion();
                assertEquals(question.getAnswerText(), Integer.toString(i));
            }
        }

        for(int i = 0 ; i < len; i++)
        {
            if(notAnswered[i])
            {
                Question question = database.nextQuestion();
                assertEquals(question.getAnswerText(), Integer.toString(i));
            }
        }
    }

    @Test
    public void addNewQuestion_next_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chapter1");
        database.addNewQuestion(cPath, "0", "0");
        database.addNewQuestion(cPath, "1", "0");
        database.addNewQuestion(cPath, "2", "0");

        Question question = database.nextQuestion();
        assertEquals("0", question.getQuestionText());
        database.addNewQuestion(cPath, "3", "0");
        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());
        question = database.nextQuestion();
        assertEquals("2", question.getQuestionText());
        question = database.nextQuestion();
        assertEquals("3", question.getQuestionText());
        question = database.nextQuestion();
        assertEquals("0", question.getQuestionText());
    }

    @Test
    public void removeQuestion_next_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chpater_1");
        Path qPAth1 = database.addNewQuestion(cPath, "0", "1");
        Path qPAth2 = database.addNewQuestion(cPath, "1", "1");
        Path qPAth3 = database.addNewQuestion(cPath, "2", "1");

        Question question = database.nextQuestion();
        assertEquals("0", question.getQuestionText());
        database.removeQuestion(cPath, qPAth1);

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("2", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("2", question.getQuestionText());

        database.removeQuestion(cPath, qPAth3);

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());

        database.removeQuestion(cPath, qPAth2);
        assertNull(database.nextQuestion());
    }

    @Test
    public void removeQuestion_addNewQuestion_next_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        assertNull(database.nextQuestion());
        Path cPath =  database.addNewChapter("chapter_1");
        Path qPath1 = database.addNewQuestion(cPath, "1", "-");
        Path qPath2 = database.addNewQuestion(cPath, "2", "-");
        Path qPath3 = database.addNewQuestion(cPath, "3", "-");

        database.removeQuestion(cPath,qPath2);
        database.addNewQuestion(cPath, "2", "-");

        Question question;

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("3", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("2", question.getQuestionText());

        question = database.nextQuestion();
        assertEquals("1", question.getQuestionText());
    }

    //prevQuestion() tests
    @Test
    public void prevAllActiveLastNotEmpty()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        database.addNewQuestion(cPath1, "4", "1");
        database.addNewQuestion(cPath1, "3", "1");
        database.addNewQuestion(cPath1, "2", "1");

        database.addNewChapter("Empty chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "1", "1");
        database.addNewQuestion(cPath2, "0", "1");


        for(int i = 0; i < 5 ; i++)
        {
            Question question = database.previousQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }

        for(int i = 0; i < 5; i++)
        {
            Question question = database.previousQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }
    }

    @Test
    public void prevAllActiveLastEmpty()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter_1");
        database.addNewQuestion(cPath1, "4", "1");
        database.addNewQuestion(cPath1, "3", "1");
        database.addNewQuestion(cPath1, "2", "1");

        database.addNewChapter("Empty_chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "1", "1");
        database.addNewQuestion(cPath2, "0", "1");

        database.addNewChapter("Empty_chapter_2");

        for(int i = 0; i < 5 ; i++)
        {
            Question question = database.previousQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }

        for(int i = 0; i < 5; i++)
        {
            Question question = database.previousQuestion();
            String text = question.getQuestionText();
            assertEquals(Integer.toString(i), text);
        }
    }

    @Test
    public void prevAllActiveAnswered()
    {
        Database database = new Database(path, Mode.CREATE);
        boolean [] notAnswered = {true, true, false, false ,true, false};
        prevAllActiveCorrectAnsweredTest(notAnswered, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered1 = {false, true, false, false ,true, true};
        prevAllActiveCorrectAnsweredTest(notAnswered1, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered2 = {false, true, false, false ,true, false};
        prevAllActiveCorrectAnsweredTest(notAnswered2, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered3 = {false, false, false, false ,false, false};
        prevAllActiveCorrectAnsweredTest(notAnswered3, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered4 = {true, true, true, true ,true, true};
        prevAllActiveCorrectAnsweredTest(notAnswered4, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered5 = {false};
        prevAllActiveCorrectAnsweredTest(notAnswered5, database);
        cleanUp();
        setUp();

        database = new Database(path, Mode.CREATE);
        boolean [] notAnswered6 = {true};
        prevAllActiveCorrectAnsweredTest(notAnswered6, database);
        cleanUp();
        setUp();
    }

    private void prevAllActiveCorrectAnsweredTest(boolean [] notAnswered, Database database)
    {
        Path cPath = database.addNewChapter("chapter_1");
        int len = notAnswered.length;
        for(int i = 0; i < len; i++)
        {
            Path qPath = database.addNewQuestion(cPath, Integer.toString(i), Integer.toString(i));
            if(!notAnswered[i])
                database.setQuestionState(cPath, qPath, QuestionState.CORRECT);
        }

        for(int i = len - 1 ; i >= 0; i--)
        {
            if(notAnswered[i])
            {
                Question question = database.previousQuestion();
                assertEquals(question.getAnswerText(), Integer.toString(i));
            }
        }

        for(int i = len - 1 ; i >= 0; i--)
        {
            if(notAnswered[i])
            {
                Question question = database.previousQuestion();
                assertEquals(question.getAnswerText(), Integer.toString(i));
            }
        }
    }

    @Test
    public void addNewQuestion_prev_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chapter1");
        database.addNewQuestion(cPath, "0", "0");
        database.addNewQuestion(cPath, "1", "0");
        database.addNewQuestion(cPath, "2", "0");

        Question question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());
        database.addNewQuestion(cPath, "3", "0"); // 0 1 2 3
        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());
        question = database.previousQuestion();
        assertEquals("0", question.getQuestionText());
        question = database.previousQuestion();
        assertEquals("3", question.getQuestionText());
        question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());
    }

    @Test
    public void removeQuestion_prev_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chpater_1");
        Path qPAth1 = database.addNewQuestion(cPath, "0", "1");
        Path qPAth2 = database.addNewQuestion(cPath, "1", "1");
        Path qPAth3 = database.addNewQuestion(cPath, "2", "1");

        Question question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());
        database.removeQuestion(cPath, qPAth1);

        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());

        database.removeQuestion(cPath, qPAth3);

        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());

        database.removeQuestion(cPath, qPAth2);
        assertNull(database.previousQuestion());
    }

    @Test
    public void removeQuestion_addNewQuestion_prev_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        assertNull(database.nextQuestion());
        Path cPath =  database.addNewChapter("chapter_1");
        Path qPath1 = database.addNewQuestion(cPath, "1", "-");
        Path qPath2 = database.addNewQuestion(cPath, "4", "-");
        Path qPath3 = database.addNewQuestion(cPath, "2", "-");

        database.removeQuestion(cPath,qPath2);
        database.addNewQuestion(cPath, "3", "-");

        Question question;

        question = database.previousQuestion();
        assertEquals("3", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("2", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("1", question.getQuestionText());

        question = database.previousQuestion();
        assertEquals("3", question.getQuestionText());
    }

    @Test
    public void next_prev_mixed_0()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath = database.addNewChapter("chapter");
        database.addNewQuestion(cPath, "0", "-");
        database.addNewQuestion(cPath, "1", "-");
        database.addNewQuestion(cPath, "2", "-");
        database.addNewQuestion(cPath, "3", "-");
        database.addNewQuestion(cPath, "4", "-");

        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
        assertEquals("2", database.nextQuestion().getQuestionText());
        assertEquals("3", database.nextQuestion().getQuestionText());
        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("1", database.previousQuestion().getQuestionText());
        assertEquals("0", database.previousQuestion().getQuestionText());
        assertEquals("4", database.previousQuestion().getQuestionText());
        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
    }

    @Test
    public void next_prev_mixed_1()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter");
        database.addNewQuestion(cPath1, "0", "-");
        database.addNewQuestion(cPath1, "1", "-");
        database.addNewQuestion(cPath1, "2", "-");

        database.addNewChapter("Empty chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "3", "-");
        database.addNewQuestion(cPath2, "4", "-");
        //0 1 2 | 3 4 |
        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
        assertEquals("2", database.nextQuestion().getQuestionText());
        assertEquals("3", database.nextQuestion().getQuestionText());

        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("1", database.previousQuestion().getQuestionText());
        assertEquals("0", database.previousQuestion().getQuestionText());
        assertEquals("4", database.previousQuestion().getQuestionText());
        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
    }

    @Test
    public void next_prev_mixed_2()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter");
        database.addNewQuestion(cPath1, "0", "-");
        database.addNewQuestion(cPath1, "1", "-");
        database.addNewQuestion(cPath1, "2", "-");

        database.addNewChapter("Empty chapter");

        Path cPath2 = database.addNewChapter("chapter_2");
        database.addNewQuestion(cPath2, "3", "-");
        database.addNewQuestion(cPath2, "4", "-");

        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
        assertEquals("2", database.nextQuestion().getQuestionText());
        assertEquals("3", database.nextQuestion().getQuestionText());
        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("1", database.previousQuestion().getQuestionText());
        assertEquals("0", database.previousQuestion().getQuestionText());
        assertEquals("4", database.previousQuestion().getQuestionText());
        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());

        database.addNewChapter("Empty chapter 2");
    }

    @Test
    public void next_prev_addNewQuestion_removeQuestion_mixed()
    {
        Database database = new Database(path, Mode.CREATE);
        Path cPath1 = database.addNewChapter("chapter");
        Path cPath2 = database.addNewChapter("chapter 2");
        Path cPath3 = database.addNewChapter("chapter 3");

        database.addNewQuestion(cPath1, "0", "-");
        Path qPath1 = database.addNewQuestion(cPath1, "1", "-");
        database.addNewQuestion(cPath1, "2", "-");
        Path qPath3 = database.addNewQuestion(cPath1, "3", "-");

        database.addNewQuestion(cPath2, "4", "-");
        Path qPAth5 =  database.addNewQuestion(cPath2, "5", "-");
        database.addNewQuestion(cPath2, "6", "-");

        database.addNewQuestion(cPath3, "7", "-");
        //0 1 2 3 |4 5 6| 7
        assertEquals("0", database.nextQuestion().getQuestionText());
        assertEquals("1", database.nextQuestion().getQuestionText());
        assertEquals("2", database.nextQuestion().getQuestionText());
        assertEquals("3", database.nextQuestion().getQuestionText());
        database.removeQuestion(cPath1, qPath1); //0 2 3 |4 5 6| 7
        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("0", database.previousQuestion().getQuestionText());
        database.removeQuestion(cPath1, qPath3); // 0 2 |4 5 6| 7
        assertEquals("2", database.nextQuestion().getQuestionText());
        database.removeQuestion(cPath2, qPAth5);// 0 2 |4 6| 7
        assertEquals("4", database.nextQuestion().getQuestionText());
        assertEquals("6", database.nextQuestion().getQuestionText());
        assertEquals("7", database.nextQuestion().getQuestionText());
        assertEquals("6", database.previousQuestion().getQuestionText());
        assertEquals("4", database.previousQuestion().getQuestionText());
        database.addNewQuestion(cPath1, "10", "-");//0 2 10|4 6| 7
        database.addNewQuestion(cPath1, "20", "-");//0 2 10 20|4 6| 7
        assertEquals("20", database.previousQuestion().getQuestionText());
        assertEquals("10", database.previousQuestion().getQuestionText());
        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("0", database.previousQuestion().getQuestionText());
        assertEquals("7", database.previousQuestion().getQuestionText());
        assertEquals("6", database.previousQuestion().getQuestionText());
        assertEquals("4", database.previousQuestion().getQuestionText());
        assertEquals("20", database.previousQuestion().getQuestionText());
        assertEquals("10", database.previousQuestion().getQuestionText());
        assertEquals("2", database.previousQuestion().getQuestionText());
        assertEquals("10", database.nextQuestion().getQuestionText());

    }

    @After
    public void cleanUp()
    {
        File directory = new File(path.toString());
        Utils.deleteDirectory(directory);
    }
}
