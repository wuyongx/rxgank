package com.wy.retrofit.di.modules;

import android.app.Activity;
import com.wy.retrofit.di.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module public class ActivityModule {

  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @ActivityScope public Activity provideActivity() {
    return activity;
  }
}
