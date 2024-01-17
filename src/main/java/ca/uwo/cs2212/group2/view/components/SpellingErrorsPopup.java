package ca.uwo.cs2212.group2.view.components;

import javax.swing.*;

import ca.uwo.cs2212.group2.model.Speller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ca.uwo.cs2212.group2.constants.ViewConstants.PRIMARY_COLOUR;

public class SpellingErrorsPopup extends JDialog{

    int Misspellings = Speller.getInstance().getWrongWords().size();
    int Miscapitalizations = Speller.getInstance().getStats()[3];
    int Doublewords = Speller.getInstance().getStats()[4];



    private  final String MESSAGE_TEXT = "<html>" +
    "<head>" +
    "<style>" +
    "body { font-family: Arial, sans-serif; }" +
    ".header { font-size: 20px; color: #FFFFFF; }" +
    ".metric { font-size: 16px; color: #FFFFFF; }" +
    "</style>" +
    "</head>" +
    "<body>" +
    "<div class='header'>Number of spelling errors:</div>" +
    "<div class='metric'>Misspellings: " + Misspellings + "</div>" +
    "<div class='metric'>Miscapitalizations: " + Miscapitalizations + "</div>" +
    "<div class='metric'>Double word: " + Doublewords + "</div>" +
    "</body>" ;

    /**
   * Constructor for the spelling errors popup
   *
   * @param parentFrame the frame to display the popup in
   */
    public SpellingErrorsPopup(JFrame parentFrame){
        super(parentFrame, "Spelling Errors", true); // 'true' for modal

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
        this.setLocationRelativeTo(parentFrame); // Center relative to the parent frame

    }


    public SpellingErrorsPopup(){
        

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




    /**
    * Method to display the popup
   *
   * @param parentFrame the frame to display the popup in
   */
    public static void showSpellingErrorsDialog(JFrame parentFrame) {
            SpellingErrorsPopup popup = new SpellingErrorsPopup(parentFrame);
            popup.setVisible(true);
    }

    public static void showSpellingErrorsDialog() {
            SpellingErrorsPopup popup = new SpellingErrorsPopup();
            popup.setVisible(true);
    }

}
