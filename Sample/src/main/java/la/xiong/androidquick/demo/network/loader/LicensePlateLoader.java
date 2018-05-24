package la.xiong.androidquick.demo.network.loader;

import android.util.Log;

import la.xiong.androidquick.demo.network.base.ObjectLoader;
import la.xiong.androidquick.demo.network.service.LicensePlateService;
import la.xiong.androidquick.network.RetrofitManager;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Edianzu on 2018/5/24.
 */

public class LicensePlateLoader extends ObjectLoader {
    private LicensePlateService mLicensePlateService;
    public LicensePlateLoader(){
        mLicensePlateService = RetrofitManager.getInstance().create(LicensePlateService.class);
    }

    public Observable<String> submitLicense(String province, String city, String carnum, String phoneNum, String remark, String author){
        return observe(mLicensePlateService.submitLicense(province,city,carnum,phoneNum,remark,author)).map(new Func1<String,String>() {
            @Override
            public String call(String s) {
                Log.d("LicensePlateLoader","call="+s);
                return s;
            }
        });
    }


}
