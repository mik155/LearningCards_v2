package database.chapter.question;

import java.nio.file.Path;

public class QuestionRepresentation
{
    private Path questionPath;
    private Path chapterPath;
    private boolean isActive;
    private QuestionState questionState;
    private String questionText;
    private String answerText;

    /**
     * Constructor method.
     * @param questionPath path of question
     * @param isActive activeState of question
     * @param questionState state of question
     * @param questionText text of question
     * @param answerText text of answer
     * */
    public QuestionRepresentation(Path questionPath, boolean isActive, QuestionState questionState, String questionText, String answerText)
    {
        this.questionPath = questionPath;
        this.chapterPath = null;
        this.isActive = isActive;
        this.questionState = questionState;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    /**
     * Sets path of question
     * @param path path of question
     * */
    public void setQuestionPath(Path path)
    {
        this.questionPath = path;
    }

    /**
     * Sets chapter path.
     * @param path path of chapter
     * */
    public void setChapterPath(Path path) {
        this.chapterPath = path;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Sets question stata.
     * @param questionState state of question
     * */
    public void setQuestionState(QuestionState questionState) {
        this.questionState = questionState;
    }

    /**
     * Sets question description.
     * @param questionText text of question
     * */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public Path getQuestionPath() {
        return questionPath;
    }
    public Path getChapterPath() {
        return chapterPath;
    }

    public boolean isActive() {
        return isActive;
    }

    public QuestionState getQuestionState() {
        return questionState;
    }
}
