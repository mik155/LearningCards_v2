package database.chapter;

import java.nio.file.Path;;

public class ChapterRepresentation
{
    private Path path;
    private String name;
    private int correct;
    private int inCorrect;
    private int active;

    public ChapterRepresentation(Path cPath, String cName, int cCorrect, int cInCorrect, int cActive)
    {
        path = cPath;
        name = cName;
        correct = cCorrect;
        inCorrect = cInCorrect;
        active = cActive;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void setInCorrect(int inCorrect) {
        this.inCorrect = inCorrect;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public int getCorrect() {
        return correct;
    }

    public int getInCorrect() {
        return inCorrect;
    }

    public int getActive() {
        return active;
    }
}
