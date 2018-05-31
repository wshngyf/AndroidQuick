package la.xiong.androidquick.demo.network.loader;

import la.xiong.androidquick.demo.network.exception.ApiException;
import rx.functions.Func1;


/**
 * Created by fey on 2018/5/31.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.getResultCode() != 0) {
            throw new ApiException(httpResult.getResultCode());
        }
        return httpResult.getData();
    }


}
