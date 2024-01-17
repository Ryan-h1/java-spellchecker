package ca.uwo.cs2212.group2.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ca.uwo.cs2212.group2.constants.ViewConstants.MENU_BACKGROUND_COLOUR;

public class HelpPopup extends JDialog {

  private static final String MESSAGE_TEXT =
      "<html><font color = 'white'><center>Welcome to our spellchecker! <br><br>"
          + "To view correction suggestions, click on the red word. <br><br>"
          + "There are 5 major tabs for navigating through the application: <br><br>"
          + "Settings, File, Spell Check, Metrics, Save. <br><br>"
          + "To spellcheck your document, click spellcheck. <br><br>"
          + "To view the metrics of your document, click metrics. <br><br>"
          + "To add or remove a word from your dictionary, click settings. <br><br> "
          + "To create a new file, open a file, or save your file, click file.</center> </font></html>";

  /**
   * Constructor for the help popup
   *
   * @param parentFrame the frame to display the popup in
   */
  public HelpPopup(JFrame parentFrame) {
    super(parentFrame, "Help", true); // 'true' for modal

    // Set up the content panel
    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(new Color(0x993399));
    contentPanel.setPreferredSize(new Dimension(400, 350));
    contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Set up the label with the help message
    JLabel helpLabel = new JLabel(MESSAGE_TEXT);
    helpLabel.setFont(
        new Font(helpLabel.getFont().getName(), helpLabel.getFont().getStyle(), 13));
    helpLabel.setPreferredSize(new Dimension(400, 350));
    helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
    contentPanel.add(helpLabel);
    
    // Add content panel to the dialog
    this.add(contentPanel);
    this.pack();
    this.setLocationRelativeTo(parentFrame); // Center relative to the parent frame
  }

  public HelpPopup() {

    // Set up the content panel
    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(new Color(0x993399));
    contentPanel.setPreferredSize(new Dimension(400, 300));
    contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Set up the label with the help message
    JLabel helpLabel = new JLabel(MESSAGE_TEXT);
    helpLabel.setFont(
        new Font(helpLabel.getFont().getName(), helpLabel.getFont().getStyle(), 13));
    helpLabel.setPreferredSize(new Dimension(400, 300));
    helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
  public static void showHelpDialog(JFrame parentFrame) {
    HelpPopup helpPopup = new HelpPopup(parentFrame);
    helpPopup.setVisible(true);
  }

  public static void showHelpDialog() {
    HelpPopup helpPopup = new HelpPopup();
    helpPopup.setVisible(true);
  }

  
  
}
