package cc.adcat.qim.ui.register;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import cc.adcat.qim.ActivityManager;
import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.ui.main.MainActivity;
import cc.adcat.qim.utils.ToastUtil;

public class RegisterActivity extends BaseActivity implements RegisterContract.IView, View.OnClickListener, RegisterCallback {
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_re_password)
    EditText etRePassword;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    private AlertDialog mProgressDialog;//进度条
    private RegisterContract.IPresenter mPresenter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void creatPresenter() {
        mPresenter = new RegisterPresenter(this,this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void showProgressBar(String msg) {
        if (mProgressDialog == null){
            mProgressDialog = new AlertDialog.Builder(this)
                    .setTitle(msg)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
        }
//        if (mProgressDialog.isShowing()){
//            mProgressDialog.dismiss();
//        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void toast(String msg) {
        ToastUtil.showLong(this,msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();
        mPresenter.register(username,password,rePassword,this);
    }

    @Override
    public void onSuccess() {
        launcherActivity(MainActivity.class);
        ActivityManager.getInstance().finishAll();
    }

    @Override
    public void onFailed(Throwable e) {
        ToastUtil.showLong(this,e.getMessage());
    }
}
