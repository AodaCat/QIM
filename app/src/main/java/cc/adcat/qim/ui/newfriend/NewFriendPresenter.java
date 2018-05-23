package cc.adcat.qim.ui.newfriend;

import android.content.Context;

import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NewFriendPresenter implements NewFriendContract.IPresenter{
    private Context mContext;
    private NewFriendContract.IView mView;
    private IClient mClient;

    public NewFriendPresenter(Context context, NewFriendContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
    }

    @Override
    public void addFriend(String username, NewFriendCallback callback) {
        mClient.addFriend(username)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.hideProgressBar();
                        callback.onAddFriendSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        callback.onAddFriendFailed(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
