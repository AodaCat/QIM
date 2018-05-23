package cc.adcat.qim.ui.main.fragments.message;

import android.content.Context;

import java.util.List;

import cc.adcat.qim.App;
import cc.adcat.qim.bean.ChatBean;
import cc.adcat.qim.bean.sql.gen.ChatBeanDao;
import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import cc.adcat.qim.utils.Log;

public class MessagePresenter implements MessageContract.IPresenter{
    private static final String TAG = "MessagePresenter";
    private Context mContext;
    private MessageContract.IView mView;
    private ChatBeanDao mChatBeanDao;
    private IClient mClient;
    public MessagePresenter(Context context, MessageContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mChatBeanDao = App.getInstance().getDaoSession().getChatBeanDao();
        mClient = QIMClient.getInstance();
    }

    @Override
    public void loadChats(MessageCallback callback) {
//        mView.refresh();
        Log.d(TAG,mClient.getCurrentUser());
        String currentUser = mClient.getCurrentUser();
        String me = currentUser.substring(0,currentUser.indexOf('/'));
        List<ChatBean> results = mChatBeanDao.queryBuilder()
                                        .where(ChatBeanDao.Properties.To.eq(me))
                                        .orderDesc(ChatBeanDao.Properties.Time)
                                        .build()
                                        .list();
        callback.onLoadSuccess(results);
    }

    @Override
    public void deleteChat(ChatBean chatBean,MessageCallback callback) {
        mChatBeanDao.delete(chatBean);
        callback.onDeleteSuccess();
    }
}
