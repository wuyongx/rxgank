package com.wy.retrofit.config;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.wy.retrofit.util.FileUtil;

public class GlideConfiguration implements GlideModule {

  @Override public void applyOptions(final Context context, GlideBuilder builder) {
    // Apply options to the builder here.
    builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    if (FileUtil.hasExternalStorage()) {
      builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", 50 * 1024 * 1024));
    }
  }

  @Override public void registerComponents(Context context, Glide glide) {
    // register ModelLoaders here.
  }
}