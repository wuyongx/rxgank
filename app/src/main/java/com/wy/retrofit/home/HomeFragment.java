package com.wy.retrofit.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.wy.retrofit.kjhttp.rxjava.DataException;
import com.wy.retrofit.kjhttp.rxjava.NetWorkException;
import com.wy.retrofit.kjhttp.rxjava.ProgressSubscriber;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import com.wy.retrofit.kjhttp.rxjava.SampleSubscriber;
import com.wy.retrofit.util.ResourceUtil;
import com.wy.retrofit.util.Shares;
import com.wy.retrofit.view.LoadType;
import java.util.List;
import javax.inject.Inject;

public class HomeFragment extends BaseFragment
    implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.OnRecyclerViewItemClickListener,
    BaseQuickAdapter.OnRecyclerViewItemChildClickListener {

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeLayout;
  @Inject RetrofitClient api;
  @Inject Gson gson;
  private int page = 1;
  private GankRecyclerAdapter adapter;
  private String type;
  private View noMoreDataView;
  private boolean canLoadMore;

  public static HomeFragment newInstance(String type) {
    Bundle bundle = new Bundle();
    bundle.putString(ARG_PARAM1, type);
    HomeFragment fragment = new HomeFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  protected void initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.content_home, container, false);
    noMoreDataView = inflater.inflate(R.layout.not_loading, container, false);
    setContentView(view);
    //inject HomeFragment to Dagger graph, then inject field can be use
    ((HomeActivity) _mActivity).getHomeComponent()
        .newFragmentComponent(getFragmentModule())
        .inject(this);
  }

  @Override protected void setListener() {
    swipeLayout.setColorSchemeColors(ResourceUtil.getColor(R.color.colorAccent));
    swipeLayout.setOnRefreshListener(this);
  }

  @Override protected void processLogic(Bundle savedInstanceState) {
    setupRecyclerView(recyclerView);
    type = getArguments().getString(ARG_PARAM1);
    getGank(LoadType.REFRESH);
  }

  public void setNewType(String type) {
    if (!TextUtils.isEmpty(type)) {
      this.type = type;
    }
    page = 1;
    canLoadMore = false;
    getGank(LoadType.REFRESH);
  }

  private void getGank(@LoadType int loadType) {
    api.getGankList(type, page,
        new ProgressSubscriber<>(_mActivity, new SampleSubscriber<List<GankInfo>>() {
          @Override public void onNext(List<GankInfo> gank) {
            canLoadMore = true;
            if (loadType == LoadType.REFRESH) {
              stopRefresh();
              adapter.setNewData(gank);
              recyclerView.scrollToPosition(0);
              adapter.openLoadMore(20, true);
            } else {
              adapter.addData(gank);
              adapter.notifyDataChangedAfterLoadMore(true);
            }
          }

          @Override public void onError(Throwable e) {
            stopRefresh();
            if (loadType == LoadType.LOAD_MORE) {
              adapter.notifyDataChangedAfterLoadMore(false);
              adapter.addFooterView(noMoreDataView);
            }
            if (e instanceof DataException) {
              canLoadMore = false;
            } else if (e instanceof NetWorkException) {
              page--;
            }
          }
        }));
  }

  private void stopRefresh() {
    if (swipeLayout.isRefreshing()) {
      swipeLayout.setRefreshing(false);
    }
  }

  private void setupRecyclerView(RecyclerView recyclerView) {
    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    recyclerView.hasFixedSize();
    adapter = new GankRecyclerAdapter(_mActivity, R.layout.recycler_item_gank, null);
    adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
    adapter.setOnRecyclerViewItemClickListener(this);
    adapter.setOnRecyclerViewItemChildClickListener(this);
    adapter.setOnLoadMoreListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override public void onLoadMoreRequested() {
    if (!canLoadMore) {
      return;
    }
    page++;
    getGank(LoadType.LOAD_MORE);
  }

  @Override public void onRefresh() {
    page = 1;
    getGank(LoadType.REFRESH);
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
          .subscribe(new ProgressSubscriber<>(_mActivity, this::setResult,null));
    }
  }

  public void setResult(List<GankInfo> gankInfos) {
    adapter.setNewData(gankInfos);
    recyclerView.scrollToPosition(0);
  }
}
