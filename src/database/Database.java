package database;

import Utils.Utils;
import database.chapter.Chapter;
import database.chapter.question.Question;
import database.chapter.question.QuestionState;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * One static instance of Database is created.
 * Class implements working on set of Chapter objects.
 * Each chapter can store Question objects.
*/
public class Database {


    public static Database database;

    private Path path;
    private List chapterList;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;

    private Iterator  iterator;
    private Question lastReturnedQuestion;
    private Chapter lastReturnedChapter;

    /**
     * Creates or imports Database
     * @param initDirectory Path to directory, where Database folder is stored (READ mode),
     *                      or will be created (CREATE mode).
     * @param mode if object is set to OPEN mode - imports Databse from initDirectory
     *             if object is set to CREATE mode - creates Database directory with Path:
     *             initDirectory/databaseDir
     */
    public Database(final Path initDirectory, Mode mode) {
        database = null;
        chapterList = null;
        iterator = null;
        lastReturnedChapter = null;
        lastReturnedChapter = null;

        File file = new File(initDirectory.toString());
        if (mode.open()) {
            //read database from disk
            if (file.exists() && file.isDirectory()) {
                path = initDirectory;
                chapterList = new LinkedList<Chapter>();
                downloadChaptersFromDirectory(path);
                database = this;
            }
        } //if(mode.open())

        if (mode.create()) {
            //create new database
            if (file.isDirectory()) {
                String dirName = Utils.getFreeFileName(initDirectory, "LearningCardsDatabase");
                String absolutePath = initDirectory.toString() + File.separator + dirName;
                File databaseDirectory = new File(absolutePath);
                if (databaseDirectory.mkdir()) {
                    path = Paths.get(absolutePath);
                    chapterList = new LinkedList<Question>();
                    database = this;
                    activeQuestionsNumber = 0;
                    correctAnsweredQuestions = 0;
                    inCorrectAnsweredQuestions = 0;
                }
            }
        } //if(mode.create())

    } //Database(final Path initDirectory, Mode mode)

    /**
     * Adds new question to chapter from path: chapterPath.
     * @param chapterPath   path to chapter, where question should be added.
     * @param questionText  Text of new question.
     * @param answerText    Text of answer for new question.
     * @return  Return path of new question.
     */
    public Path addNewQuestion(final Path chapterPath, final String questionText, final String answerText) {
        Chapter chapter = getChapter(chapterPath);
        if (!chapter.equals(null))
        {
            Path newChapterPath = chapter.addNewQuestion(questionText, answerText);
            activeQuestionsNumber++;
            return newChapterPath;
        }
        return null;
    }

    /**
     * Adds new Chapter to database. If Chapter with chapterName exists, returns null
     * and not creating chapter. If adding new chapter was succesfull, returns Path to added chapter.
     * @param chapterName name of new Chapter
     * @return returns Path to new added chapter or null - if adding was not succesfull
     **/
    public Path addNewChapter(final String chapterName)
    {
        Path chapterPath = Paths.get(path.toString() + File.separator + chapterName);
        File dir = new File(chapterPath.toString());
        if(!dir.exists())
        {
            Chapter newChapter = new Chapter(path, chapterName);
            chapterList.add(newChapter);
            if(lastReturnedChapter != null)
            {
                int index = chapterList.indexOf(lastReturnedChapter) + 1;
                iterator = chapterList.listIterator(index);
            }
            return  chapterPath;
        }
        return null;
    }

    /**
     * Removes chapter with name: chapterName.
     * @param chapterPath name of Chapter that will be deleted
     */
    public void removeChapter(final Path chapterPath)
    {
        Iterator tmpIterator = chapterList.listIterator();
        while (tmpIterator.hasNext())
        {
            Chapter chapter  =  (Chapter) tmpIterator.next();
            if(chapter.getPath().equals(chapterPath))
            {
                activeQuestionsNumber -= chapter.getActiveQuestionsNumber();
                inCorrectAnsweredQuestions -= chapter.getInCorrectAnsweredQuestions();
                correctAnsweredQuestions -= chapter.getCorrectAnsweredQuestions();

                chapter.delete();
                tmpIterator.remove();

                if(lastReturnedChapter != null)
                {
                    int index = chapterList.indexOf(lastReturnedChapter) + 1;
                    iterator = chapterList.listIterator(index);
                }

                break;
            }
        }
    }

    /**
     *  Removes question from database
     * @param chapterPath Chapter of removing question.
     * @param questionPath Path of removing question.
     */

    public void removeQuestion(Path chapterPath, Path questionPath)
    {
        Iterator iterator = chapterList.iterator();
        while (iterator.hasNext())
        {
            Chapter chapter = (Chapter) iterator.next();
            if(chapter.getPath().equals(chapterPath))
            {
                if(chapter.isActive(questionPath))
                    activeQuestionsNumber--;
                QuestionState questionState = chapter.removeQuestion(questionPath);
                if(questionState != null)
                {
                    if(questionState.correct())
                        correctAnsweredQuestions--;
                    if(questionState.inCorrect())
                        inCorrectAnsweredQuestions--;
                }
                break;
            }
        }
    }

    /**
     * returns next Question from Database, that is active and wasn't answered.
     * Works in cycle. If every active question was returned, returns first active question (from beginning).
     * @return Returns active, not answered question.
     */
    public Question nextQuestion()
    {
        if(iterator == null)
        {
            iterator = chapterList.listIterator();
            if(!iterator.hasNext())
            {
                iterator = null;
                return null;
            }
        }

        if(getActiveQuestionsNumber() == 0 || getActiveQuestionsNumber() == getAnsweredQuestions())
        {
            iterator = null;
            return null;
        }

        if(lastReturnedChapter != null)
        {
            Question questionCandidate = lastReturnedChapter.nextQuestion();
            if(questionCandidate == null)
            {
                lastReturnedChapter.reset();
                if(iterator.hasNext())
                    lastReturnedChapter = (Chapter) iterator.next();
                else
                    lastReturnedChapter = null;
                return nextQuestion();
            }
            else
            {
                lastReturnedQuestion = questionCandidate;
                return questionCandidate;
            }
        }
        else
        {
            if (iterator.hasNext())
                lastReturnedChapter = (Chapter) iterator.next();
            else
            {
                iterator = null;
                lastReturnedChapter = null;
            }
            return nextQuestion();
        }
    }

    /**
     * @return  path of chapter from which last question was returned.
     */
    public Path getLastReturnedChapterPath()
    {
        if(lastReturnedChapter == null)
            return null;

        return lastReturnedChapter.getPath();
    }

    /**
     * @return Returns number of active questions.
     */
    public int getActiveQuestionsNumber()
    {
        return activeQuestionsNumber;
    }

    /**
     * @return Returns number of incorrect asnwered questions.
     */
    public int getInCorrectAnsweredQuestions()
    {
        return inCorrectAnsweredQuestions;
    }

    /**
     * @return Returns number of correct asnwered questions.
     */
    public int getCorrectAnsweredQuestions()
    {
        return correctAnsweredQuestions;
    }

    /**
     * @return Returns number of asnwered questions.
     */
    public int getAnsweredQuestions()
    {
        return correctAnsweredQuestions + inCorrectAnsweredQuestions;
    }

    /**
     * Sets state of specific question.
     * @param chapterPath path of chapter thah desired question
     * @param questionPath path of desired question
     * @param state state of question that will be set
    */
    public void setQuestionState(Path chapterPath, Path questionPath, QuestionState state)
    {
        Chapter chapter = getChapter(chapterPath);
        if(chapter != null)
            if(chapter.setQuestionState(questionPath, state))
            {
                if (state.correct())
                    correctAnsweredQuestions++;
                if (state.inCorrect())
                    inCorrectAnsweredQuestions++;
            }
    }

    /**
     * Sets active field of specific question
     * @param chapterPath path of chapter thah desired question
     * @param questionPath path of desired question
     * @param active new value of active field
     */
    public void active(Path chapterPath, Path questionPath,  boolean active)
    {
        Chapter chapter = getChapter(chapterPath);
        if(chapter != null)
            chapter.setActive(active, questionPath);
    }


    /**
     * Returns value of question acticve field. If guestion doesn't exist, returns false.
     * @param chapterPath path to chapter that includes question
     * @param questionPath path to specific question
     * @return returns true if question is active, otherwise returns false
     */
    public boolean isActive(Path chapterPath, Path questionPath)
    {
        Chapter chapter = getChapter(chapterPath);
        if(chapter != null)
            return chapter.isActive(questionPath);
        return false;
    }

    /**
     * Returns Question state object of specific question. If question doesn't exist, returns false.
     * @param chapterPath path to chapter that includes question
     * @param questionPath path to specific question
     * @return returns QuestionState object of specific question
     */
    public QuestionState qetQuestionState(Path chapterPath, Path questionPath)
    {
        Chapter chapter = getChapter(chapterPath);
        if(chapterPath != null)
            return chapter.getQuestionState(questionPath);
        return null;
    }

    public Path getLastReturnedQuestionPath()
    {
        if(lastReturnedQuestion == null)
            return null;
        return lastReturnedQuestion.getPath();
    }

    private Chapter getChapter(final Path chapterPath)
    {
        Iterator iterator = chapterList.iterator();
        Chapter chapter = null;
        while (iterator.hasNext())
        {
            chapter = (Chapter) iterator.next();
            if(chapter.getPath().equals(chapterPath))
                return chapter;
        }
        return null;
    }

    private void downloadChaptersFromDirectory(final Path dirPath)
    {
        File file = new File(dirPath.toString());
        if(file.exists() && file.isDirectory())
        {
            File [] files = file.listFiles();
            for(File f : files)
            {
                if(isChapterName(f.toPath().getFileName().toString()))
                {
                    Chapter newChapter = Chapter.readChapter(dirPath);
                    if(newChapter == null)
                        continue;
                    chapterList.add(newChapter);
                    activeQuestionsNumber += newChapter.getActiveQuestionsNumber();
                    correctAnsweredQuestions += newChapter.getCorrectAnsweredQuestions();
                    inCorrectAnsweredQuestions += newChapter.getInCorrectAnsweredQuestions();
                } //if(isChapterName(f.toPath().getFileName().toString()))
            } //for
        } // if(file.exists() && file.isDirectory())
    }

    private boolean isChapterName(final String name)
    {
        return name.matches("^chapter");
    }
}
