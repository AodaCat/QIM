package cc.adcat.qim.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import cc.adcat.qim.services.QIMService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginPresenter implements LoginContract.IPersenter{
    private Context mContext;
    private LoginContract.IView mView;
    private IClient mClient;
    public LoginPresenter(Context context, LoginContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
    }

    @Override
    public void login(String username, String password, final LoginCallback callback) {
        if (TextUtils.isEmpty(username)){
            mView.toast("用户名不能为空...");
            return;
        }
        if (TextUtils.isEmpty(password)){
            mView.toast("密码不能为空...");
            return;
        }
        mView.showProgressBar("正在登录...");
        mClient.login(username,password)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            Intent intent = new Intent(mContext, QIMService.class);
                            mContext.startService(intent);
                            callback.onSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        callback.onFailed(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgressBar();
                    }
                });
    }
}
