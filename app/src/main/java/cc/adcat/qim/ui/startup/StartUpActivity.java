package cc.adcat.qim.ui.startup;

import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.ui.login.LoginActivity;
import cc.adcat.qim.ui.register.RegisterActivity;

public class StartUpActivity extends BaseActivity implements StartUpContract.IView, View.OnClickListener {


    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_start_up;
    }

    @Override
    protected void creatPresenter() {

    }

    @Override
    protected void initViews() {
        super.initViews();
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
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
        }
    }

    private void register() {
        launcherActivity(RegisterActivity.class);
    }

    private void login() {
        launcherActivity(LoginActivity.class);
    }
}
