package com.wy.retrofit.kjhttp.rxjava;

import android.support.annotation.NonNull;
import com.wy.retrofit.App;
import com.wy.retrofit.R;
import com.wy.retrofit.gank.Beatuty;
import com.wy.retrofit.gank.GankInfo;
import com.wy.retrofit.kjhttp.ApiService;
import com.wy.retrofit.kjhttp.ParamAdapter;
import com.wy.retrofit.kjhttp.request.ReqTeacherLifeCircle;
import com.wy.retrofit.kjhttp.response.RespArrayWrapper;
import com.wy.retrofit.kjhttp.response.TeacherLifeCircleInfo;
import com.wy.retrofit.kjhttp.response.Wrapper;
import com.wy.retrofit.util.RegularUtil;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wuyong on 16/4/18.
 * <p>
 * 封装Retrofit 请求
 */
@Singleton public class RetrofitClient {
  private static final String TAG = RetrofitClient.class.getName();
  private ApiService mService;

  @Inject public RetrofitClient(ApiService service) {
    mService = service;
  }

  public static final Observable.Transformer schedulersTransformer =
      new Observable.Transformer<Observable, Observable>() {
        @Override public Observable<Observable> call(Observable<Observable> observable) {
          return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
      };

  /**
   * 应用 Schedulers .方便 compose() 简化代码
   */
  @SuppressWarnings("unchecked") public static <T> Observable.Transformer<T, T> applySchedulers() {
    return (Observable.Transformer<T, T>) schedulersTransformer;
  }

  @NonNull public static Observable<Map<String, String>> createParam(Object object) {
    return getParamObservable(object);
  }

  /**
   * 获取 Map 类型的参数 :参数为对象
   */
  @NonNull public static Observable<Map<String, String>> getParamObservable(final Object object) {
    return Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
      @Override public void call(Subscriber<? super Map<String, String>> subscriber) {
        if (!subscriber.isUnsubscribed()) {
          try {
            Map<String, String> map;
            if (object == null) {
              map = new TreeMap<String, String>();
            } else {
              map = ParamAdapter.convert(object);
            }
            subscriber.onNext(map);
            subscriber.onCompleted();
          } catch (Throwable e) {
            subscriber.onError(e);
          }
        }
      }
    });
  }

  /**
   * 用来处理Http的response ,将HttpResponse的Data部分剥离出来返回给subscriber
   *
   * @param <T> Subscriber真正需要的数据类型，Data数据类型
   */
  private static class GetResponseData<T> implements Func1<Wrapper<T>, T> {
    @Override public T call(Wrapper<T> response) {
      if (!response.success()) {
        throw new NetWorkException(response.msg);
      }
      return response.data;
    }
  }

  private static class GetRespArrayData<T>
      implements Func1<RespArrayWrapper<T>, RespArrayWrapper<T>> {
    @Override public RespArrayWrapper<T> call(RespArrayWrapper<T> response) {
      if (response.isEmpty()) {
        throw new DataException(App.getApp().getString(R.string.msg_no_more_data));
      }
      return response;
    }
  }

/*  public static class GetArrayResponseData<T> implements Func1<ArrayWrapper<T>, List<T>> {

    @Override public List<T> call(ArrayWrapper<T> response) {
      //状态非1 或者 数据为空
      if (!response.succeed()) {
        throw new DataException(response.msg);
      }
      return response.data;
    }
  }*/

/*  private static class GetPageResponseData<T> implements Func1<Wrapper<PageWrapper<T>>, List<T>> {
    @Override public List<T> call(Wrapper<PageWrapper<T>> response) {
      if (!response.succeed()) {//返回0,非1
        throw new DataException(response.msg);
      }
      return response.data.rows;
    }
  }*/

  public void getTeacherLifeCircle(ReqTeacherLifeCircle req,
      Subscriber<RespArrayWrapper<TeacherLifeCircleInfo>> subscriber) {
    createParam(req).flatMap(map -> mService.getTeacherLifeCircle2(map))
        .map(new GetResponseData<>())
        .map(new GetRespArrayData<>())
        .compose(applySchedulers())
        .subscribe(subscriber);
  }

  public void getBeauties(int page, Subscriber<List<Beatuty>> subscriber) {
    mService.getBeauties(10, page).map(response -> {
      if (response.success()) {
        return response.results;
      } else {
        throw new DataException(App.getApp().getString(R.string.msg_no_more_data));
      }
    }).compose(applySchedulers()).subscribe(subscriber);
  }

  public void getGankList(String type, int page, Subscriber<List<GankInfo>> subscriber) {
    mService.getGankList(type, page).map(response -> {
      if (response.success()) {
        return response.results;
      } else {
        throw new DataException(App.getApp().getString(R.string.msg_no_more_data));
      }
    }).compose(applySchedulers()).subscribe(subscriber);
  }

  public Observable<List<GankInfo>> searchGank(String type, String key) {
    return mService.searchGank(type).subscribeOn(Schedulers.io()).map(response -> {
      if (response.success()) {
        return response.results;
      } else {
        throw new DataException(App.getApp().getString(R.string.msg_no_data));
      }
    }).flatMap(Observable::from)
        .filter(gank -> gank.filter(key))
        .toList()
        .map(list -> {
      if (list.isEmpty()) {
        throw new DataException(App.getApp().getString(R.string.msg_no_data));
      } else {
        return list;
      }
    });
  }

  public void getMeizi(Subscriber<String> subscriber) {
    mService.getGankDate()
        .map(response -> {
          if (response.success()) {
            return response.results.get(0);
          } else {
            throw new DataException(App.getApp().getString(R.string.msg_no_data));
          }
        })
        .flatMap(date -> {
          String[] str = date.split("-");
          return mService.getGankMeizi(str[0], str[1], str[2]);
        })
        .map(response -> {
          if (response.success()) {
            return response.results.get(0);
          } else {
            throw new DataException(App.getApp().getString(R.string.msg_no_data));
          }
        })
        .flatMap(gank -> Observable.from(RegularUtil.getUrl(gank.content)))
        .filter(url -> url.endsWith(".jpg") || url.endsWith(".gif"))
        .first()
        .compose(applySchedulers())
        .subscribe(subscriber);
  }
}
