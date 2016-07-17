package com.wy.retrofit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.wy.retrofit.di.modules.FragmentModule;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends SupportFragment {
  protected static final String ARG_PARAM1 = "param1";
  protected static final String ARG_PARAM2 = "param2";
  protected String TAG;
  protected View mContentView;
  protected Unbinder unbinder;
  private FragmentModule mFragmentModule;

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    TAG = "Fragment:" + getClass().getCanonicalName();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // 避免多次从xml中加载布局文件
    if (mContentView == null) {
      initView(inflater, container, savedInstanceState);
      setListener();
      processLogic(savedInstanceState);
    } else {
      ViewGroup parent = (ViewGroup) mContentView.getParent();
      if (parent != null) {
        parent.removeView(mContentView);
      }
      //重新绑定
      unbinder = ButterKnife.bind(this, mContentView);
    }

    return mContentView;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (unbinder != null) {
      unbinder.unbind();
    }
  }

  protected void setContentView(@NonNull View contentView) {
    mContentView = contentView;
    unbinder = ButterKnife.bind(this, mContentView);
  }

  protected FragmentModule getFragmentModule() {
    if (mFragmentModule == null) {
      mFragmentModule = new FragmentModule(this);
    }
    return mFragmentModule;
  }

  /**
   * 初始化View控件
   */
  protected abstract void initView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState);

  /**
   * 给View控件添加事件监听器
   */
  protected abstract void setListener();

  /**
   * 处理业务逻辑，状态恢复等操作
   *
   * @param savedInstanceState 懒加载需要调用
   */
  protected abstract void processLogic(Bundle savedInstanceState);
}
