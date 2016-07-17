package com.wy.retrofit.di.modules;

import android.support.v4.app.Fragment;
import com.wy.retrofit.di.scopes.FragmentScope;
import dagger.Module;
import dagger.Provides;

/**
 * FragmentModule
 * Created by wuyong on 16/6/5.
 */
@Module public class FragmentModule {
 final Fragment fragment;

  public FragmentModule(Fragment fragment) {
    this.fragment = fragment;
  }

  @FragmentScope @Provides Fragment getFragment() {
    return fragment;
  }
}
