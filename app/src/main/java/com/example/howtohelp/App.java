package com.example.howtohelp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Helper class used for retrieving application context without creating a direct dependency on the
 * Android SDK
 */
public class App extends Application {
  /** The application context */
  private static Context context;

  @Override
  public void onCreate() {
    // initializes the context
    super.onCreate();
    context = this;
  }

  /**
   * Gets context.
   *
   * @return the application context
   */
  public static Context getContext() {
    return context;
  }
}
