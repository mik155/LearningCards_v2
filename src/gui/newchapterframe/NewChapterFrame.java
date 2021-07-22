package gui.newchapterframe;

import actionlisteners.newchapterframe.NewChapterFrameListener;
import gui.entryframe.EntryFrame;
import java.nio.file.Path;

import javax.swing.*;
import java.awt.*;

public class NewChapterFrame extends JFrame
{
    public static final int FRAME_WIDTH = 500;
    public static final int FRAME_HEIGTH = 195;

    public  static final int LABEL_WIDTH = FRAME_WIDTH;
    public  static final int LABEL_HEIGTH = 50;

    public static final int TEXT_AREA_WIDTH = FRAME_WIDTH;
    public static final int TEXT_AREA_HEIGTH = 200;

    public static final int BOTTOM_MENU_WIDTH = FRAME_WIDTH;
    public static final int BOTTOM_MENU_HEIGTH = FRAME_HEIGTH - LABEL_HEIGTH - TEXT_AREA_HEIGTH;


    private JTextArea textArea;
    private JButton okButton;
    private JButton cancelButton;

    public NewChapterFrame()
    {
        setTextArea();
        add(getLabel(), BorderLayout.PAGE_START);
        add(textArea, BorderLayout.CENTER);
        add(getBottomMenu(), BorderLayout.PAGE_END);

        setLocation(Utils.Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setActionListeners();
        setVisible(true);
    }

    /**
     * Returns String from textArea
     * @return  String representing name of new Chapter
     * */
    public String getText()
    {
        return textArea.getText();
    }

    private JPanel getLabel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("• Chapter name:");
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 17));

        panel.add(Box.createRigidArea(new Dimension(0,30)), BorderLayout.PAGE_START);
        panel.add(label, BorderLayout.CENTER);
        panel.add(Box.createRigidArea(new Dimension(0,15)), BorderLayout.PAGE_END);
        panel.add(Box.createRigidArea(new Dimension(50,15)), BorderLayout.LINE_START);
        return panel;
    }

    private void setTextArea()
    {
        add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.LINE_START );
        add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.LINE_END );
        textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private JPanel getBottomMenu()
    {
        JPanel bottomMenu = new JPanel();
        bottomMenu.setLayout(new BoxLayout(bottomMenu, BoxLayout.LINE_AXIS));

        okButton = new JButton("OK");
        cancelButton = new JButton("CANCEL");
        stylyzeButton(okButton);
        stylyzeButton(cancelButton);

        bottomMenu.add(Box.createRigidArea(new Dimension(50,15)));
        bottomMenu.setPreferredSize(new Dimension(FRAME_WIDTH, 80));
        bottomMenu.setMaximumSize(new Dimension(FRAME_WIDTH, 80));
        bottomMenu.add(okButton);
        bottomMenu.add(Box.createRigidArea(new Dimension(30,0)));
        bottomMenu.add(cancelButton);

        return bottomMenu;
    }

    private void setActionListeners()
    {
        okButton.addActionListener(new NewChapterFrameListener(this));
        cancelButton.addActionListener(new NewChapterFrameListener(this));
    }

    private   static void stylyzeButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 15));
    }
}
