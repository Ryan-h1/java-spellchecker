package ca.uwo.cs2212.group2.view.components;


import javax.swing.*;
import java.awt.*;

public class SavePopup extends JDialog{
    private static final String MESSAGE_TEXT = "<html><font color = 'white'>Please save your document before spellchecking</font></html>";

    public SavePopup(){
        // Set up the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0x993399));
        contentPanel.setPreferredSize(new Dimension(400, 350));
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Set up the label with the help message
        JLabel helpLabel = new JLabel(MESSAGE_TEXT);
        helpLabel.setPreferredSize(new Dimension(250, 250));
        contentPanel.add(helpLabel);

        // Add content panel to the dialog
        this.add(contentPanel);
        this.pack();
    }

    public static void showSaveDialog() {
        SavePopup popup = new SavePopup();
        popup.setVisible(true);
      }

    
}
