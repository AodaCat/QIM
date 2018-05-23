package cc.adcat.qim.ui.addfriend;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.ui.newfriend.NewFriendActivity;
import cc.adcat.qim.utils.ToastUtil;

public class AddFriendActivity extends BaseActivity implements AddFriendContract.IView, View.OnClickListener, AddFriendCallback {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.btn_find)
    Button btnFind;
    @BindView(R.id.rv_new_friends)
    RecyclerView rvNewFriends;
    private AddFriendContract.IPresenter mPresenter;
    private AlertDialog mProgressDialog;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void creatPresenter() {
        mPresenter = new AddFriendPresenter(this,this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        btnBack.setOnClickListener(this);
        btnFind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_find:
                find();
                break;
        }
    }

    private void find() {
        String username = etUsername.getText().toString();
        mPresenter.find(username,this);
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
    public void onFindUser(String username,String account) {
        Intent intent = new Intent(this, NewFriendActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("account",account);
        launcherActivity(intent);
    }
}
