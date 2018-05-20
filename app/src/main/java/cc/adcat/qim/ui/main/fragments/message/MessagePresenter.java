package cc.adcat.qim.ui.main.fragments.message;

import android.content.Context;

import java.util.List;

import cc.adcat.qim.App;
import cc.adcat.qim.bean.ChatBean;
import cc.adcat.qim.bean.sql.gen.ChatBeanDao;

public class MessagePresenter implements MessageContract.IPresenter{
    private Context mContext;
    private MessageContract.IView mView;
    private ChatBeanDao mChatBeanDao;

    public MessagePresenter(Context context, MessageContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mChatBeanDao = App.getInstance().getDaoSession().getChatBeanDao();
    }

    @Override
    public void loadChats(MessageCallback callback) {
//        mView.refresh();
        List<ChatBean> results = mChatBeanDao.queryBuilder()
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
