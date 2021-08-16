package database.chapter.question;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import Utils.Utils;

public class Question
{
    private Path path;
    private String question;
    private String answer;
    private boolean isActive;
    private QuestionState state;

    /**
     * Creates question in path directory. If given directory laready includes file named: name,
     * question is not created. Along with questionFile, questionFile_answer is created.
     * @param path path of directory where new question should be created
     * @param name name of new question file
     * */
    public Question(final Path path, final String name)
    {
        state = QuestionState.NO_ANSWER;
        isActive = true;
        createQuestion(path, name);
    }

    /**
     * Default contructor.
     * */
    public Question()
    {
        path = null;
        question = null;
        answer = null;
    }

    /**
     * Sets question content to text.
     * @param text new question content
     * */
    public boolean setQuestionText(final String text)
    {
        if(path != null)
        {
            try
            {
                FileWriter fileWriter = new FileWriter(path.toString());
                fileWriter.write(text);
                fileWriter.close();
                question = text;
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
        return false;
    }

    /**
     * Sets answer content to text.
     * @param text new answer content
     * */
    public boolean setAnswerText(final String text)
    {
        if(path != null)
        {
            try
            {
                FileWriter fileWriter = new FileWriter(path + "_answer");
                fileWriter.write(text);
                fileWriter.close();
                answer = text;
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
        return false;
    }

    /**
     * Sets question as (not)active.
     * @param b b == true, question sets to active,
     *          b == false, question sets to not active
     * */
    public void active(boolean b)
    {
        if(isActive && !b)
            setState(QuestionState.NO_ANSWER);
        isActive = b;
    }

    /**
     * Returns question content.
     * */
    public String getQuestionText()
    {
        return question;
    }

    /**
     * Returns answer content of question.
     * */
    public String getAnswerText()
    {
        return answer;
    }

    /**
     * Returns question path.
     * */
    public Path getPath()
    {
        return this.path;
    }

    /**
     * Returns active state of question.
     * */
    public boolean isActive()
    {
        return isActive;
    }

    /**
     * Removes question. If operation was succed returns true (false instead).
     * */
    public boolean removeQuestion()
    {
        if(path == null)
            return false;

        File questionFile = new File(path.toString());
        File answerFile = new File(path.toString() + "_answer");

        if(questionFile.delete() && answerFile.delete())
        {
            path = null;
            question = null;
            answer = null;
            isActive = false;
            return true;
        }
        return false;
    }

    /**
     * Read question with given path. Returns responding Question class instance.
     * */
    public static Question readQuestion(final Path questionFilePath)
    {
        File file = new File(questionFilePath.toString());
        File answerFile = new File(questionFilePath + "_answer");

        if(!file.exists() || !file.isFile() || !answerFile.exists() || !answerFile.isFile())
            return null;

        Question question = new Question();
        question.path = questionFilePath;
        question.active(true);

        String questionText = Utils.getTextFromFile(questionFilePath);
        if(questionText.equals(null))
            return null;

        String answerText = Utils.getTextFromFile(Paths.get(answerFile.getAbsolutePath()));
        if(answerText.equals(null))
            return null;

        question.setQuestionText(questionText);
        question.setAnswerText(answerText);
        question.setState(QuestionState.NO_ANSWER);

        return question;
    }

    /**
     * Returns state pf question.
     * */
    public QuestionState getState()
    {
        return state;
    }

    /**
     * Sets state of question.
     * @param state new state of question
     * */
    public void setState(QuestionState state)
    {
        this.state = state;
    }

    /**
     * Returns QuestionRepresentation class instance of question.
     * */
    public QuestionRepresentation getRepresentation()
    {
        return new QuestionRepresentation(path, isActive, state, getQuestionText(), getAnswerText());
    }

    public String toString()
    {
        return "PATH:\n" + path.toString() + "\nQUESTION:\n" + question + "\nANSWER:\n" + answer;
    }

    private void clean()
    {
        File questionTextFile = new File(path.toString());
        File answerTextFile = new File(path.toString() + File.separator + "_answer");
        if(questionTextFile.isFile())
            questionTextFile.delete();
        if (answerTextFile.isFile())
            answerTextFile.delete();
        this.path = null;
        answer = null;
        question = null;
        isActive = false;
    }


    /**
     * Updates content of answer and question.
     * @param questionText new question content
     * @param answerText new answer content
     * */
    public boolean update(String questionText, String answerText)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(path.toString());
            FileWriter fileWriterA = new FileWriter(path.toString() + "_answer");

            fileWriter.write(questionText);
            fileWriterA.write(answerText);

            fileWriter.close();
            fileWriterA.close();

            question = questionText;
            answer = answerText;

            return true;
        }
        catch (IOException exception)
        {
            return false;
        }
    }

    private boolean createQuestion(final Path path, final String name)
    {
        File directory = new File(path.getParent().toString());
        if(!directory.isDirectory())
            return false;

        if(Utils.ifContainsFile(path, name) ||
                Utils.ifContainsFile(path, name + "_answer"))
            return false;

        File questionTextFile = new File(path + File.separator + name);
        File answerTextFile = new File(path + File.separator + name + "_answer");

        try
        {
            if(questionTextFile.createNewFile() && answerTextFile.createNewFile())
            {
                question = "";
                answer = "";
                this.path = Paths.get(path + File.separator + name);
                return true;
            }
            else
            {
                clean(); //deletes question and answer files, if they exist. Sets path to null.
                return false;
            }

        }
        catch (IOException e)
        {
            clean(); //deletes question and answer files, if they exist. Sets path to null.
            return false;
        }
    }
}
