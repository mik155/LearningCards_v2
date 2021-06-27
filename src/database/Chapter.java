package database;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Utils.Utils;

public class Chapter {

    /*
      public static Chapter readChapter(final Path path)
            returns new Chapter correlated with directory. Doesn't create new directory/files.
            Returned Chapter includes connection with directory from disk.
            (path argument - path to mentioned directory).
            Returns null if reading wasn't succesful.

      public Path addNewQuestion(final String questionText, final String answerText)
            if operation succed, returns path to created question. In other case returns null.

      public booolen removeQuestion(final Path path)
            if operation succed, returns true. In other case returns false.


      public void setName(final String name)
      public void setActive(boolean active)
      public boolean setQuestionState(final Path path,final QuestionState state)

      public String getName()
      public Path getPath()
      public int getActiveQuestionsNumber()
      public int getInCorrectAnsweredQuestions()
      public int getCorrectAnsweredQuestions()
      public int getAnsweredQuestions()
    */
    private List<Question> questionList;
    private String chapterName;
    private Path path;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;

    public Chapter(final Path path, final String name) {
        createChapter(path, name);
    }

    private Chapter() {
        questionList = null;
        path = null;
        chapterName = null;
        activeQuestionsNumber = 0;
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    public void setName(final String name) {
        chapterName = name;
    }

    public void setActive(boolean active)
    {
        Iterator iterator = questionList.iterator();
        while (iterator.hasNext()) {
            Question question = (Question) iterator.next();
            if (question.getPath().equals(path)) {
                question.active(active);
                break;
            }
        }
    }

    public boolean setQuestionState(final Path path, final QuestionState state) {
        Iterator iterator = questionList.iterator();
        Question question = null;
        while (iterator.hasNext()) {
            question = (Question) iterator.next();
            if (question.getPath().equals(path)) ;
            break;
        }

        if (question == null)
            return false;

        if (state.correct())
            correctAnsweredQuestions++;
        if (state.inCorrect())
            inCorrectAnsweredQuestions++;
        question.setState(state);

        return true;
    }

    public String getName() {
        return chapterName;
    }

    public Path getPath()
    {
        return path;
    }

    public int getActiveQuestionsNumber() {
        return activeQuestionsNumber;
    }

    public int getInCorrectAnsweredQuestions() {
        return inCorrectAnsweredQuestions;
    }

    public int getCorrectAnsweredQuestions() {
        return correctAnsweredQuestions;
    }

    public int getAnsweredQuestions()
    {
        return correctAnsweredQuestions + inCorrectAnsweredQuestions;
    }

    public Path addNewQuestion(final String questionText, final String answerText)
    {
        String name = Utils.getFreeFileName(path, "question");
        if (name != null) {
            Question question = new Question(path, name);
            if (question.getPath() == null)
                return null;
            question.setQuestionText(questionText);
            question.setAnswerText(answerText);
            questionList.add(question);
            activeQuestionsNumber++;
            return question.getPath();
        }
        return null;
    }

    public boolean removeQuestion(final Path path)
    {
        Iterator iterator = questionList.iterator();
        while (iterator.hasNext()) {
            Question question = (Question) iterator.next();
            if (question.getPath().equals(path))
            {
                if(question.getState().inCorrect())
                    inCorrectAnsweredQuestions--;
                if(question.getState().correct())
                    correctAnsweredQuestions--;
                iterator.remove();
                activeQuestionsNumber--;
                return true;
            }
        }
        return false;
    }

    public static Chapter readChapter(final Path path)
    {
        File dir = new File(path.toString());
        if(dir.exists() && dir.isDirectory())
        {
            Chapter newChapter = new Chapter();
            newChapter.path = path;
            newChapter.activeQuestionsNumber = 0;
            newChapter.questionList = new LinkedList<Question>();
            newChapter.chapterName = path.getFileName().toString();

            File [] files = dir.listFiles();
            for(File f : files)
            {
                if(f.getName().length() >= "question".length())
                    if(f.getName().substring(0, "question".length()).equals("question"))
                    {
                        Question newQuestion = Question.readQuestion(path);
                        newChapter.addQuestion(newQuestion);
                    }
            }
            return newChapter;
        }//if(dir.exists() && dir.isDirectory())
        return null;
    }

    private void addQuestion(final Question question)
    {
        if(!question.equals(null) && !question.getPath().equals(null))
        {
            question.active(true);
            questionList.add(question);
            activeQuestionsNumber++;
        }
    }

    private boolean createChapter(final Path path, final String name)
    {
        if (!Utils.ifContainsFile(path, name)) {
            chapterName = name;
            questionList = new LinkedList<Question>();
            activeQuestionsNumber = 0;
            File newDirectory = new File(path.toString() + File.separator + name);
            newDirectory.mkdir();
            return true;
        }
        return false;
    }
}
