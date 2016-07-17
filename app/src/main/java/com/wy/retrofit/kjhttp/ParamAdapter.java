package com.wy.retrofit.kjhttp;

import android.support.annotation.NonNull;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * 通过反射把请求的实体类的字段名和值转化为Map<String, String>
 *
 * @author wuyong
 */
public class ParamAdapter {
  private static final String TAG = ParamAdapter.class.getCanonicalName();

  /**
   * @param obj 要转换的对象
   * @return 此对象的 key,value map
   */
  public static synchronized Map<String, String> convert(@NonNull Object obj)
      throws IllegalAccessException {
    Log.e(TAG, Thread.currentThread().getName());

    Map<String, String> params = new TreeMap<>();//保证字段顺序

    Class<?> clazz = obj.getClass();
    //自己的Field
    Field[] fields = clazz.getFields();
    boolean nullField = (fields == null || fields.length == 0);
    if (nullField) {
      return params;
    }

    String app = null;
    String _class = null;

    for (Field field : fields) {
      //field.setAccessible(true);
      //  得到字段名
      String fieldName = field.getName();
      // 得到字段值
      String fieldValue = String.valueOf(field.get(obj));

       /*特殊字段处理*/
      if (fieldName.startsWith("$")) {
        continue;
      }
      if (fieldValue == null) {
        continue;
      }
      if (fieldName.equals("app")) {
        app = fieldValue;
      }
      if (fieldName.equals("clazz")) {
        fieldName = "class";
        _class = fieldValue;
      }
      if (fieldName.equals("sign")) {
        fieldValue=SignPool.getInstance().sign(app,_class);
      }
      params.put(fieldName, fieldValue);
      Log.d(TAG, fieldName + " : " + fieldValue);
    }
    return params;
  }
}
