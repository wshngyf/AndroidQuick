package la.xiong.androidquick.demo.network.loader;

/**
 * Created by fey on 2018/5/31.
 */

public class SuccessEntity extends HttpResult {
    public String licenseplate;

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }
}
