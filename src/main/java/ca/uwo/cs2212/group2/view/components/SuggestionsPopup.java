package ca.uwo.cs2212.group2.view.components;

import ca.uwo.cs2212.group2.service.WordsToIgnoreOnceService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ca.uwo.cs2212.group2.model.*;

public class SuggestionsPopup extends JDialog {

  public interface SuggestionSelectedCallback {
    void onSuggestionSelected(String suggestion);
  }

  private JButton suggestion1Button;
  private JButton suggestion2Button;
  private JButton suggestion3Button;
  private JButton suggestion4Button;
  private SuggestionSelectedCallback callback;
  private SuggestionSelectedCallback callback2;

  private Word currentWord; // The word being processed

  private Speller speller;

  public SuggestionsPopup(
      Word word,
      String sugg1,
      String sugg2,
      String sugg3,
      String sugg4,
      SuggestionSelectedCallback callback,
      SuggestionSelectedCallback callback2) {

    this.currentWord = word;
    this.speller = Speller.getInstance();
    // Set up the content panel

    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(new Color(0x993399));
    contentPanel.setPreferredSize(new Dimension(400, 350));
    // contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    contentPanel.setLayout(new GridLayout(0, 1, 10, 10)); // GridLayout with 2 columns

    // Add buttons
    suggestion1Button = new JButton(sugg1);
    suggestion2Button = new JButton(sugg2);
    suggestion3Button = new JButton(sugg3);
    suggestion4Button = new JButton(sugg4);
    JButton addToDictionaryButton = new JButton("Add word to dictionary");
    JButton ignoreOnceButton = new JButton("Ignore once");
    JButton ignoreForSession = new JButton("Ignore for session");
    JButton manualCorrectionButton = new JButton("Manual correction");
    JButton removeWordButton = new JButton("Delete");

    // Add action listeners to buttons
    suggestion1Button.addActionListener(e -> callback.onSuggestionSelected(sugg1));
    suggestion2Button.addActionListener(e -> callback.onSuggestionSelected(sugg2));
    suggestion3Button.addActionListener(e -> callback.onSuggestionSelected(sugg3));
    suggestion4Button.addActionListener(e -> callback.onSuggestionSelected(sugg4));
    addToDictionaryButton.addActionListener(
        event -> {
          if (currentWord != null) {
            this.speller.getDict().addWord(currentWord.getContent());
            this.speller.resetCache();
            this.speller.getUserDict().addWord(word.getContent());
            Speller.getInstance().writeLineToFile(word.getContent(),true);
            this.dispose();
          }
        });
    ignoreOnceButton.addActionListener(
        event -> {
          if (currentWord != null) {
            WordsToIgnoreOnceService.getInstance().ignoreWordOnce(currentWord);
            this.dispose();
          }
        });
    ignoreForSession.addActionListener(
        event -> {
          if (currentWord != null) {
            this.speller.getDict().addWord(currentWord.getContent());
            this.speller.resetCache();
            this.dispose();
          }
        });
    manualCorrectionButton.addActionListener(event -> this.dispose());
    removeWordButton.addActionListener(
        event -> {
          callback2.onSuggestionSelected(" ");
        });

    // Add buttons to the content panel
    contentPanel.add(suggestion1Button);
    contentPanel.add(suggestion2Button);
    contentPanel.add(suggestion3Button);
    contentPanel.add(suggestion4Button);
    contentPanel.add(addToDictionaryButton);
    contentPanel.add(ignoreOnceButton);
    contentPanel.add(ignoreForSession);
    contentPanel.add(manualCorrectionButton);
    contentPanel.add(removeWordButton);

    // Add content panel to the dialog
    this.add(contentPanel);
    this.pack();
  }

  public void showSuggestionsDialog() {
    this.setVisible(true);
  }
}
