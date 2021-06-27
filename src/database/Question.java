package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import Utils.Utils;

public class Question
{
    /*
     public static Question readQuestion(final Path questionFilePath)
            Read question from file. Returns null if reading is impossible.


     public boolean setQuestionText(final String text)
     public boolean setAnswerText(final String text)
     public void setState(QuestionState state)


     public String getQuestionText()
     public String getAnswerText()
     public QuestionState getState()
     public Path getPath()


     public boolean removeQuestion()
            Removes files correlated with Question.
            Sets path, question, answer to null.

     public void active(boolean b)
     public boolean isActive()
     */
    private Path path;
    private String question;
    private String answer;
    private boolean isActive;
    private QuestionState state;

    public Question(final Path path, final String name)
    {
        state = QuestionState.NO_ANSWER;
        isActive = true;
        createQuestion(path, name);
    }

    public Question()
    {
        path = null;
        question = null;
        answer = null;
    }

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

    public boolean setAnswerText(final String text)
    {
        if(path != null)
        {
            try
            {
                FileWriter fileWriter = new FileWriter(path.toString() + "_answer");
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

    public void active(boolean b)
    {
        isActive = b;
    }

    public String getQuestionText()
    {
        return question;
    }

    public String getAnswerText()
    {
        return answer;
    }

    public Path getPath()
    {
        return this.path;
    }

    public boolean isActive()
    {
        return isActive;
    }

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

    public static Question readQuestion(final Path questionFilePath)
    {
        File file = new File(questionFilePath.toString());
        File answerFile = new File(questionFilePath.toString() + File.separator + "_answer");

        if(!file.exists() || !file.isFile() || !answerFile.exists() || !answerFile.isFile())
            return null;

        Question question = new Question();
        question.path = questionFilePath;
        question.active(true);

        String questionText = Utils.getTextFromFile(questionFilePath);
        if(questionText.equals(null))
            return null;

        String answerText = Utils.getTextFromFile(Paths.get(answerFile.getAbsolutePath().toString()));
        if(answerText.equals(null))
            return null;

        question.setQuestionText(questionText);
        question.setAnswerText(answerText);
        question.setState(QuestionState.NO_ANSWER);

        return question;
    }

    public QuestionState getState()
    {
        return state;
    }

    public void setState(QuestionState state)
    {
        this.state = state;
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

    private boolean createQuestion(final Path path, final String name)
    {
        File directory = new File(path.getParent().toString());
        if(!directory.isDirectory())
            return false;

        if(Utils.ifContainsFile(path, name) ||
                Utils.ifContainsFile(path, name + "_answer"))
            return false;

        File questionTextFile = new File(path.toString() + File.separator + name);
        File answerTextFile = new File(path.toString() + File.separator + name + "_answer");

        try
        {
            if(questionTextFile.createNewFile() && answerTextFile.createNewFile())
            {
                question = "";
                answer = "";
                this.path = Paths.get(path.toString() + File.separator + name);
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
