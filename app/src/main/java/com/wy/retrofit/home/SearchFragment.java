package com.wy.retrofit.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wy.retrofit.R;
import com.wy.retrofit.fragment.BaseFragment;
import com.wy.retrofit.gank.GankInfo;
import com.wy.retrofit.kjhttp.rxjava.ProgressSubscriber;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import com.wy.retrofit.util.Shares;
import java.util.List;
import javax.inject.Inject;

public class SearchFragment extends BaseFragment
    implements BaseQuickAdapter.OnRecyclerViewItemClickListener,
    BaseQuickAdapter.OnRecyclerViewItemChildClickListener {

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @Inject RetrofitClient api;
  @Inject Gson gson;
  private GankRecyclerAdapter adapter;

  public static SearchFragment newInstance(String type) {
    Bundle bundle = new Bundle();
    bundle.putString(ARG_PARAM1, type);
    SearchFragment fragment = new SearchFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  protected void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    setContentView(view);
    //inject HomeFragment to Dagger graph, then inject field can be use
    ((SearchActivity) _mActivity).getSearchComponent()
        .newFragmentComponent(getFragmentModule())
        .inject(this);
  }

  @Override protected void setListener() {
  }

  @Override protected void processLogic(Bundle savedInstanceState) {
    setupRecyclerView(recyclerView);
  }

  private void setupRecyclerView(RecyclerView recyclerView) {
    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    recyclerView.hasFixedSize();
    adapter = new GankRecyclerAdapter(_mActivity, R.layout.recycler_item_gank, null);
    adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
    adapter.setOnRecyclerViewItemClickListener(this);
    adapter.setOnRecyclerViewItemChildClickListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override public void onItemClick(View view, int position) {
    GankInfo item = adapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(item.url));
    startActivity(intent);
  }

  @Override public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    GankInfo item = this.adapter.getItem(position);
    String searchKey = null;
    int id = view.getId();

    if (id == R.id.iv_share) {
      if (item.url.endsWith(".jpg") || item.url.endsWith(".gif")) {
        Shares.shareImage(_mActivity, item.url, item.desc);
      } else {
        Shares.share(_mActivity, item.source, item.desc + " -> " + item.url);
      }
    } else if (id == R.id.iv_header || id == R.id.tv_source) {
      searchKey = item.who;
    } else if (id == R.id.tv_type) {
      searchKey = item.type;
    } else if (id == R.id.tv_date) {
      searchKey = item.createdAt.substring(0, 10);
    }

    if (!TextUtils.isEmpty(searchKey)) {
      api.searchGank("all", searchKey)
          .compose(RetrofitClient.applySchedulers())
          .subscribe(new ProgressSubscriber<>(_mActivity, this::setResult, null));
    }
  }

  public void setResult(List<GankInfo> gankInfos) {
    adapter.setNewData(gankInfos);
    recyclerView.scrollToPosition(0);
  }

  public void searchGank(String type, String searchKey) {
    if (!TextUtils.isEmpty(searchKey)) {
      api.searchGank(type, searchKey)
          .compose(RetrofitClient.applySchedulers())
          .subscribe(new ProgressSubscriber<>(_mActivity, gank -> {
            recyclerView.postDelayed(new Runnable() {
              @Override public void run() {
                hideSoftInput();
              }
            }, 40);
            setResult(gank);
          }, null));
    }
  }
}
