package questionlistframe;

import database.chapter.question.Question;
import database.chapter.question.QuestionState;
import gui.questionListFrame.QuestionListFrame;
import gui.questionListFrame.QuestionPanel;

import java.nio.file.Paths;

public class QuestionListFrameTest
{
    public static void main(String [] args)
    {
        QuestionListFrame questionListFrame = new QuestionListFrame();
        questionListFrame.addQuestionPanel(new QuestionPanel("Pytanie nr 1", Paths.get("")));
        questionListFrame.addQuestionPanel(new QuestionPanel("Pytanie nr 2", Paths.get("")));
        questionListFrame.addQuestionPanel(new QuestionPanel("Pytanie nr 3", Paths.get("")));

        questionListFrame.setVisible(true);
    }
}
