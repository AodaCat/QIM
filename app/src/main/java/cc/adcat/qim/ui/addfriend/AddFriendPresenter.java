package cc.adcat.qim.ui.addfriend;

import android.content.Context;
import android.text.TextUtils;

import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;

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
        mClient.find(username);
    }
}
