package database;

import Utils.Utils;
import database.chapter.Chapter;
import database.chapter.ChapterRepresentation;
import database.chapter.question.Question;
import database.chapter.question.QuestionRepresentation;
import database.chapter.question.QuestionState;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * One static instance of Database is created.
 * Class implements working on set of Chapter objects.
 * Each chapter can store Question objects.
*/
public class Database {


    public static Database database;

    private Path path;
    private List<Chapter> chapterList;

    private int activeQuestionsNumber;
    private int correctAnsweredQuestions;
    private int inCorrectAnsweredQuestions;

    private ListIterator<Chapter> iterator;
    private LastSerachedBy lastSerachedBy;
    private Question lastReturnedQuestion;
    private Chapter lastReturnedChapter;

    /**
     * Creates or imports Database
     *
     * @param initDirectory Path to directory, where Database folder is stored (READ mode),
     *                      or will be created (CREATE mode).
     * @param mode          if object is set to OPEN mode - imports Databse from initDirectory
     *                      if object is set to CREATE mode - creates Database directory with Path:
     *                      initDirectory/databaseDir
     */
    public Database(final Path initDirectory, Mode mode) {
        database = null;
        chapterList = null;
        iterator = null;
        lastSerachedBy = null;
        lastReturnedChapter = null;
        lastReturnedChapter = null;

        File file = new File(initDirectory.toString());
        if (mode.open()) {
            //read database from disk
            if (file.exists() && file.isDirectory()) {
                path = initDirectory;
                chapterList = new LinkedList<>();
                downloadChaptersFromDirectory(path);
                database = this;
            }
        } //if(mode.open())

        if (mode.create())
        {
            //create new database
            if (file.isDirectory())
            {
                String dirName = Utils.getFreeFileName(initDirectory, "LearningCardsDatabase");
                String absolutePath = initDirectory + File.separator + dirName;
                File databaseDirectory = new File(absolutePath);
                if (databaseDirectory.mkdir()) {
                    path = Paths.get(absolutePath);
                    chapterList = new LinkedList<>();
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
     *
     * @param chapterPath  path to chapter, where question should be added.
     * @param questionText Text of new question.
     * @param answerText   Text of answer for new question.
     * @return Return path of new question.
     */
    public Path addNewQuestion(final Path chapterPath, final String questionText, final String answerText) {
        Chapter chapter = getChapter(chapterPath);
        if (chapter != null)
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
     *
     * @param chapterName name of new Chapter
     * @return returns Path to new added chapter or null - if adding was not succesfull
     **/
    public Path addNewChapter(final String chapterName) {
        Path chapterPath = Paths.get(path.toString() + File.separator + chapterName);
        File dir = new File(chapterPath.toString());
        if (!dir.exists()) {
            Chapter newChapter = new Chapter(path, chapterName);
            chapterList.add(newChapter);
            if (lastReturnedChapter != null) {
                int index = chapterList.indexOf(lastReturnedChapter) + 1;
                iterator = chapterList.listIterator(index);
            }
            return chapterPath;
        }
        return null;
    }

    /**
     * Removes chapter with name: chapterName.
     *
     * @param chapterPath name of Chapter that will be deleted
     */
    public void removeChapter(final Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if (chapter.getPath().equals(chapterPath))
            {
                activeQuestionsNumber -= chapter.getActiveQuestionsNumber();
                inCorrectAnsweredQuestions -= chapter.getInCorrectAnsweredQuestions();
                correctAnsweredQuestions -= chapter.getCorrectAnsweredQuestions();
                chapter.delete();

                if (lastReturnedChapter != null) {
                    int index = chapterList.indexOf(lastReturnedChapter) + 1;
                    iterator = chapterList.listIterator(index);
                }

                break;
            }
        }
    }

    /**
     * Removes question from database
     *
     * @param chapterPath  Chapter of removing question.
     * @param questionPath Path of removing question.
     */

    public void removeQuestion(Path chapterPath, Path questionPath)
    {
        for(Chapter chapter : chapterList)
        {
            if (chapter.getPath().equals(chapterPath)) {
                if (chapter.isActive(questionPath))
                    activeQuestionsNumber--;
                QuestionState questionState = chapter.removeQuestion(questionPath);
                if (questionState != null) {
                    if (questionState.correct())
                        correctAnsweredQuestions--;
                    if (questionState.inCorrect())
                        inCorrectAnsweredQuestions--;
                }
                break;
            }
        }
    }

    /**
     * returns next Question from Database, that is active and wasn't answered.
     * Works in cycle. If every active question was returned, returns first active question (from beginning).
     *
     * @return Returns active, not answered question.
     */
    public Question nextQuestion() {
        if (getActiveQuestionsNumber() == getAnsweredQuestions())
            return null;

        if (lastReturnedChapter == null)
            lastReturnedChapter = getNextActiveChapter();

        Question questionCandidate = getNextActiveQuestionFromChapter();

        if (questionCandidate == null) {
            lastReturnedChapter = getNextActiveChapter();
            lastReturnedChapter.setIteratorAtZeroPosition();
            lastReturnedChapter.setLastSerachedBy(LastSerachedBy.NEXT);
            return nextQuestion();
        } else {
            lastReturnedQuestion = questionCandidate;
            return questionCandidate;
        }
    }

    /**
     * returns next Question from Database, that is active and wasn't answered.
     * Works in cycle. If every active question was returned, returns first active question (from beginning).
     * Starts iterating set in opposite direction to nextQuestion()
     *
     * @return Returns active, not answered question.
     */
    public Question previousQuestion() {
        if (getActiveQuestionsNumber() == getAnsweredQuestions())
            return null;

        if (lastReturnedChapter == null)
            lastReturnedChapter = getPreviousActiveChapter();

        Question questionCandidate = getPreviousActiveQuestionFromChapter();

        if (questionCandidate == null) {
            lastReturnedChapter = getPreviousActiveChapter();
            lastReturnedChapter.setIteratorAtLastPosition();
            lastReturnedChapter.setLastSerachedBy(LastSerachedBy.PREV);

            return previousQuestion();
        } else {
            lastReturnedQuestion = questionCandidate;
            return questionCandidate;
        }
    }

    private Question getNextActiveQuestionFromChapter() {
        Question questionCandidate;
        while (lastReturnedChapter.hasNextQuestion()) {
            questionCandidate = lastReturnedChapter.nextQuestion();
            if (questionCandidate.isActive() && questionCandidate.getState().noAnswer())
                return questionCandidate;
        }
        return null;
    }

    private Question getPreviousActiveQuestionFromChapter() {
        Question questionCandidate;
        while (lastReturnedChapter.hasPrevQuestion()) {
            questionCandidate = lastReturnedChapter.previousQuestion();
            if (questionCandidate.isActive() && questionCandidate.getState().noAnswer())
                return questionCandidate;
        }
        return null;
    }

    private Chapter getNextActiveChapter() {
        while (true) {
            Chapter chapterCandidate = getNextChapter();
            if (chapterCandidate.getAnsweredQuestions() < chapterCandidate.getActiveQuestionsNumber()) {
                lastSerachedBy = LastSerachedBy.NEXT;
                return chapterCandidate;
            }
        }
    }

    private Chapter getPreviousActiveChapter() {
        while (true) {
            Chapter chapterCandidate = getPreviousChapter();
            if (chapterCandidate.getAnsweredQuestions() < chapterCandidate.getActiveQuestionsNumber()) {
                lastSerachedBy = LastSerachedBy.PREV;
                return chapterCandidate;
            }

        }
    }

    private Chapter getNextChapter() {
        if (iterator == null)
            iterator = chapterList.listIterator();

        if (lastSerachedBy == null)
            lastSerachedBy = LastSerachedBy.NEXT;

        if (lastSerachedBy.prev())
        {
            if (iterator.hasNext())
                iterator.next();
            else {
                iterator = chapterList.listIterator();
                iterator.next();
            }
        }

        Chapter returnChapter;

        if (iterator.hasNext())
            returnChapter = iterator.next();
        else {
            iterator = chapterList.listIterator();
            returnChapter = iterator.next();
        }

        lastSerachedBy = LastSerachedBy.NEXT;
        return returnChapter;
    }

    private Chapter getPreviousChapter() {
        if (iterator == null)
            iterator = chapterList.listIterator();


        if (lastSerachedBy == null)
            lastSerachedBy = LastSerachedBy.PREV;

        if (lastSerachedBy.next()) {
            if (iterator.hasPrevious())
                iterator.previous();
            else {
                while (iterator.hasNext())
                    iterator.next();
                iterator.previous();
            }
        }

        Chapter returnChapter;

        if (iterator.hasPrevious())
            returnChapter = iterator.previous();
        else {
            while (iterator.hasNext())
                iterator.next();
            returnChapter = iterator.previous();
        }

        lastSerachedBy = LastSerachedBy.PREV;
        return returnChapter;
    }

    /**
     * Returns path of chapter from which last question was returned.
     * @return instance of Path class
     */
    public Path getLastReturnedChapterPath()
    {
        if (lastReturnedChapter == null)
            return null;

        return lastReturnedChapter.getPath();
    }

    /**
     * Returns number of active questions.
     * @return number of active questions (int)
     */
    public int getActiveQuestionsNumber() {
        return activeQuestionsNumber;
    }

    /**
     * Returns number of incorrect asnwered questions.
     * @return number of incorrect asnwered questions(int).
     */
    public int getInCorrectAnsweredQuestions() {
        return inCorrectAnsweredQuestions;
    }

    /**
     * Returns number of correct asnwered questions.
     * @return number of correct asnwered questions (int).
     */
    public int getCorrectAnsweredQuestions() {
        return correctAnsweredQuestions;
    }

    /**
     * Returns number of asnwered questions.
     * @return number of asnwered questions (int).
     */
    public int getAnsweredQuestions() {
        return correctAnsweredQuestions + inCorrectAnsweredQuestions;
    }

    /**
     * Sets state of specific question.
     *
     * @param chapterPath  path of chapter where desired question belongs
     * @param questionPath path of desired question
     * @param state        state of question that will be set
     */
    public void setQuestionState(Path chapterPath, Path questionPath, QuestionState state) {
        Chapter chapter = getChapter(chapterPath);
        if (chapter != null)
        {
            int correctAnsw = chapter.getCorrectAnsweredQuestions();
            int incorrectAnsw = chapter.getInCorrectAnsweredQuestions();
            if (chapter.setQuestionState(questionPath, state))
            {
                correctAnsw = correctAnsw - chapter.getCorrectAnsweredQuestions();
                incorrectAnsw = incorrectAnsw - chapter.getInCorrectAnsweredQuestions();
                correctAnsweredQuestions = correctAnsweredQuestions - correctAnsw;
                inCorrectAnsweredQuestions = inCorrectAnsweredQuestions - incorrectAnsw;
            }
        }
    }

    /**
     * Sets active field of specific question
     *
     * @param chapterPath  path of chapter thah desired question
     * @param questionPath path of desired question
     * @param active       new value of active field
     */
    public void active(Path chapterPath, Path questionPath, boolean active)
    {
        Chapter chapter = getChapter(chapterPath);
        if (chapter != null)
        {
            int inCorrect = chapter.getInCorrectAnsweredQuestions();
            int correct = chapter.getCorrectAnsweredQuestions();

            if (chapter.setActive(active, questionPath))
            {
                if (active)
                    activeQuestionsNumber++;
                else
                    activeQuestionsNumber--;
            }

            inCorrect = chapter.getInCorrectAnsweredQuestions() - inCorrect;
            correct = chapter.getCorrectAnsweredQuestions() - correct;

            inCorrectAnsweredQuestions += inCorrect;
            correctAnsweredQuestions += correct;

        }
    }

    /**
     * Returns value of question acticve field. If guestion doesn't exist, returns false.
     *
     * @param chapterPath  path to chapter that includes question
     * @param questionPath path to specific question
     * @return returns true if question is active, otherwise returns false
     */
    public boolean isActive(Path chapterPath, Path questionPath) {
        Chapter chapter = getChapter(chapterPath);
        if (chapter != null)
            return chapter.isActive(questionPath);
        return false;
    }

    /**
     * Returns Question state object of specific question. If question doesn't exist, returns false.
     * @param chapterPath  path to chapter that includes question
     * @param questionPath path to specific question
     * @return returns QuestionState object of specific question
     */
    public QuestionState qetQuestionState(Path chapterPath, Path questionPath)
    {
        Chapter chapter = getChapter(chapterPath);
        if (chapterPath != null)
            return chapter.getQuestionState(questionPath);
        return null;
    }

    /**
     * Returns last returned question.
     * @return instance of Question class
     */
    public Question getLastRetunedQuestion()
    {
        return lastReturnedQuestion;
    }


    /**
     * Returns list of ChapterRepresentation instance's.
     * List corresponds to chapters included in database.
     * @return instance of LinkedList<ChapterRepresentation>
     * */
    public LinkedList<ChapterRepresentation> getChapterListRepresentation()
    {
        LinkedList<ChapterRepresentation> list = new LinkedList<>();
        for (Chapter chapter : chapterList)
        {
            list.add(new ChapterRepresentation(chapter.getPath(), chapter.getName()
                    , chapter.getCorrectAnsweredQuestions(), chapter.getInCorrectAnsweredQuestions(),
                    chapter.getActiveQuestionsNumber()));
        }
        return list;
    }

    /**
     * Returns list of QuestionRepresentation instance's.
     * List corresponds to chapter specified by chapterPath.
     * If chapter doesn't exit, returns null.
     * @return instance of LinkedList<QuestionRepresentation>
     * */
    public LinkedList<QuestionRepresentation> getQuestionListRepresentation(Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
                return chapter.getQuestionListRepresentation();
        }
        return null;
    }

    /**
     * Returns ChapterRepresentation instance that corresponds to speciffied chapter.
     * If chapter doesn't exit, returns null.
     * @param chapterPath path of chapter
     * @return instance of ChapterRepresentation
     * */
    public ChapterRepresentation getChapterRepresentation(Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
                return chapter.getRepresentation();
        }
        return null;
    }

    /**
     * Returns QuestionRepresentation instance that corresponds to speciffied question.
     * If question doesn't exit, returns null.
     * @param chapterPath path of chapter
     * @param questionPath path of question
     * @return instance of QuestionRepresentation
     * */
    public QuestionRepresentation getQuestionRepresentation(Path chapterPath, Path questionPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
                return chapter.getQuestionRepresentation(questionPath);
        }
        return null;
    }

    /**
     * Returns path of last returned question. If no question was returned, returns null.
     * Question can be returned by nextQuestion() or previousQuestion() function.
     * @return path of last returned question.
     * */
    public Path getLastReturnedQuestionPath()
    {
        if(lastReturnedQuestion == null)
            return null;
        return lastReturnedQuestion.getPath();
    }

    /**
     * Sets active state of all questions on true.
     * */
    public void activateAllQuestions()
    {
        activeQuestionsNumber = 0;
        for(Chapter chapter : chapterList)
        {
            chapter.setAllQuestionsActive();
            activeQuestionsNumber += chapter.getActiveQuestionsNumber();
        }
        correctAnsweredQuestions = 0;
        inCorrectAnsweredQuestions = 0;
    }

    /**
     * Sets active state of all questions on false.
     * */
    public void deactivateAllQuestions()
    {
        activeQuestionsNumber = 0;
        for(Chapter chapter : chapterList)
            chapter.setAllQuestionsNotActive();
        activeQuestionsNumber = 0;
        correctAnsweredQuestions = 0;
        inCorrectAnsweredQuestions = 0;
    }

    /**
     * Sets active state of all questions from specific chapter on true.
     * if chapter doens't exist, does nothing.
     * */
    public void activateAllQuestionsOfChapter(Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
            {
                activeQuestionsNumber -= chapter.getActiveQuestionsNumber();
                correctAnsweredQuestions -= chapter.getCorrectAnsweredQuestions();
                inCorrectAnsweredQuestions -= chapter.getInCorrectAnsweredQuestions();
                chapter.setAllQuestionsActive();
                activeQuestionsNumber += chapter.getActiveQuestionsNumber();
                break;
            }
        }
    }

    /**
     * Sets active state of all questions from specific chapter on false.
     * if chapter doens't exist, does nothing.
     * */
    public void deactivateAllQuestionsOfChapter(Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
            {
                activeQuestionsNumber -= chapter.getActiveQuestionsNumber();
                correctAnsweredQuestions -= chapter.getCorrectAnsweredQuestions();
                inCorrectAnsweredQuestions -= chapter.getInCorrectAnsweredQuestions();
                chapter.setAllQuestionsNotActive();
                break;
            }
        }
    }

    /**
     * Sets state of all questions on QuestionState.NO_ANSWER.
     * */
    public void resetAnswers()
    {
        for(Chapter chapter : chapterList)
            chapter.resetAnswers();
        inCorrectAnsweredQuestions = 0;
        correctAnsweredQuestions = 0;
    }

    /**
     * Sets state of all questions from specific chapter on QuestionState.NO_ANSWER.
     * if chapter doens't exist, does nothing.
     * @param chapterPath path of chapter
     * */
    public void resetChapterAnswers(Path chapterPath)
    {
        for(Chapter chapter : chapterList)
        {
            if(chapter.getPath().equals(chapterPath))
            {
                correctAnsweredQuestions -= chapter.getCorrectAnsweredQuestions();
                inCorrectAnsweredQuestions -= chapter.getInCorrectAnsweredQuestions();
                chapter.resetAnswers();
                break;
            }
        }
    }

    /**
     * Updates content of qiven question. if update wasn't succesful, returns false.
     * @param chapterPath path of chapter
     * @param questionPath path of question
     * @param questionText new text of question
     * @param answerText new text of question_answer
     * @return boolean, true - update was succesfull
     *                  false - update was not succesfull
     **/
    public boolean updateQuestion(Path chapterPath, Path questionPath,
                                  String questionText, String answerText)
    {
        Chapter chapter = getChapter(chapterPath);
        if(chapter != null)
            return chapter.updateQuestion(questionPath, questionText, answerText);
        return false;
    }

    private Chapter getChapter(final Path chapterPath)
    {
        for (Chapter chapter : chapterList)
        {
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
                if(f.isDirectory())
                {
                    Chapter newChapter = Chapter.readChapter(f.toPath());
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

