package la.xiong.androidquick.demo.network.loader;


import la.xiong.androidquick.demo.network.exception.ApiException;
import la.xiong.androidquick.demo.network.service.LicensePlateService;
import la.xiong.androidquick.network.RetrofitManager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Edianzu on 2018/5/24.
 */

public class LicensePlateLoader {
    private LicensePlateService mLicensePlateService;
    public LicensePlateLoader(){
        mLicensePlateService = RetrofitManager.getInstance().create(LicensePlateService.class);
    }

    public void submitLicense(Subscriber<SuccessEntity> subscriber, String province, String city, String carnum, String phoneNum, String remark, String author){
       mLicensePlateService.submitLicense(province,city,carnum,phoneNum,remark,author)
               .map(new HttpResultFunc<SuccessEntity>())
               .subscribeOn(Schedulers.io())
               .unsubscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(subscriber);
    }


}
