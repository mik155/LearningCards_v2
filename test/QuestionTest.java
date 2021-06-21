import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.lang.reflect.Field;

import Utils.Utils;
import database.Question;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class QuestionTest
{
    private static Path path;
    @Before
    public void setUp()
    {
        File directory = new File(Paths.get("").toAbsolutePath().toString());
        File [] files = directory.listFiles();
        if(files != null)
            for(File f : files)
                if(f.getName().equals("TestDirectory"))
                    fail();

        path =  Paths.get( "TestDirectory").toAbsolutePath();
        directory = new File(path.toString());
        assertTrue(directory.mkdir());
    }

    @Test
    public void emptyConstructorTest()
    {
        Question question = new Question();
        assertFalse(question.setQuestionText("text1"));
        assertFalse(question.setAnswerText("text2"));
        assertEquals(null, question.getQuestionText());
        assertEquals(null, question.getAnswerText());
    }

    @Test
    public void createQuestionTest()
    {
        Question question = new Question(path, "Question1");
        assertTrue(Utils.ifContainsFile(path, "Question1"));
        assertTrue(Utils.ifContainsFile(path, "Question1_answer"));
        assertEquals("", question.getQuestionText());
        assertEquals("", question.getAnswerText());
    }

    @Test
    public void setQuestionTextTest()
    {
        Question question = new Question(path, "Question1");
        assertTrue(Utils.ifContainsFile(path, "Question1"));
        assertTrue(Utils.ifContainsFile(path, "Question1_answer"));
        assertEquals("", question.getQuestionText());
        assertEquals("", question.getAnswerText());

        String questionText ="Oto moje pytanie. Czy nie powinieniem zadac go o swicie ?";
        question.setQuestionText(questionText);
        assertTrue(question.getQuestionText().equals(questionText));
        assertTrue(question.getAnswerText().equals(""));

        assertTrue(Utils.ifContainsFile(path, "Question1"));
        assertTrue(Utils.ifContainsFile(path, "Question1_answer"));

        Path questionPath = null;
        Path answerPath = null;
        try
        {
            Field pathField = Question.class.getDeclaredField("path");
            pathField.setAccessible(true);
            questionPath = (Path) pathField.get(question);
            answerPath = Paths.get(questionPath.toString() + "_answer");
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
            fail();
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
            fail();
        }

        assertTrue(Utils.getTextFromFile(questionPath).equals(questionText));
        assertTrue(Utils.getTextFromFile(answerPath).equals(""));
    }

    @Test
    public void setAnswerTextTest()
    {
        Question question = new Question(path, "Question1");
        assertTrue(Utils.ifContainsFile(path, "Question1"));
        assertTrue(Utils.ifContainsFile(path, "Question1_answer"));
        assertEquals("", question.getQuestionText());
        assertEquals("", question.getAnswerText());

        String answerText ="Tego nikt nie wie.";
        question.setAnswerText(answerText);
        assertTrue(question.getAnswerText().equals(answerText));
        assertTrue(question.getQuestionText().equals(""));

        assertTrue(Utils.ifContainsFile(path, "Question1"));
        assertTrue(Utils.ifContainsFile(path, "Question1_answer"));

        Path questionPath = null;
        Path answerPath = null;
        try
        {
            Field pathField = Question.class.getDeclaredField("path");
            pathField.setAccessible(true);
            questionPath = (Path) pathField.get(question);
            answerPath = Paths.get(questionPath.toString() + "_answer");
        }
        catch (NoSuchFieldException e)
        {
            System.out.println(e.toString());
            fail();
        }
        catch (IllegalAccessException e)
        {
            System.out.println(e.toString());
            fail();
        }

        assertTrue(Utils.getTextFromFile(answerPath).equals(answerText));
        assertTrue(Utils.getTextFromFile(questionPath).equals(""));
    }

    @Test
    public void removeQuestionTest()
    {
        Question question = new Question(path, "Question");

        assertTrue(Utils.ifContainsFile(path, "Question"));
        assertTrue(Utils.ifContainsFile(path, "Question_answer"));

        question.removeQuestion();

        assertFalse(Utils.ifContainsFile(path, "Question"));
        assertFalse(Utils.ifContainsFile(path, "Question_answer"));
        assertEquals(null, question.getQuestionText());
        assertEquals(null, question.getAnswerText());
    }

    @After
    public void cleanUp()
    {
        File directory = new File(path.toString());
        Utils.deleteDirectory(directory);
    }

    @AfterClass
    public static void afterAll()
    {
        File directory = new File(path.toString());
        Utils.deleteDirectory(directory);
    }
}