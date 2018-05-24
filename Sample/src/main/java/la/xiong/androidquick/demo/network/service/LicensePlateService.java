package la.xiong.androidquick.demo.network.service;

import java.util.List;

import la.xiong.androidquick.demo.network.GankRes;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Edianzu on 2018/5/24.
 */

public interface LicensePlateService {

    @GET("addLicense")
    Observable<String> submitLicense(@Query("province") String province,@Query("city") String city,@Query("carnum") String carnum,@Query("phoneNum") String phoneNum,@Query("remark") String remark,@Query("author") String author);


}
