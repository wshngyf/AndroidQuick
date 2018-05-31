package la.xiong.androidquick.demo.network.service;

import la.xiong.androidquick.demo.network.loader.HttpResult;
import la.xiong.androidquick.demo.network.loader.SuccessEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Edianzu on 2018/5/24.
 */

public interface LicensePlateService {

    @GET("addLicense")
    Observable<HttpResult<SuccessEntity>> submitLicense(@Query("province") String province, @Query("city") String city, @Query("carnum") String carnum, @Query("phoneNum") String phoneNum, @Query("remark") String remark, @Query("author") String author);


}
