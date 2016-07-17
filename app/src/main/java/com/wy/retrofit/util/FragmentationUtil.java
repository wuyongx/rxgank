package com.wy.retrofit.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.List;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by wuyong on 16/7/17.
 */
public class FragmentationUtil {

  public static  SupportFragment getActiveFragment(SupportFragment parentFragment, FragmentManager fragmentManager) {
      List<Fragment> fragmentList = fragmentManager.getFragments();
      if (fragmentList == null) {
        return parentFragment;
      }
      for (int i = fragmentList.size() - 1; i >= 0; i--) {
        Fragment fragment = fragmentList.get(i);
        if (fragment instanceof SupportFragment) {
          SupportFragment supportFragment = (SupportFragment) fragment;
          if (!supportFragment.isHidden() && supportFragment.getUserVisibleHint()) {
            return getActiveFragment(supportFragment, supportFragment.getChildFragmentManager());
          }
        }
      }
      return parentFragment;
    }

}
