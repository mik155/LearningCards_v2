package actionlisteners.initframe;

import database.Database;
import database.Mode;
import gui.entryframe.EntryFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InitFrameListener implements ActionListener
{
    private JTextArea textArea;
    private JFrame initFrame;
    public InitFrameListener(JFrame iFrame, JTextArea tArea)
    {
        textArea = tArea;
        initFrame = iFrame;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        Object object = actionEvent.getSource();
        if(object instanceof JButton)
        {
            JButton pressedButton = (JButton) object;
            Path path = Paths.get(textArea.getText().trim());
            File file = new File(path.toString());
            if(file.exists() && file.isDirectory())
            {
                if(pressedButton.getText().equals("CREATE"))
                {
                    Database database = new Database(path, Mode.CREATE);
                    initFrame.dispose();
                    EntryFrame entryFrame = new EntryFrame(0, 0, 0);
                }
                else  if(pressedButton.getText().equals("OPEN"))
                {
                    Database database = new Database(path, Mode.OPEN);
                    initFrame.dispose();
                    EntryFrame entryFrame = new EntryFrame(database.getCorrectAnsweredQuestions(),
                            database.getInCorrectAnsweredQuestions(),
                            database.getActiveQuestionsNumber());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(initFrame, "Entered Path is incorrect.");
            }

        }
    }
}
