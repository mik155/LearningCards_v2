package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Question
{
    /*
     boolean setQuestionText(final String text)
     boolean setAnswerText(final String text)
     String getQuestionText()
     String getAnswerText()
     boolean removeQuestion()
     */
    private Path path;
    private String question;
    private String answer;
    private boolean isActive;

    public Question(final Path path, final String name)
    {
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

    public boolean isActive()
    {
        return isActive;
    }

    public boolean createQuestion(final Path path, final String name)
    {
        File directory = new File(path.getParent().toString());
        if(!directory.isDirectory())
            return false;

        File [] files = directory.listFiles();
        for(File f : files)
        {
            if(f.getName().equals(name))
                return false;
        }

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
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
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
            return true;
        }
        return false;
    }
}
