package com.wy.retrofit.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DataKeeper extends Fragment {

  private static final String FRAG_TAG = DataKeeper.class.getCanonicalName();
  private Object retainedData;

  public static <ParentFrag extends Fragment & Keeper> DataKeeper attach(ParentFrag parent) {
    return attach(parent.getChildFragmentManager());
  }

  public static <ParentActivity extends AppCompatActivity & Keeper> DataKeeper attach(
      ParentActivity parent) {
    return attach(parent.getSupportFragmentManager());
  }

  private static DataKeeper attach(FragmentManager fragmentManager) {
    DataKeeper frag = (DataKeeper) fragmentManager.findFragmentByTag(FRAG_TAG);
    if (frag == null) {
      frag = new DataKeeper();
      fragmentManager.beginTransaction().add(frag, FRAG_TAG).commit();
    }
    return frag;
  }

  public Object getRetainedData() {
    return retainedData;
  }

  public void setRetainedData(Object retainedData) {
    this.retainedData = retainedData;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  public Keeper getParent() {
    Fragment parentFragment = getParentFragment();
    if (parentFragment instanceof Keeper) {
      return (Keeper) parentFragment;
    } else {
      Activity activity = getActivity();
      if (activity instanceof Keeper) {
        return (Keeper) activity;
      }
    }
    return null;
  }

  public interface Keeper {
  }
}