package ca.uwo.cs2212.group2.controller;

import ca.uwo.cs2212.group2.service.UploadFileStateService;
import ca.uwo.cs2212.group2.view.pages.LandingMenu;

import java.awt.event.ActionListener;

public class LandingMenuController {
  private LandingMenu view;
  private ViewController viewController;

  public LandingMenuController(LandingMenu view, ViewController viewController) {
    this.view = view;
    this.viewController = viewController;
    attachListeners();
  }

  private void attachListeners() {
    this.view.addNewFileButtonListener(createNewFileActionListener());
    this.view.addUploadFileButtonListener(createUploadFileActionListener());
  }

  private ActionListener createNewFileActionListener() {
    return e -> {
      viewController.showPanel("MainPanel");
    };
  }

  private ActionListener createUploadFileActionListener() {
    return e -> {
      viewController.showPanel("MainPanel");
      UploadFileStateService.getInstance().setShouldTriggerFileUpload(true);
    };
  }
}
