package database.chapter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Utils.Utils;
import database.LastSerachedBy;
import database.chapter.question.Question;
import database.chapter.question.QuestionRepresentation;
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
    private ListIterator iterator;
    private LastSerachedBy lastSerachedBy;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;
    private Question lastReturnedQuestion;


    public Chapter(final Path path, final String name) {
        createChapter(path, name);
    }

    private Chapter()
    {
        iterator = questionList.listIterator();
        lastSerachedBy = null;
        lastReturnedQuestion = null;
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
                QuestionState state = question.getState();
                if(active == false && question.isActive())
                {
                    if (state.inCorrect())
                        inCorrectAnsweredQuestions--;
                    if (state.correct())
                        correctAnsweredQuestions--;
                }
                question.active(active);
                break;
            }
        }
    }

    public void setAllQuestionsActive()
    {
        Iterator iterator = questionList.listIterator();
        Question question = null;
        while (iterator.hasNext())
        {
            question = (Question) iterator.next();
            question.active(true);
        }
        activeQuestionsNumber = questionList.size();
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    public void setAllQuestionsNotActive()
    {
        Iterator iterator = questionList.listIterator();
        Question question = null;
        while (iterator.hasNext())
        {
            question = (Question) iterator.next();
            question.active(false);
        }
        activeQuestionsNumber = 0;
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    public void resetAnswers()
    {
        for(Question question: questionList)
            question.setState(QuestionState.NO_ANSWER);
        correctAnsweredQuestions = 0;
        inCorrectAnsweredQuestions = 0;
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
            if(oldState.correct())
                correctAnsweredQuestions--;
            if(oldState.inCorrect())
                inCorrectAnsweredQuestions--;
            return true;
        }

        return false;
    }

    public void setLastSerachedBy(LastSerachedBy lsb)
    {
        lastSerachedBy = lsb;
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

    public LastSerachedBy lastSerachedBy()
    {
        return lastSerachedBy;
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
                if(lastSerachedBy.prev())
                    iterator.previous();

            }
            return question.getPath();
        }
        return null;
    }

    public QuestionState  removeQuestion(final Path path)
    {
        Iterator tmpIterator = questionList.iterator();
        int removeElementIndex = 0;
        int lastReturnedElementIndex = questionList.indexOf(lastReturnedQuestion);
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
                setIteratorOnPosition(lastReturnedElementIndex, removeElementIndex);

                return questionState;
            }
            removeElementIndex++;
        }
        return null;
    }

    public LinkedList getQuestionListRepresentation()
    {
        LinkedList<QuestionRepresentation> list =  new LinkedList<QuestionRepresentation>();
        for(Question question : questionList)
        {
            QuestionRepresentation qRep = question.getRepresentation();
            qRep.setChapterPath(path);
            list.add(qRep);
        }
        return list;
    }

    public QuestionRepresentation getQuestionRepresentation(Path questionPath)
    {
        Question question = getQuestion(questionPath);
        if(question == null)
            return null;
        QuestionRepresentation questionRepresentation = question.getRepresentation();
        questionRepresentation.setChapterPath(path);
        return questionRepresentation;
    }

    public ChapterRepresentation getRepresentation()
    {
        return new ChapterRepresentation(path, chapterName, correctAnsweredQuestions, inCorrectAnsweredQuestions, activeQuestionsNumber);
    }

    public boolean updateQuestion(Path questionPath, String questionText, String answerText)
    {
        Question question = getQuestion(questionPath);
        if(question != null)
            return question.update(questionText, answerText);
        return false;
    }

    private void setIteratorOnPosition(int lastReturnedElementIndex, int removeElementIndex)
    {
        if(questionList.isEmpty())
            iterator = null;
        else if(lastReturnedQuestion != null)
        {
            if(lastReturnedElementIndex != removeElementIndex)
            {
                Path tmp = lastReturnedQuestion.getPath();

                iterator = questionList.listIterator();
                if(lastSerachedBy.prev())
                    do{
                        previousQuestion();
                    }while (!lastReturnedQuestion.getPath().equals(tmp));

                if(lastSerachedBy.next())
                    do{
                        nextQuestion();
                    }while (!lastReturnedQuestion.getPath().equals(tmp));
            }
            else if(lastReturnedElementIndex == removeElementIndex)
            {
                iterator = questionList.listIterator();
                if(lastSerachedBy.next())
                    while (lastReturnedElementIndex > 0)
                    {
                        lastReturnedElementIndex--;
                        nextQuestion();
                    }
                if (lastSerachedBy.prev())
                {
                    if(lastSerachedBy.next())
                        while (lastReturnedElementIndex > 0)
                        {
                            lastReturnedElementIndex--;
                            nextQuestion();
                        }
                        previousQuestion();
                }

            }
        }
    }

    public Question nextQuestion()
    {
        Question returnQuestion = null;
        if(iterator == null)
            iterator = questionList.listIterator();
        if(lastSerachedBy == null)
            lastSerachedBy = LastSerachedBy.NEXT;

        if(lastSerachedBy.next())
        {
            if(iterator.hasNext())
                returnQuestion = (Question) iterator.next();
            else
            {
                iterator = questionList.listIterator();
                returnQuestion = (Question) iterator.next();
            }
        }
        else if(lastSerachedBy.prev())
        {
            if(iterator.hasNext())
                iterator.next();
            else
            {
                iterator = questionList.listIterator();
                iterator.next();
            }

            if(iterator.hasNext())
                returnQuestion = (Question) iterator.next();
            else
            {
                iterator = questionList.listIterator();
                returnQuestion = (Question) iterator.next();
            }
        }

        lastSerachedBy = LastSerachedBy.NEXT;
        lastReturnedQuestion = returnQuestion;
        return returnQuestion;
    }

    public Question previousQuestion()
    {
        Question returnQuestion = null;
        if(iterator == null)
            iterator = questionList.listIterator();
        if(lastSerachedBy == null)
            lastSerachedBy = LastSerachedBy.PREV;


        if(lastSerachedBy.next())
        {
            if(iterator.hasPrevious())
                iterator.previous();
            else
            {
                setIteratorAtLastPosition();
                iterator.previous();
            }

            if(iterator.hasPrevious())
                returnQuestion = (Question) iterator.previous();
            else
            {
                setIteratorAtLastPosition();
                returnQuestion = (Question) iterator.previous();
            }
        }
        else if(lastSerachedBy.prev())
        {
            if(iterator.hasPrevious())
                returnQuestion = (Question) iterator.previous();
            else
            {
                setIteratorAtLastPosition();
                returnQuestion = (Question) iterator.previous();
            }
        }

        lastSerachedBy = LastSerachedBy.PREV;
        lastReturnedQuestion = returnQuestion;
        return returnQuestion;
    }

    public boolean hasNextQuestion()
    {
        if(iterator == null)
            return true;
        if(lastSerachedBy == null)
            return iterator.hasNext();

        if(lastSerachedBy.next())
            return iterator.hasNext();
        else
        {
            if(iterator.hasNext())
                iterator.next();
            else
                return false;

            if(iterator.hasNext())
            {
                iterator.previous();
                return true;
            }
            else
            {
                iterator.previous();
                return false;
            }
        }
    }

    public boolean hasPrevQuestion()
    {
        if(iterator == null)
            return true;
        if(lastSerachedBy == null)
            return iterator.hasPrevious();

        if(lastSerachedBy.next())
        {
            if(iterator.hasPrevious())
                iterator.previous();
            else
                return false;

            if(iterator.hasPrevious())
            {
                iterator.next();
                return true;
            }
            else
            {
                iterator.next();
                return false;
            }
        }
        else
            return iterator.hasPrevious();
    }

    public void setIteratorAtZeroPosition()
    {
        iterator = questionList.listIterator();
    }

    public void setIteratorAtLastPosition()
    {
        iterator = questionList.listIterator();
        while(iterator.hasNext())
            iterator.next();
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

    private Question getQuestion(Path questionPath)
    {
        for(Question question : questionList)
        {
            if(question.getPath().equals(questionPath))
                return question;
        }
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
