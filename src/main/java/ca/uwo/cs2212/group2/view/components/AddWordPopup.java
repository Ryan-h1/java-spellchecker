package ca.uwo.cs2212.group2.view.components;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.uwo.cs2212.group2.controller.*;
import ca.uwo.cs2212.group2.model.*;

/**
 * @author Shaylan Pratt
 * Represents a popup dialog for adding a word to the user dictionary.
 */
public class AddWordPopup extends JDialog {

    private static final String MESSAGE_TEXT = "<html><font color = 'white'>Enter a word to add to your dictionary:</font></html>";
    private Dictionary userDict; // Reference to the user dictionary


    /**
     * Constructs an AddWordPopup object with the given user dictionary.
     *
     * @param userDict the user dictionary to which the word will be added.
     */
    public AddWordPopup(Dictionary userDict){
        this.userDict=userDict; 
        
        // Set up the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0x993399));
        contentPanel.setPreferredSize(new Dimension(400, 350));
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        

        // Set up the label with the help message
        JLabel helpLabel = new JLabel(MESSAGE_TEXT);
        helpLabel.setPreferredSize(new Dimension(250, 250));
        contentPanel.add(helpLabel);


        // Add text field
        JTextField wordTextField = new JTextField();
        wordTextField.setPreferredSize(new Dimension(250, 30));
        contentPanel.add(wordTextField);

        // Add submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your logic here to handle the submitted word
                String word = wordTextField.getText();
                System.out.println("Submitted word: " + word);
                userDict.addWord(word); 
                
                Speller.getInstance().writeLineToFile(word,true);
                System.out.println(Speller.getInstance().getUserDict());
                // You can close the dialog if needed
                dispose();
            }
        });
        contentPanel.add(submitButton);


        // Add content panel to the dialog
        this.add(contentPanel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the dialog
    }


    /**
     * Displays the AddWordPopup dialog with the given user dictionary.
     *
     * @param userDict the user dictionary to which the word will be added.
     */
    public static void showAddWordDialog(Dictionary userDict) {
        AddWordPopup popup = new AddWordPopup(userDict);
        popup.setVisible(true);
      }
    
}
