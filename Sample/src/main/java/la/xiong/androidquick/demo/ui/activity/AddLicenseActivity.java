package la.xiong.androidquick.demo.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import la.xiong.androidquick.demo.R;
import la.xiong.androidquick.demo.base.BaseActivity;
import la.xiong.androidquick.demo.network.loader.HttpResult;
import la.xiong.androidquick.demo.network.loader.LicensePlateLoader;
import la.xiong.androidquick.demo.network.loader.SuccessEntity;
import la.xiong.androidquick.tool.AppUtil;
import la.xiong.androidquick.tool.DialogUtil;
import la.xiong.androidquick.tool.StrUtil;
import la.xiong.androidquick.tool.ToastUtil;
import la.xiong.androidquick.ui.dialog.CommonDialog;
import rx.Subscriber;
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
    @BindView(R.id.tv_clearremark)
    TextView mClearRemark;


    String mProvince;
    String mCity;
    Subscriber subscriber;
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
                String[] province = mContext.getResources().getStringArray(R.array.spinner_province);
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
                String[] city =  mContext.getResources().getStringArray(R.array.spinner_city);
                mCity=city[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCity="A";
            }
        });
        mClearRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEtRemark.getText().clear();

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
                DialogUtil.showLoadingDialog(mContext,"正在提交");

                String author=mEtAuthor.getText().toString().trim();
                String carnum=mEtCarnum.getText().toString().trim().toUpperCase();
                String phonenum=mEtPhoneNum.getText().toString().trim();
                String remark=mEtRemark.getText().toString().trim();
                if(StrUtil.isEmpty(author)||StrUtil.isEmpty(carnum)||StrUtil.isEmpty(phonenum)){
                    DialogUtil.dismissLoadingDialog(mContext);

                    ToastUtil.showToast("车牌、手机号、采集人必填，备注可以不填");
                    return;
                }
                if(carnum.length()!=5){
                    DialogUtil.dismissLoadingDialog(mContext);

                    ToastUtil.showToast("车牌不够5位");
                    return;
                }
                if(phonenum.length()!=11){
                    DialogUtil.dismissLoadingDialog(mContext);

                    ToastUtil.showToast("手机号不够11位");
                    return;
                }
                mAppLoader.submitLicense(new Subscriber<SuccessEntity>(){
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.dismissLoadingDialog(mContext);
                        ToastUtil.showToast(e.getMessage());
                        mEtCarnum.getText().clear();
                        mEtPhoneNum.getText().clear();
                    }

                    @Override
                    public void onNext(SuccessEntity successEntity) {
                        mEtCarnum.getText().clear();
                        mEtPhoneNum.getText().clear();
                        DialogUtil.dismissLoadingDialog(mContext);
                        ToastUtil.showToast(successEntity.getLicenseplate()+"-添加成功");
                    }
                },mProvince,mCity,carnum,phonenum,remark,author);

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
