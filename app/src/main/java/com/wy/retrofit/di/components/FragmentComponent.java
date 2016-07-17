package com.wy.retrofit.di.components;

import com.wy.retrofit.di.modules.FragmentModule;
import com.wy.retrofit.di.scopes.FragmentScope;
import com.wy.retrofit.home.HomeFragment;
import com.wy.retrofit.home.SearchFragment;
import dagger.Subcomponent;

/**
 * Created by wuyong on 16/6/5
 */
@FragmentScope @Subcomponent(modules = FragmentModule.class) public interface FragmentComponent {
  void inject(HomeFragment fragment);
  void inject(SearchFragment fragment);

}
