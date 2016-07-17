package com.wy.retrofit.home;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;
import com.wy.retrofit.R;
import com.wy.retrofit.activity.BaseActivity;
import com.wy.retrofit.di.components.ActivityComponent;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import com.wy.retrofit.util.FragmentationUtil;
import com.wy.retrofit.util.L;
import javax.inject.Inject;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Subscriber;

public class HomeActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, OnTabClickListener {
  private static final String[] TYPE = { "all", "Android", "休息视频" };

  private ActivityComponent mHomeComponent;
  private BottomBar mBottomBar;
  @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
  @BindView(R.id.content_container) FrameLayout frameLayout;
  @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.nav_view) NavigationView navigationView;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.search_layout) LinearLayout searchLayout;
  ImageView navHeaderImage;
  @Inject RetrofitClient api;

  private SupportFragment[] fragments = new SupportFragment[3];

  private int pre;
  private int current;

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_home);
    unbinder = ButterKnife.bind(this);
    navHeaderImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
  }

  @Override protected void setListener() {
    searchLayout.setOnClickListener(view -> SearchActivity.startActivity(mContext, TYPE[current]));
  }

  @Override protected void processLogic(Bundle savedInstanceState) {
    mBottomBar = BottomBar.attachShy(coordinatorLayout, frameLayout, savedInstanceState);
    mBottomBar.setItems(new BottomBarTab(R.drawable.ic_create_read, TYPE[0]),
        new BottomBarTab(R.drawable.ic_create_read, TYPE[1]),
        new BottomBarTab(R.drawable.ic_create_read, TYPE[2]));
    mBottomBar.setOnTabClickListener(this);

    //inject HomeActivity to Dagger graph, then inject field can be use
    mHomeComponent = getAppComponent().newActivitySubComponent(getActivityModule());
    mHomeComponent.inject(this);
    if (savedInstanceState == null) {
      fragments[0] = HomeFragment.newInstance(TYPE[0]);
      fragments[1] = HomeFragment.newInstance(TYPE[1]);
      fragments[2] = HomeFragment.newInstance(TYPE[2]);
      loadMultipleRootFragment(R.id.content_container, 0, fragments);
    } else {
      fragments[0] = findFragment(HomeFragment.class);
      fragments[1] = findFragment(HomeFragment.class);
      fragments[2] = findFragment(HomeFragment.class);
    }
    setSupportActionBar(toolbar);

    FloatingActionButton fab = getViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        HomeFragment homeFragment =
            (HomeFragment) FragmentationUtil.getActiveFragment(null, getSupportFragmentManager());
        homeFragment.setNewType(null);
      }
    });
    setupDrawerContent(navigationView);

    getMeizi();
  }

  private void getMeizi() {
    api.getMeizi(new Subscriber<String>() {
      @Override public void onCompleted() {

      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(String url) {
        Glide.with(mContext).load(url).crossFade().centerCrop().into(navHeaderImage);
      }
    });
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override public void onBackPressedSupport() {
    super.onBackPressedSupport();
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawers();
    } else {
      super.onBackPressedSupport();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    item.setCheckable(true);
    String key = item.getTitle().toString();
    ((HomeFragment) fragments[0]).setNewType(key);
    mDrawerLayout.closeDrawers();
    return true;
  }

  public ActivityComponent getHomeComponent() {
    return mHomeComponent;
  }

  @Override public void onTabSelected(int position) {
    current = position;
    showHideFragment(fragments[position], fragments[pre]);
    pre = position;
  }

  @Override public void onTabReSelected(int position) {
    L.e(TAG, position);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mBottomBar.onSaveInstanceState(outState);
  }
}
