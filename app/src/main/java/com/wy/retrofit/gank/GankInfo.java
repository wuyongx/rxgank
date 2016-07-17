package com.wy.retrofit.gank;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by wuyong on 16/7/14.
 */
public class GankInfo implements Parcelable {

  public String _id;
  public String createdAt;
  public String desc;
  public String content;
  public String title;
  public String publishedAt;
  public String source;
  public String type;
  public String url;
  public boolean used;
  public String who;
  public String meiziUrl;

  public boolean filter(String key) {
    String lowerKey = key.toLowerCase();
    return contains(lowerKey, desc, who, type, source, url,createdAt);
  }

  private boolean contains(String key, String... target) {
    if (target == null || target.length == 0) {
      return false;
    }
    boolean[] booleanArray = new boolean[target.length];
    for (int i = 0, n = target.length; i < n; i++) {
      String tar = target[i];
      if (TextUtils.isEmpty(tar)) {
        continue;
      }
      booleanArray[i] = tar.toLowerCase().contains(key);
    }
    boolean result = false;
    for (int i = 0, n = target.length; i < n; i++) {
      result = result || booleanArray[i];
    }
    return result;
  }

  public GankInfo() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this._id);
    dest.writeString(this.createdAt);
    dest.writeString(this.desc);
    dest.writeString(this.content);
    dest.writeString(this.title);
    dest.writeString(this.publishedAt);
    dest.writeString(this.source);
    dest.writeString(this.type);
    dest.writeString(this.url);
    dest.writeByte(this.used ? (byte) 1 : (byte) 0);
    dest.writeString(this.who);
    dest.writeString(this.meiziUrl);
  }

  protected GankInfo(Parcel in) {
    this._id = in.readString();
    this.createdAt = in.readString();
    this.desc = in.readString();
    this.content = in.readString();
    this.title = in.readString();
    this.publishedAt = in.readString();
    this.source = in.readString();
    this.type = in.readString();
    this.url = in.readString();
    this.used = in.readByte() != 0;
    this.who = in.readString();
    this.meiziUrl = in.readString();
  }

  public static final Creator<GankInfo> CREATOR = new Creator<GankInfo>() {
    @Override public GankInfo createFromParcel(Parcel source) {
      return new GankInfo(source);
    }

    @Override public GankInfo[] newArray(int size) {
      return new GankInfo[size];
    }
  };
}
