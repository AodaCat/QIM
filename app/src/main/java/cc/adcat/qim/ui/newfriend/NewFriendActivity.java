package cc.adcat.qim.ui.newfriend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.utils.ToastUtil;

public class NewFriendActivity extends BaseActivity implements NewFriendContract.IView, View.OnClickListener, NewFriendCallback {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private AlertDialog mProgressDialog;
    private NewFriendContract.IPresenter mPresenter;
    String username,account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");
        account = getIntent().getStringExtra("account");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_new_friend;
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvAccount.setText(account);
        tvUsername.setText(username);
        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    protected void creatPresenter() {
        mPresenter = new NewFriendPresenter(this,this);
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
            case R.id.btn_add:
                addNewFriend();
                break;
        }
    }

    private void addNewFriend() {
        String user = tvAccount.getText().toString();
        mPresenter.addFriend(user,this);
    }

    @Override
    public void onAddFriendSuccess() {
        toast("发送请求成功...");
        finish();
    }

    @Override
    public void onAddFriendFailed(Throwable e) {
        toast("请求失败:"+e.getMessage());
    }
}
