package com.wy.retrofit.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import com.wy.retrofit.App;
import com.wy.retrofit.R;

/**
 * Created by wuyong on 16/7/13.
 */
public class CustomProgressDialog extends AppCompatDialog {

  private TextView mContent;
  public static final String MSG = App.getApp().getString(R.string.msg_loading);

  public CustomProgressDialog(Context context, int theme) {
    super(context, theme);
  }

  public static CustomProgressDialog createDialog(Context context, @Nullable String message) {
    CustomProgressDialog dialog = new CustomProgressDialog(context, 0);
    dialog.setContentView(R.layout.loading_dialog_view);
    dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    dialog.setCanceledOnTouchOutside(false);
    if (!TextUtils.isEmpty(message)) {
      dialog.setMessage(message);
    } else {
      dialog.setMessage(MSG);
    }
    return dialog;
  }

  /**
   * setMessage 提示内容
   *
   * @param msg 消息内容
   */
  public CustomProgressDialog setMessage(@NonNull String msg) {
    if (mContent == null) {
      mContent = (TextView) findViewById(R.id.tv_loading_msg);
    }
    mContent.setText(msg);
    return this;
  }

  public CustomProgressDialog resetMessage() {
    setMessage(MSG);
    return this;
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (isShowing()) {
      cancel();
    }
  }
}
