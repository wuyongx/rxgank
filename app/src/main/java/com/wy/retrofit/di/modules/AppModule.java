package com.wy.retrofit.di.modules;

import com.wy.retrofit.App;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Dagger2 依赖注入 之 Module
 */
@Module public class AppModule {
  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides @Singleton public App provideApp() {
    return app;
  }
}
