package database.chapter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Utils.Utils;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;

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

      public Question nextQuestion()
            Returns next active question. If there are no active questions left, returns null.

      public void reset()

      public void setName(final String name)
      public void setActive(boolean active,final  Path path)
      public boolean setQuestionState(final Path path,final QuestionState state)

      public String getName()
      public Path getPath()
      public List getQuestions()
      public int getActiveQuestionsNumber()
      public int getInCorrectAnsweredQuestions()
      public int getCorrectAnsweredQuestions()
      public int getAnsweredQuestions()
    */
    private List<Question> questionList;
    private String chapterName;
    private Path path;
    private Iterator iterator;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;
    private Question lastReturnedQuestion;


    public Chapter(final Path path, final String name) {
        createChapter(path, name);
    }

    private Chapter() {
        lastReturnedQuestion = null;
        iterator = null;
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

    public void setActive(boolean active, final Path path) {
        Iterator iterator = questionList.iterator();
        while (iterator.hasNext()) {
            Question question = (Question) iterator.next();
            if (question.getPath().equals(path)) {
                question.active(active);
                break;
            }
        }
    }

    public boolean isActive(Path questionPath)
    {
        Iterator iterator = questionList.iterator();
        while (iterator.hasNext())
        {
            Question question = (Question) iterator.next();
            if (question.getPath().equals(questionPath))
            {
                return question.isActive();
            }
        }
        return false;
    }

    public boolean setQuestionState(final Path path, final QuestionState state) {
        Iterator iterator = questionList.iterator();
        Question question = null;
        QuestionState oldState = null;
        while (iterator.hasNext()) {
            question = (Question) iterator.next();
            if (question.getPath().equals(path))
            {
                oldState = question.getState();
                question.setState(state);
                break;
            }
        }

        if (oldState == null)
            return false;

        if(!oldState.equals(state))
        {
            if (state.correct())
                correctAnsweredQuestions++;
            if (state.inCorrect())
                inCorrectAnsweredQuestions++;
            question.setState(state);

            return true;
        }

        return false;
    }

    public String getName() {
        return chapterName;
    }

    public Path getPath() {
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

    public int getAnsweredQuestions() {
        return correctAnsweredQuestions + inCorrectAnsweredQuestions;
    }

    public QuestionState getQuestionState(Path questionPath)
    {
        Iterator iterator = questionList.iterator();
        while (iterator.hasNext())
        {
            Question question = (Question) iterator.next();
            if (question.getPath().equals(path))
                return question.getState();
        }
        return null;
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
            if(lastReturnedQuestion != null)
            {
                int index = questionList.indexOf(lastReturnedQuestion) + 1;
                iterator = questionList.listIterator(index);
            }
            return question.getPath();
        }
        return null;
    }

    public QuestionState  removeQuestion(final Path path)
    {
        Iterator tmpIterator = questionList.iterator();
        QuestionState questionState = null;
        while (tmpIterator.hasNext()) {
            Question question = (Question) tmpIterator.next();
            if (question.getPath().equals(path))
            {
                if (question.getState().inCorrect())
                    inCorrectAnsweredQuestions--;
                if (question.getState().correct())
                    correctAnsweredQuestions--;
                if(question.isActive())
                    activeQuestionsNumber--;

                questionState = question.getState();
                question.removeQuestion();
                tmpIterator.remove();

                if(lastReturnedQuestion != null)
                {
                    int index = questionList.indexOf(lastReturnedQuestion) + 1;
                    iterator = questionList.listIterator(index);
                }

                return questionState;
            }
        }
        return null;
    }

    public Question nextQuestion() {
        Question question = null;
        if (iterator == null)
            iterator = questionList.listIterator();
        while (iterator.hasNext()) {
            question = (Question) iterator.next();
            if (question.isActive() && !question.getState().correct()
                    && !question.getState().inCorrect())
            {
                lastReturnedQuestion = question;
                return question;
            }
        }
        return null;
    }

    public void reset()
    {
        iterator = null;
    }

    public void delete()
    {
        Iterator iterator = questionList.listIterator();
        while (iterator.hasNext())
        {
            Question question = (Question) iterator.next();
            question.removeQuestion();
            iterator.remove();
        }

        File file = new File(path.toString());
        if(file.exists())
            file.delete();
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
                        Path questionFilePath = Paths.get(path.toString() + File.separator + f.getName());
                        Question newQuestion = Question.readQuestion(questionFilePath);
                        newChapter.addQuestion(newQuestion);
                    }
            }
            return newChapter;
        }//if(dir.exists() && dir.isDirectory())
        return null;
    }

    private void addQuestion(final Question question)
    {
        if(question != null && !question.getPath().equals(null))
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
            this.path = Paths.get(path.toString() + File.separator + name);
            File newDirectory = new File(this.path.toString());
            newDirectory.mkdir();
            return true;
        }
        return false;
    }
}
