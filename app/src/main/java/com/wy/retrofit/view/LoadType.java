package com.wy.retrofit.view;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE) @IntDef({ LoadType.REFRESH, LoadType.LOAD_MORE })
public @interface LoadType {
  int REFRESH = 0x006;
  int LOAD_MORE = 0x009;
}