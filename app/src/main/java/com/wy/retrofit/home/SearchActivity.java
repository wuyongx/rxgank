package com.wy.retrofit.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.wy.retrofit.R;
import com.wy.retrofit.activity.SwipeBackBaseActivity;
import com.wy.retrofit.di.components.ActivityComponent;
import com.wy.retrofit.kjhttp.ApiService;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.yokeyword.fragmentation.SwipeBackLayout;
import rx.android.schedulers.AndroidSchedulers;

public class SearchActivity extends SwipeBackBaseActivity {
  @BindView(R.id.toolbar) Toolbar toolbar;
  @Inject RetrofitClient api;
  @Inject ApiService service;
  private String type;
  private SearchFragment searchFragment;
  private ActivityComponent mSearchComponent;

  @Override protected void onResume() {
    super.onResume();
    searchFragment =
        (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.content_container);
  }

  public static void startActivity(Context context, String type) {
    Intent intent = new Intent(context, SearchActivity.class);
    intent.putExtra(ARG_PARAM1, type);
    context.startActivity(intent);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_search);
    unbinder = ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    ActionBar supportActionBar = getSupportActionBar();
    //noinspection ConstantConditions
    supportActionBar.setDisplayShowTitleEnabled(false);
  }

  @Override protected void setListener() {

  }

  @Override protected void processLogic(Bundle savedInstanceState) {
    mSearchComponent = getAppComponent().newActivitySubComponent(getActivityModule());
    mSearchComponent.inject(this);
    type = getIntent().getStringExtra(ARG_PARAM1);
    if (savedInstanceState == null) {
      loadRootFragment(R.id.content_container, SearchFragment.newInstance(type));
    }
    getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_ALL);
  }

  public ActivityComponent getSearchComponent() {
    return mSearchComponent;
  }

  @Override public void onBackPressedSupport() {
    NavUtils.navigateUpFromSameTask(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    MenuItemCompat.expandActionView(searchItem);

    RxSearchView.queryTextChanges(searchView)
        .filter(text -> text.length() >= 2)
        .debounce(500, TimeUnit.MILLISECONDS)
        .map(CharSequence::toString)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(key -> searchFragment.searchGank(type, key));
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    //noinspection SimplifiableIfStatement
    if (id == R.id.action_search) {
      return true;
    } else if (id == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
