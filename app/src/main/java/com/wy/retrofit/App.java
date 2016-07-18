package com.wy.retrofit;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import com.wy.retrofit.di.components.AppComponent;
import com.wy.retrofit.di.components.DaggerAppComponent;
import com.wy.retrofit.di.modules.AppModule;
import com.wy.retrofit.kjhttp.rxjava.LibraryWrapper;

public class App extends Application {
  private AppComponent mComponent;
  private static App app;

  public static App getApp() {
    return app;
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
       MultiDex.install(this);
    }
  }

  @Override public void onCreate() {
    super.onCreate();
    app = this;
    buildComponentAndInject();
    // 初始化第三方库 Logger 日志库
    LibraryWrapper.init(this,"Logger");
  }

  private void buildComponentAndInject() {
    mComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
  }

  public AppComponent component() {
    return mComponent;
  }
}
