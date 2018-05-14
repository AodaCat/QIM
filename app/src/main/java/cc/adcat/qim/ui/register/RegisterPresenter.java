package cc.adcat.qim.ui.register;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import cc.adcat.qim.services.QIMService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterPresenter implements RegisterContract.IPresenter{
    private Context mContext;
    private RegisterContract.IView mView;
    private IClient mClient;
    public RegisterPresenter(Context context, RegisterContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
    }

    @Override
    public void register(String username, String password, String rePassword, final RegisterCallback callback) {
        if (TextUtils.isEmpty(username)){
            mView.toast("用户名不能为空...");
            return;
        }
        if (TextUtils.isEmpty(password)){
            mView.toast("密码不能为空...");
            return;
        }
        if (TextUtils.isEmpty(rePassword)){
            mView.toast("第二次输入密码不能为空...");
            return;
        }
        if (!TextUtils.equals(password,rePassword)){
            mView.toast("两次输入密码不一致...");
            return;
        }
        mView.showProgressBar("正在注册...");
        mClient.register(username,password)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean && callback != null){
                            callback.onSuccess();
                            Intent intent = new Intent(mContext, QIMService.class);
                            mContext.startService(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        if (callback != null){
                            callback.onFailed(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgressBar();
                    }
                });
    }
}
