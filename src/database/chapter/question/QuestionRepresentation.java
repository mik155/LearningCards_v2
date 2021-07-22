package database.chapter.question;

import java.nio.file.Path;

public class QuestionRepresentation
{
    private Path questionPath;
    private Path chapterPath;
    private boolean isActive;
    private QuestionState questionState;
    private String description;

    public QuestionRepresentation(Path questionPath, Path chapterPath, boolean isActive, QuestionState questionState, String description) {
        this.questionPath = questionPath;
        this.chapterPath = chapterPath;
        this.isActive = isActive;
        this.questionState = questionState;
        this.description = description;
    }

    public QuestionRepresentation(Path questionPath, boolean isActive, QuestionState questionState, String description) {
        this.questionPath = questionPath;
        this.chapterPath = null;
        this.isActive = isActive;
        this.questionState = questionState;
        this.description = description;
    }

    public void setQuestionPath(Path path) {
        this.questionPath = path;
    }
    public void setChapterPath(Path path) {
        this.chapterPath = path;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setQuestionState(QuestionState questionState) {
        this.questionState = questionState;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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
