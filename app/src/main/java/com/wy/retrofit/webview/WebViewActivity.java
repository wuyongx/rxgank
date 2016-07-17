package com.wy.retrofit.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wy.retrofit.R;

public class WebViewActivity extends AppCompatActivity {

  @BindView(R.id.web_view) WebView webView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web_view);
    ButterKnife.bind(this);

    loadWebView();
  }

  @SuppressLint("SetJavaScriptEnabled") private void loadWebView() {
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setJavaScriptCanOpenWindowsAutomatically(true);

    webView.loadUrl("file:///android_asset/video_js.html");
  }
}
