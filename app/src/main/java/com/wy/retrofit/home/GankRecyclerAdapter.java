package com.wy.retrofit.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wy.retrofit.R;
import com.wy.retrofit.gank.GankInfo;
import com.wy.retrofit.util.DateUtil;
import java.util.List;

/**
 * Created by wuyong on 16/7/16.
 */
public class GankRecyclerAdapter extends BaseQuickAdapter<GankInfo> {
  private Context mContext;
  private final ColorGenerator colorGenerator;

  public GankRecyclerAdapter(Context context, int layoutResId, List<GankInfo> data) {
    super(layoutResId, data);
    mContext = context;
    colorGenerator = ColorGenerator.MATERIAL;
  }

  @Override protected void convert(BaseViewHolder holder, GankInfo info) {
    holder.setText(R.id.tv_type, info.type);
    holder.setText(R.id.tv_date, DateUtil.getDate(info.createdAt));
    holder.setText(R.id.tv_content, info.desc);
    holder.setText(R.id.tv_source, info.who);
    ImageView headView = holder.getView(R.id.iv_header);
    View dividerHorizontal = holder.getView(R.id.divider_horizontal);

    TextDrawable.IBuilder builder =
        TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();

    String headStr = info.who;
    if (TextUtils.isEmpty(info.who)) {
      headStr = info.type;
    }

    int color = colorGenerator.getColor(headStr);
    TextDrawable headDrawable = builder.build(headStr.substring(0, 1), color);

    headView.setImageDrawable(headDrawable);

    ImageView meiziView = holder.getView(R.id.iv_meizi);
    if (info.url.endsWith(".jpg") || info.url.endsWith(".gif")) {
      info.meiziUrl = info.url;
    }
    boolean meizi = !TextUtils.isEmpty(info.meiziUrl);
    meiziView.setVisibility(meizi ? View.VISIBLE : View.GONE);
    dividerHorizontal.setVisibility(meizi ? View.GONE : View.VISIBLE);
    Glide.with(mContext)
        .load(info.meiziUrl)
        .centerCrop()
        .crossFade()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(meiziView);

    //子 View 监听器

    holder.setOnClickListener(R.id.iv_header, new OnItemChildClickListener());
    holder.setOnClickListener(R.id.tv_type, new OnItemChildClickListener());
    holder.setOnClickListener(R.id.tv_source, new OnItemChildClickListener());
    holder.setOnClickListener(R.id.tv_date, new OnItemChildClickListener());
    holder.setOnClickListener(R.id.iv_share, new OnItemChildClickListener());
  }
}
