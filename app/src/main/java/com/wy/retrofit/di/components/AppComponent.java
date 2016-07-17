package com.wy.retrofit.di.components;

import com.google.gson.Gson;
import com.wy.retrofit.App;
import com.wy.retrofit.di.modules.ActivityModule;
import com.wy.retrofit.di.modules.AppModule;
import com.wy.retrofit.di.modules.NetWorkApiModule;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import dagger.Component;
import javax.inject.Singleton;

/**
 * 官方对 Component 的说明 :http://google.github.io/dagger/users-guide
 */
@Singleton @Component(modules = { AppModule.class, NetWorkApiModule.class })
public interface AppComponent {
  App app();
  RetrofitClient retrofit();
  Gson gson();
  ActivityComponent newActivitySubComponent(ActivityModule activityModule);
}
