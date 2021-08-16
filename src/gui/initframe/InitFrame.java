package gui.initframe;

import actionlisteners.initframe.InitFrameListener;

import javax.swing.*;
import java.awt.*;

public class InitFrame extends JFrame
{
    public static final int FRAME_WIDTH = 500;
    public static final int FRAME_HEIGTH = 195;
    public static final int SIDE_MARGIN = 20;

    private JButton openButton;
    private JButton createButton;
    private JLabel label;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public InitFrame()
    {
        setLabel();
        setTextArea();
        setButtons();
        setActionListeners();

        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGTH));
        setMinimumSize(new Dimension(FRAME_WIDTH, 155));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(Utils.Utils.getCenterFramePoint(FRAME_WIDTH, FRAME_HEIGTH));
        setResizable(false);
        setVisible(true);
    }

    private void setLabel()
    {

        add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.LINE_AXIS));

        label = new JLabel("Enter Path:");
        label.setFont(new Font(label.getFont().getName(), Font.ITALIC, 18));

        innerPanel.add(Box.createRigidArea(new Dimension(SIDE_MARGIN, 0)));
        innerPanel.add(label, BorderLayout.CENTER);
        innerPanel.add(Box.createHorizontalGlue());

        add(innerPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setTextArea()
    {
        JPanel panel = new JPanel();
        textArea = new JTextArea();
        textArea.setRows(1);
        textArea.setLineWrap(false);
        textArea.getDocument().putProperty("filterNewlines", true);
        scrollPane = new JScrollPane(textArea);

        scrollPane.setPreferredSize(new Dimension(FRAME_WIDTH - 2 * SIDE_MARGIN, 40));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.setMaximumSize(new Dimension(FRAME_WIDTH - SIDE_MARGIN, 50));
        panel.add(Box.createRigidArea(new Dimension(0, 15)), BorderLayout.PAGE_END);
        panel.add(scrollPane);
        add(panel);
    }

    private void setButtons()
    {
        openButton = new JButton("OPEN");
        createButton = new JButton("CREATE");
        stylyzeButton(openButton);
        stylyzeButton(createButton);

        JPanel bottomMenu = new JPanel();
        bottomMenu.setLayout(new BoxLayout(bottomMenu, BoxLayout.LINE_AXIS));

        bottomMenu.add(Box.createRigidArea(new Dimension(SIDE_MARGIN, 0)));
        bottomMenu.add(openButton);
        bottomMenu.add(Box.createRigidArea(new Dimension(40, 0)));
        bottomMenu.add(createButton);
        bottomMenu.add(Box.createHorizontalGlue());
        add(bottomMenu);
     }

    private static void stylyzeButton(final JButton button)
    {
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 16));
    }

    private void setActionListeners()
    {
        openButton.addActionListener(new InitFrameListener(this, textArea));
        createButton.addActionListener(new InitFrameListener(this, textArea));
    }
}
