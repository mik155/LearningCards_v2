package questionlistframe;

import database.chapter.question.QuestionState;
import gui.questionListFrame.QuestionListFrame;
import gui.questionListFrame.QuestionPanel;

import java.nio.file.Paths;

public class QuestionListFrameTest
{
    public static void main(String [] args)
    {
        QuestionListFrame questionListFrame = new QuestionListFrame(null, null);
        questionListFrame.addQuestionPanel(new QuestionPanel( Paths.get(""),Paths.get(""), "Pytanie nr 1", QuestionState.CORRECT, true));
        questionListFrame.addQuestionPanel(new QuestionPanel( Paths.get(""),Paths.get(""), "Pytanie nr 2", QuestionState.CORRECT, true));
        questionListFrame.addQuestionPanel(new QuestionPanel( Paths.get(""),Paths.get(""), "Pytanie nr 3", QuestionState.CORRECT, true));

    }
}
