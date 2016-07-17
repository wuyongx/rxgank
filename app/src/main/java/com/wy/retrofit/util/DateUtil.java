package com.wy.retrofit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wuyong on 16/7/16.
 */
public class DateUtil {
  private static SimpleDateFormat format =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
  private static SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日 HH:mm yyyy", Locale.CHINA);

  public static String getDate(String dateSrc) {
    try {
      Date date = format.parse(dateSrc);
      return format2.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }
}
