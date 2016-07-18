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
import com.orhanobut.logger.Logger;
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
import com.wy.retrofit.view.LoadingViewInterface;
import java.util.List;
import javax.inject.Inject;

public class HomeFragment extends BaseFragment
    implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.OnRecyclerViewItemClickListener,
    BaseQuickAdapter.OnRecyclerViewItemChildClickListener, LoadingViewInterface {

  @BindView(R.id.recycler_view) RecyclerView recyclerView;
  @BindView(R.id.swipe_layout) SwipeRefreshLayout swipeLayout;
  @Inject RetrofitClient api;
  @Inject Gson gson;
  private int page = 1;
  private GankRecyclerAdapter adapter;
  private String type;
  private View noMoreDataView;
  private boolean canLoadMore;
  private View mLoadingView;

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
    mLoadingView = inflater.inflate(R.layout.loading_more, container, false);
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
            page++;// 数据加载成功,为上拉加载页码+1
            if (loadType == LoadType.REFRESH) {//下拉刷新
              adapter.setNewData(gank);
              recyclerView.scrollToPosition(0);
            } else {//上拉加载
              adapter.notifyDataChangedAfterLoadMore(gank, true);
            }
          }

          @Override public void onError(Throwable e) {
            if (loadType == LoadType.LOAD_MORE) {//处理上拉加载
              if (e instanceof NetWorkException) {//网络异常或超时
                page--;
              } else if (e instanceof DataException) {//接口没返回数据
                canLoadMore = false;
                adapter.addFooterView(noMoreDataView);
              }
            }
          }
        }));
  }

  /**
   * 关闭下拉和上拉刷新 view
   */
  private void stopRefresh() {
    if (swipeLayout == null || adapter == null) {
      return;
    }
    if (swipeLayout.isRefreshing()) {
      swipeLayout.setRefreshing(false);
    }
    adapter.notifyDataChangedAfterLoadMore(canLoadMore);
  }

  private void setupRecyclerView(RecyclerView recyclerView) {
    recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    recyclerView.hasFixedSize();
    adapter = new GankRecyclerAdapter(_mActivity, R.layout.recycler_item_gank, null);
    adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
    adapter.setOnRecyclerViewItemClickListener(this);
    adapter.setOnRecyclerViewItemChildClickListener(this);
    adapter.openLoadMore(20, true);
    adapter.setLoadingView(mLoadingView);
    adapter.setOnLoadMoreListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override public void onLoadMoreRequested() {
    if (!canLoadMore) {
      return;
    }
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
          .subscribe(new ProgressSubscriber<>(_mActivity, this::setResult, null));
    }
  }

  public void setResult(List<GankInfo> gankInfos) {
    adapter.setNewData(gankInfos);
    recyclerView.scrollToPosition(0);
  }

  @Override public void resetLoadingUi() {
    Logger.e(TAG, "restUi");
    stopRefresh();
  }
}
