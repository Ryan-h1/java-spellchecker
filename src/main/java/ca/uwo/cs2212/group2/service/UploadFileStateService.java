package ca.uwo.cs2212.group2.service;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.core.Observable;

public class UploadFileStateService {
  private static UploadFileStateService instance;
  private BehaviorSubject<Boolean> shouldTriggerFileUploadSubject;

  private UploadFileStateService() {
    shouldTriggerFileUploadSubject = BehaviorSubject.createDefault(false);
  }

  public static UploadFileStateService getInstance() {
    if (instance == null) {
      instance = new UploadFileStateService();
    }
    return instance;
  }

  public void setShouldTriggerFileUpload(boolean shouldTriggerFileUpload) {
    shouldTriggerFileUploadSubject.onNext(shouldTriggerFileUpload);
  }

  public Observable<Boolean> getShouldTriggerFileUploadObservable() {
    return shouldTriggerFileUploadSubject;
  }
}
