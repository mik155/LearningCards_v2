package database.chapter;

import Utils.Utils;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ChapterTest {
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

    @Test
    public void createChapter() {
        Chapter chapter = new Chapter(path, "chapter_test");
        assertTrue(Utils.ifContainsFile(path, "chapter_test"));
    }

    @Test
    public void addQuestion()
    {
        Chapter chapter = new Chapter(path, "chapter_test");
        String question = "Tats the question.";
        String answer = "Tats the answer.";
        chapter.addNewQuestion(question, answer);
        Path questionPath = Paths.get(chapter.getPath().toString() + File.separator + "question0");
        Path answerPath = Paths.get(chapter.getPath().toString() + File.separator + "question0_answer");
        assertTrue(Utils.getTextFromFile(questionPath).equals(question));
        assertTrue(Utils.getTextFromFile(answerPath).equals(answer));
    }

    @Test
    public void removeQuestion()
    {
        Chapter chapter = new Chapter(path, "chapter_test");
        String question = "Tats the question.";
        String answer = "Tats the answer.";
        Path questionPath = chapter.addNewQuestion(question, answer);
        Path answerPath = Paths.get(chapter.getPath().toString() + File.separator + "question0_answer");
        chapter.removeQuestion(questionPath);

        assertFalse(Utils.ifContainsFile(chapter.getPath(), "question0"));
        assertFalse(Utils.ifContainsFile(chapter.getPath(), "question0_answer"));
    }

    @Test
    public void removeEmpty()
    {
        Chapter chapter = new Chapter(path, "chapter_test");

        chapter.removeQuestion(Paths.get(""));

        chapter.addNewQuestion("1", "1");
        chapter.addNewQuestion("1", "1");
        chapter.addNewQuestion("1", "1");

        String question = "Tats the question.";
        String answer = "Tats the answer.";
        Path questionPath = chapter.addNewQuestion(question, answer);

        chapter.addNewQuestion("1", "1");
        chapter.addNewQuestion("1", "1");

        questionPath = Paths.get(question.toString() + "a");
        chapter.removeQuestion(questionPath);
        assertTrue(Utils.ifContainsFile(chapter.getPath(), "question0"));
        assertTrue(Utils.ifContainsFile(chapter.getPath(), "question0_answer"));
    }

    @Test
    public void setState()
    {
        Chapter chapter = new Chapter(path, "chapter_test");
        Path q1 = chapter.addNewQuestion("1","a1");
        Path q2 = chapter.addNewQuestion("2","a2");
        Path q3 = chapter.addNewQuestion("3","a3");
        Path q4 = chapter.addNewQuestion("4","a4");
        Path q5 = chapter.addNewQuestion("5","a5");

        assertEquals(chapter.getAnsweredQuestions(), 0);
        assertEquals(chapter.getInCorrectAnsweredQuestions(), 0);
        assertEquals(chapter.getCorrectAnsweredQuestions(), 0);
        assertEquals(chapter.getActiveQuestionsNumber(), 5);

        chapter.setQuestionState(q3, QuestionState.CORRECT);
        chapter.setQuestionState(q2, QuestionState.CORRECT);
        chapter.setQuestionState(q5, QuestionState.IN_CORRECT);

        assertEquals(chapter.getAnsweredQuestions(), 3);
        assertEquals(chapter.getInCorrectAnsweredQuestions(), 1);
        assertEquals(chapter.getCorrectAnsweredQuestions(), 2);
        assertEquals(chapter.getActiveQuestionsNumber(), 5);
    }

    @Test
    public void setStateEmpty()
    {
       Chapter chapter = new Chapter(path, "chapter_test");

       chapter.setQuestionState(Paths.get(""), QuestionState.CORRECT);

       assertEquals(0, chapter.getActiveQuestionsNumber());
       assertEquals(0, chapter.getActiveQuestionsNumber());
       assertEquals(0, chapter.getInCorrectAnsweredQuestions());
       assertEquals(0, chapter.getCorrectAnsweredQuestions());

       chapter.setQuestionState(Paths.get(path.toString()+ File.separator + "question0"), QuestionState.CORRECT);

       assertEquals(0, chapter.getActiveQuestionsNumber());
       assertEquals(0, chapter.getActiveQuestionsNumber());
       assertEquals(0, chapter.getInCorrectAnsweredQuestions());
       assertEquals(0, chapter.getCorrectAnsweredQuestions());

    }

    @Test
    public void readChapter()
    {
        Chapter newChapter = new Chapter(path, "chapter_test");
        newChapter.addNewQuestion("1", "a1");
        newChapter.addNewQuestion("2", "a2");
        newChapter.addNewQuestion("3", "a3");

        Chapter importedChapter = Chapter.readChapter(newChapter.getPath());

        assertNotNull(importedChapter);
        assertEquals(newChapter.getName(), importedChapter.getName());

        assertTrue(hasSameQuestions(importedChapter, newChapter));
    }

    @Test
    public void read2Chapters()
    {
        Chapter chapter_1 = new Chapter(path, "chapter1");
        Chapter chapter_2 = new Chapter(path, "chapter2");

        chapter_1.addNewQuestion("c1", "c1");
        chapter_1.addNewQuestion("c2", "c2");
        chapter_1.addNewQuestion("c3", "c3");

        chapter_2.addNewQuestion("c4", "c4");
        chapter_2.addNewQuestion("c5", "c5");
        chapter_2.addNewQuestion("c6", "c6");

        Chapter nChapter1 = Chapter.readChapter(chapter_1.getPath());
        Chapter nChapter2 = Chapter.readChapter(chapter_2.getPath());

        assertEquals(3, nChapter1.getActiveQuestionsNumber());
        assertEquals(3, nChapter2.getActiveQuestionsNumber());
        assertNotEquals(nChapter1.getName(), nChapter2.getName());
        assertFalse(hasSameQuestions(chapter_1, chapter_2));
    }

    @Test
    public void nextOnEmptyChapter()
    {
        Chapter chapter = new Chapter(path, "test");
        assertEquals(null, chapter.nextQuestion());
    }

    @Test
    public void nextQuestionAllActiveQuestions()
    {
        Chapter chapter = new Chapter(path, "chapter_test");

        String questionText = "text";
        for(int i = 0; i < 100; i++)
        {
            chapter.addNewQuestion(questionText, questionText + "_answer");
            questionText += Integer.toString(i);
        }

        questionText = "text";
        int counter = 0;
        while (true)
        {
            Question newQuestion = chapter.nextQuestion();
            if(newQuestion == null)
                break;
            assertEquals(newQuestion.getQuestionText(), questionText);
            assertEquals(newQuestion.getAnswerText(), questionText + "_answer");
            questionText += Integer.toString(counter);
            counter++;
        }
        assertEquals(100, counter);
    }

    @Test
    public void nextMixedQuestionState()
    {
        int [] active = {0, 1, 34, 35, 77, 98, 99};
        testNext(active);

        int [] active2 = {6, 7, 8, 19, 35, 77, 98, 99};
        testNext(active2);

        int [] active3 = {0, 7, 8, 19, 35, 77, 78, 88};
        testNext(active3);

        int [] active4 = {99};
        testNext(active4);

        int [] active5 = {0};
        testNext(active5);

        int [] active6 = {7};
        testNext(active6);
    }

    private void testNext(int [] active)
    {
        cleanUp();
        setUp();

        Chapter chapter = new Chapter(path, "chapter");
        boolean [] array  = new boolean [100];

        for(int i = 0; i < active.length; i++)
            array[active[i]] = true;

        for(int i = 0; i < 100; i++)
        {
            Path questionPath = chapter.addNewQuestion(Integer.toString(i), Integer.toString(i));
            if(!array[i])
                chapter.setQuestionState(questionPath, QuestionState.CORRECT);
        }

        int i = 0;
        while (true)
        {
            Question question = chapter.nextQuestion();
            if(question == null)
                break;
            assertEquals(Integer.toString(active[i]), question.getQuestionText());
            assertEquals(Integer.toString(active[i]), question.getAnswerText());
            i++;
        }

        chapter.reset();

        i = 0;
        while (true)
        {
            Question question = chapter.nextQuestion();
            if(question == null)
                break;
            assertEquals(Integer.toString(active[i]), question.getQuestionText());
            assertEquals(Integer.toString(active[i]), question.getAnswerText());
            i++;
        }
    }


    @After
    public void cleanUp()
    {
        File directory = new File(path.toString());
        Utils.deleteDirectory(directory);
    }

    private boolean hasSameQuestions(Chapter chapter1, Chapter chapter2)
    {
        List nList = new LinkedList<String>();

        while (true)
        {
            Question newQuestion = chapter1.nextQuestion();
            if(newQuestion == null)
            {
                chapter1.reset();
                break;
            }

            nList.add(newQuestion.toString());
        }

        List iList = new LinkedList<String>();
        while (true)
        {
            Question newQuestion = chapter2.nextQuestion();
            if(newQuestion == null)
            {
                chapter2.reset();
                break;
            }

            iList.add(newQuestion.toString());
        }

        int nSize = nList.size();
        int iSize = iList.size();

        nList.retainAll(iList);
        iList.retainAll(nList);

        return nSize == nList.size() && iSize == iList.size() && nSize == iSize;
    }
}