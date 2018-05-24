package la.xiong.androidquick.network;

import android.content.Context;

import la.xiong.androidquick.BuildConfig;
import la.xiong.androidquick.tool.LogUtil;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static Retrofit mRetrofit;
    private static OkHttpClient okHttpClient = null;


    private void init() {
        initOkHttp();
    }

    public RetrofitManager() {
        init();
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
            //配置SSL证书检测
            builder.sslSocketFactory(SSLSocketClient.getNoSSLSocketFactory());
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        }
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
        LogUtil.i(TAG, "initOkHttp:getNoSSLSocketFactory");

        if (mRetrofit == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(ApiConfig.API_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())//定义转化器,用Gson将服务器返回的Json格式解析成实体
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//关联Rxjava
                            .build();

                }
            }
        }
    }


    private static class SingletonHolder{
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

}
