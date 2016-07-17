package com.wy.retrofit.util;

import com.wy.retrofit.kjhttp.API;
import com.wy.retrofit.kjhttp.request.ReqUpload;
import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by wuyong on 16/4/21.
 *
 * //上传文件
 * File file =
 * new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
 * "avatar.jpg");
 * if (!file.exists()) {
 * return;
 * }
 *
 * ReqUpload upload = new ReqUpload();
 * upload.app = "User";
 * upload.clazz = "Uploadlotphoto";
 * upload.files.add(file);
 * upload.files.add(file);
 *
 * Observable.defer(() -> Observable.just(upload))
 * .subscribeOn(Schedulers.io())
 * .map(UploadUtil::genUploadRequestBody)
 * .flatMap(map -> kjService.fileUpload(map))
 * .observeOn(AndroidSchedulers.mainThread())
 * .subscribe(response -> L.e(TAG, response.data),
 * throwable -> L.e(TAG, throwable.getMessage()), () -> L.e(TAG, "上传完成"));
 */
public class UploadUtil {

  public static Map<String, RequestBody> genUploadRequestBody(ReqUpload req) {
    L.e("UploadUtil", Thread.currentThread().getName());

    Map<String, RequestBody> map = new IdentityHashMap<>();
    makeRequestBody(req, map);
    return map;
  }

  private static void makeRequestBody(ReqUpload req, Map<String, RequestBody> map) {
    RequestBody app = RequestBody.create(MediaType.parse("text/plain"), req.app);
    RequestBody clazz = RequestBody.create(MediaType.parse("text/plain"), req.clazz);
    String md5 = Util.encryptMD5(req.app + req.clazz + API.MIYAO);
    RequestBody sign = RequestBody.create(MediaType.parse("text/plain"), md5);

    map.put("app", app);
    map.put("class", clazz);
    map.put("sign", sign);

    ArrayList<File> files = req.files;
    if (files == null || files.isEmpty()) {
      return;
    }
    int i = 0;
    for (File f : files) {
      if (!f.exists()) {
        continue;
      }
      i++;
      RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), f);
      map.put("image" + (i) + "\"; filename=\"" + f.getName(), fileBody);
    }
  }
}
