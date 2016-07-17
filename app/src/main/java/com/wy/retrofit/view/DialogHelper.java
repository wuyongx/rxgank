package com.wy.retrofit.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

/**
 * Created by wuyong on 16/7/13.
 * 加载对话框帮助类
 */
public class DialogHelper {
  private Context mContext;
  private CustomProgressDialog progressDialog;

  @NonNull public CustomProgressDialog getProgressDialog() {
    return progressDialog;
  }

  public DialogHelper(Context context) {
    mContext = context;
  }

  public void startProgressDialog() {
    startProgressDialog(null);
  }

  public void startProgressDialog(String message) {
    if (progressDialog == null) {
      progressDialog = CustomProgressDialog.createDialog(mContext, message);
      progressDialog.setCanceledOnTouchOutside(false);
    }

    if (progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
    registerDismissListener();
    setDialogMessage(message).show();
  }

  private CustomProgressDialog setDialogMessage(String message) {
    return progressDialog.setMessage(message);
  }

  private void registerDismissListener() {
    if (progressDialog != null) {
      if (mContext instanceof DialogInterface.OnDismissListener) {
        progressDialog.setOnDismissListener((DialogInterface.OnDismissListener) mContext);
      }
    }
  }

  public void stopProgressDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.resetMessage();
      progressDialog.cancel();
    }
  }
}