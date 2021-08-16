package database.chapter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import Utils.Utils;
import database.LastSerachedBy;
import database.chapter.question.Question;
import database.chapter.question.QuestionRepresentation;
import database.chapter.question.QuestionState;

public class Chapter
{
    private List<Question> questionList;
    private String chapterName;
    private Path path;
    private ListIterator<Question> iterator;
    private LastSerachedBy lastSerachedBy;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;
    private Question lastReturnedQuestion;

    /**
     * Class contructor.
     * @param path  directory of new chapter
     * @param name of new chapter, if (path)directory already includes directory with given name,
     *             chapter will not be created.
     * */
    public Chapter(final Path path, final String name)
    {
        createChapter(path, name);
    }

    /**
     * Default contructor of chapter.
     * */
    private Chapter()
    {
        questionList = new LinkedList<Question>();
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

    /**
     * Sets name of chapter
     * @param  name name of chapter
     * */
    public void setName(final String name) {
        chapterName = name;
    }

    /**
     * Sets active state of question with given path. If question doesn't exist, does nothing.
     * @param active active state of question
     * @param path path of question
     * */
    public boolean setActive(boolean active, final Path path)
    {

        for(Question question : questionList)
        {
            if (question.getPath().equals(path))
            {
                boolean isActive = question.isActive();
                QuestionState state = question.getState();
                if(!active && question.isActive())
                {
                    if (state.inCorrect())
                        inCorrectAnsweredQuestions--;
                    if (state.correct())
                        correctAnsweredQuestions--;
                }
                question.active(active);
                if(isActive  && !active)
                {
                    activeQuestionsNumber--;
                    return true;
                }

                if(!isActive  && active)
                {
                    activeQuestionsNumber++;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Set's all active state's of chapter question's on true.
     * */
    public void setAllQuestionsActive()
    {
        for(Question question : questionList)
            question.active(true);

        activeQuestionsNumber = questionList.size();
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    /**
     * Set's all active state's of chapter question's on false.
     * */
    public void setAllQuestionsNotActive()
    {
        for(Question question : questionList)
            question.active(false);

        activeQuestionsNumber = 0;
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    /**
     * Set's all state's of chapter question's on NO_ANSWER.
     * */
    public void resetAnswers()
    {
        for(Question question: questionList)
            question.setState(QuestionState.NO_ANSWER);
        correctAnsweredQuestions = 0;
        inCorrectAnsweredQuestions = 0;
    }

    /**
     * Returns true if question with given path is active. If question
     * doesn't exists or is not active, returns false.
     * @param questionPath path of question
     * */
    public boolean isActive(Path questionPath)
    {
        for(Question question : questionList)
            if (question.getPath().equals(questionPath))
                return question.isActive();
        return false;
    }

    /**
     * Sets question state of given question.
     * @param path of question
     * @param state state of question
     * */
    public boolean setQuestionState(final Path path, final QuestionState state)
    {
        QuestionState oldState = null;
        for(Question question : questionList)
        {
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

    /**
     * Sets lastSearchdBy object.
     * If chapter was last searchd by next() function, lsb should be set on LastSearchdBy.NEXT.
     * If chapter was last searchd by prev() function, lsb should be set on LastSearchdBy.PREV.
     * */
    public void setLastSerachedBy(LastSerachedBy lsb)
    {
        lastSerachedBy = lsb;
    }

    /**
     * Returns name of chapter.
     * */
    public String getName() {
        return chapterName;
    }

    /**
     * Returns path of chapter.
     * */
    public Path getPath() {
        return path;
    }

    /**
     * Returns number of active questions.
     * */
    public int getActiveQuestionsNumber() {
        return activeQuestionsNumber;
    }

    /**
     * Returns number of incorrect answered questions.
     * */
    public int getInCorrectAnsweredQuestions() {
        return inCorrectAnsweredQuestions;
    }

    /**
     * Returns number of correct answered questions.
     * */
    public int getCorrectAnsweredQuestions() {
        return correctAnsweredQuestions;
    }

    /**
     * Returns number of answered questions.
     * */
    public int getAnsweredQuestions() {
        return correctAnsweredQuestions + inCorrectAnsweredQuestions;
    }

    /**
     * Returns state of given question.
     * @param questionPath path of question
     * */
    public QuestionState getQuestionState(Path questionPath)
    {
        for(Question question : questionList)
        {
            if (question.getPath().equals(questionPath))
                return question.getState();
        }
        return null;
    }

    /**
     * Returns instance of LastSearchdBy class.
     * */
    public LastSerachedBy lastSerachedBy()
    {
        return lastSerachedBy;
    }

    /**
     * Adds new question to chapter.
     * @param questionText text of new question
     * @param answerText text of new answer
     * */
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

    /**
     * Removes question from chapter.
     * @param path path of question
     * @return Returns state of removed question. Returns null if question doesn't exist.
     * */
    public QuestionState  removeQuestion(final Path path)
    {
        int removeElementIndex = 0;
        int lastReturnedElementIndex = questionList.indexOf(lastReturnedQuestion);
        QuestionState questionState = null;
        for(Question question : questionList)
        {
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
                questionList.remove(question);
                setIteratorOnPosition(lastReturnedElementIndex, removeElementIndex);

                return questionState;
            }
            removeElementIndex++;
        }
        return null;
    }

    /**
     * Returns LinkedList representation of chapter
     * @return LinkedList<QuestionRepresentation>
     * */
    public LinkedList<QuestionRepresentation> getQuestionListRepresentation()
    {
        LinkedList<QuestionRepresentation> list =  new LinkedList<>();
        for(Question question : questionList)
        {
            QuestionRepresentation qRep = question.getRepresentation();
            qRep.setChapterPath(path);
            list.add(qRep);
        }
        return list;
    }

    /**
     * Returns representation of given question.
     * @return QuestionRepresentation instance.
     * */
    public QuestionRepresentation getQuestionRepresentation(Path questionPath)
    {
        Question question = getQuestion(questionPath);
        if(question == null)
            return null;
        QuestionRepresentation questionRepresentation = question.getRepresentation();
        questionRepresentation.setChapterPath(path);
        return questionRepresentation;
    }

    /**
     * Returns representation of chapter.
     * @return instance of ChapterRepresentation class
     * */
    public ChapterRepresentation getRepresentation()
    {
        return new ChapterRepresentation(path, chapterName, correctAnsweredQuestions, inCorrectAnsweredQuestions, activeQuestionsNumber);
    }

    /**
     * Updates given question. If question doesn't exist, does nothing.
     * @param questionPath path of question
     * @param questionText text of question
     * @param answerText text of answer
     * */
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

    /**
     * Returns next question from chapter. Returns questions periodically.
     * for example:
     * chapter:
     *      question1
     *      question2
     *      question3
     * Next calls of nextQuestion() function will return: question1, question2, question3, question1, ...
     * */
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
                returnQuestion = iterator.next();
            else
            {
                iterator = questionList.listIterator();
                returnQuestion = iterator.next();
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
                returnQuestion = iterator.next();
            else
            {
                iterator = questionList.listIterator();
                returnQuestion = iterator.next();
            }
        }

        lastSerachedBy = LastSerachedBy.NEXT;
        lastReturnedQuestion = returnQuestion;
        return returnQuestion;
    }

    /**
     * Returns previous question from chapter. Returns questions periodically.
     * for example:
     * chapter:
     *      question1
     *      question2
     *      question3
     * Next calls of previousQuestion() function will return: question3, question2, question1, question3, ...
     **/
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
                returnQuestion = iterator.previous();
            else
            {
                setIteratorAtLastPosition();
                returnQuestion = iterator.previous();
            }
        }
        else if(lastSerachedBy.prev())
        {
            if(iterator.hasPrevious())
                returnQuestion = iterator.previous();
            else
            {
                setIteratorAtLastPosition();
                returnQuestion = iterator.previous();
            }
        }

        lastSerachedBy = LastSerachedBy.PREV;
        lastReturnedQuestion = returnQuestion;
        return returnQuestion;
    }

    /**
     * Returns true if nextQuestion() or previousQuestion() returned (in last call) last question
     * in chapter.
     * */
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

    /**
     * Returns true if nextQuestion() or previousQuestion() returned (in last call) first question
     * in chapter.
     * */
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

    /**
     * Sets chapter Iterator on first position.
     * */
     public void setIteratorAtZeroPosition()
    {
        iterator = questionList.listIterator();
    }

    /**
     * Sets chapter Iterator on last position.
     * */
    public void setIteratorAtLastPosition()
    {
        iterator = questionList.listIterator();
        while(iterator.hasNext())
            iterator.next();
    }

    /**
     * Removes chapter.
     * */
    public void delete()
    {
        for(Question question : questionList)
        {
            question.removeQuestion();
            iterator.remove();
        }

        File file = new File(path.toString());
        if(file.exists())
            file.delete();
    }

    /**
     * Returns instance of Chapter class that responds to chapter from given path.
     * If given directory doesn't exist returns null.
     * @param path path of chapter
     * @return isntance of Chapter class
     * */
    public static Chapter readChapter(final Path path)
    {
        File dir = new File(path.toString());
        if(dir.exists() && dir.isDirectory())
        {
            Chapter newChapter = new Chapter();
            newChapter.path = path;
            newChapter.activeQuestionsNumber = 0;
            newChapter.questionList = new LinkedList<>();
            newChapter.chapterName = path.getFileName().toString();

            File [] files = dir.listFiles();
            for(File f : files)
            {
                if(f.getName().length() >= "question".length())
                    if(f.getName().substring(0, "question".length()).equals("question"))
                    {
                        Path questionFilePath = Paths.get(path + File.separator + f.getName());
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
            questionList = new LinkedList<>();
            activeQuestionsNumber = 0;
            this.path = Paths.get(path + File.separator + name);
            File newDirectory = new File(this.path.toString());
            newDirectory.mkdir();
            return true;
        }
        return false;
    }
}
