package com.wy.retrofit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuyong on 16/7/16.
 */
public class RegularUtil {
  private static final String REG = "(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))";

  public static List<String> getUrl(String html) {
    List<String> urls =new ArrayList<>(10);
    Pattern p = Pattern.compile(REG);
    Matcher matcher = p.matcher(html);
    while (matcher.find()) {
      String url = matcher.group();
      urls.add(url);
    }
    return urls;
  }
}
