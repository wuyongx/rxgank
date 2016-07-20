package com.wy.retrofit.di.components;

import com.wy.retrofit.di.modules.ActivityModule;
import com.wy.retrofit.di.modules.FragmentModule;
import com.wy.retrofit.di.scopes.ActivityScope;
import com.wy.retrofit.home.HomeActivity;
import com.wy.retrofit.home.SearchActivity;
import dagger.Subcomponent;

@ActivityScope @Subcomponent(modules = { ActivityModule.class })
public interface ActivityComponent {
  void inject(SearchActivity activity);
  void inject(HomeActivity activity);

  FragmentComponent newFragmentComponent(FragmentModule fragmentModule);
}
