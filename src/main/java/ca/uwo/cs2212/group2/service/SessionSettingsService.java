package ca.uwo.cs2212.group2.service;

/**
 * Singleton service that keeps track of the user's session settings.
 *
 * @author Ryan Hecht
 */
public class SessionSettingsService {
  private static SessionSettingsService instance;
  private boolean HTMLModeTurnedOn;

  private SessionSettingsService() {
    HTMLModeTurnedOn = false;
  }

  public static SessionSettingsService getInstance() {
    if (instance == null) {
      instance = new SessionSettingsService();
    }
    return instance;
  }

  public boolean isHTMLModeTurnedOn() {
    return HTMLModeTurnedOn;
  }

  public void setHTMLModeTurnedOn(boolean HTMLModeTurnedOn) {
    this.HTMLModeTurnedOn = HTMLModeTurnedOn;
  }
}
