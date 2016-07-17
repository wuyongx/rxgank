package com.wy.retrofit.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import com.wy.retrofit.App;

/**
 * Created by wuyong on 16/1/6. 资源工具类
 */
public class ResourceUtil {
  public static Drawable getResourceDrawable(Context context, @DrawableRes int drawableId) {
    return ResourcesCompat.getDrawable(context.getResources(), drawableId, context.getTheme());
  }

  public static int dpToPx(float dpValue) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
        Resources.getSystem().getDisplayMetrics());
  }

  public static int getColor(@ColorRes int colorRes) {
    App app = App.getApp();
    return ResourcesCompat.getColor(app.getResources(), colorRes, app.getTheme());
  }

  public static Uri getDrawableUri(@DrawableRes int resId) {
    App app = App.getApp();
    Resources r = app.getResources();
    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
        + "://"
        + r.getResourcePackageName(resId)
        + "/"
        + r.getResourceTypeName(resId)
        + "/"
        + r.getResourceEntryName(resId));
  }
}
