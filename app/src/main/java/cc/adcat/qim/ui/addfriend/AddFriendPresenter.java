package cc.adcat.qim.ui.addfriend;

import android.content.Context;
import android.text.TextUtils;

import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddFriendPresenter implements AddFriendContract.IPresenter{
    private Context mContext;
    private AddFriendContract.IView mView;
    private IClient mClient;
    public AddFriendPresenter(Context context, AddFriendContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
    }

    @Override
    public void find(String username, AddFriendCallback callback) {
        if (TextUtils.isEmpty(username)){
            mView.toast("用户名不能为空...");
            return;
        }
        mView.showProgressBar("正在搜索");
        mClient.findUser(username)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        mView.hideProgressBar();
                        callback.onFindUser(username,s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        mView.toast("出错了....");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
