package la.xiong.androidquick.demo.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import la.xiong.androidquick.demo.R;
import la.xiong.androidquick.demo.base.BaseActivity;
import la.xiong.androidquick.demo.network.loader.LicensePlateLoader;
import la.xiong.androidquick.tool.AppUtil;
import la.xiong.androidquick.tool.DialogUtil;
import la.xiong.androidquick.tool.StrUtil;
import la.xiong.androidquick.tool.ToastUtil;
import la.xiong.androidquick.ui.dialog.CommonDialog;
import rx.functions.Action1;

/**
 * Created by Edianzu on 2018/5/24.
 */

public class AddLicenseActivity extends BaseActivity {
    @BindView(R.id.et_addlicence_phonenum)
    EditText mEtPhoneNum;
    @BindView(R.id.et_addlicense_remark)
    EditText mEtRemark;
    @BindView(R.id.et_addlicense_author)
    EditText mEtAuthor;
    @BindView(R.id.et_addlicence_carnum)
    EditText mEtCarnum;
    @BindView(R.id.sp_addlicense_city)
    Spinner mSpCity;
    @BindView(R.id.sp_addlicense_province)
    Spinner mSpProvince;

    String mProvince;
    String mCity;

    private LicensePlateLoader mAppLoader;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_addlicense;
    }

    @Override
    protected void initViewsAndEvents() {
        mAppLoader=new LicensePlateLoader();
        mSpProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] province = getResources().getStringArray(R.array.spinner_province);
                mProvince=province[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mProvince="豫";
            }
        });

        mSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] city = getResources().getStringArray(R.array.spinner_city);
                mCity=city[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCity="A";
            }
        });

        mEtCarnum.setTransformationMethod(new AllCapTransformationMethod(true));

    }

    @OnClick({R.id.bt_addlicense_submit,R.id.et_addlicense_author,R.id.et_addlicense_remark,R.id.et_addlicence_phonenum})
    public void onClick(View v){
        if(AppUtil.isFastClick()){
            return;
        }
        switch (v.getId()){
            case R.id.bt_addlicense_submit:
                DialogUtil.showLoadingDialog(this,"正在提交");
                String author=mEtAuthor.getText().toString().trim();
                String carnum=mEtCarnum.getText().toString().trim().toUpperCase();
                String phonenum=mEtPhoneNum.getText().toString().trim();
                String remark=mEtRemark.getText().toString().trim();
                if(StrUtil.isEmpty(author)||StrUtil.isEmpty(carnum)||StrUtil.isEmpty(phonenum)){
                    ToastUtil.showToast("车牌、手机号、采集人必填，备注可以不填");
                    return;
                }

                mAppLoader.submitLicense(mProvince,mCity,carnum,phonenum,remark,author)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mEtCarnum.getText().clear();
                        mEtPhoneNum.getText().clear();
                        mEtRemark.getText().clear();
                        DialogUtil.dismissLoadingDialog(AddLicenseActivity.this);
                        ToastUtil.showToast("添加成功");

                        Log.d("成功获取", "s=" + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        DialogUtil.dismissLoadingDialog(AddLicenseActivity.this);

                        // TODO: 2018/5/24 报异常 
                        ToastUtil.showToast("添加成功");
                        mEtCarnum.getText().clear();
                        mEtPhoneNum.getText().clear();
                        mEtRemark.getText().clear();
                        Log.d("throwable","error="+throwable);
                    }
                });
                break;
        }
    }

    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        private boolean allUpper = false;

        public AllCapTransformationMethod(boolean needUpper) {
            this.allUpper = needUpper;
        }

        @Override
        protected char[] getOriginal() {
            if (allUpper) {
                return lower;
            } else {
                return upper;
            }
        }

        @Override
        protected char[] getReplacement() {
            if (allUpper) {
                return upper;
            } else {
                return lower;
            }
        }
    }
}
