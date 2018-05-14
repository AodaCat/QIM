package cc.adcat.qim.ui.login;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import cc.adcat.qim.ActivityManager;
import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.ui.main.MainActivity;
import cc.adcat.qim.ui.register.RegisterActivity;
import cc.adcat.qim.utils.ToastUtil;

public class LoginActivity extends BaseActivity implements LoginContract.IView, View.OnClickListener, LoginCallback {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_forget_pwd)
    Button btnForgetPwd;
    private AlertDialog mProgressDialog;//进度条
    private LoginContract.IPersenter mPerseenter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        super.initViews();
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgetPwd.setOnClickListener(this);
    }

    @Override
    protected void creatPresenter() {
        mPerseenter = new LoginPresenter(this,this);
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
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_forget_pwd:
                forgetPwd();
                break;
        }
    }

    private void forgetPwd() {
        ToastUtil.showShort(this,"未完成");
    }

    private void register() {
        launcherActivity(RegisterActivity.class);
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        mPerseenter.login(username,password,this);
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
