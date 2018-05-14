package cc.adcat.qim.ui.chat;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import cc.adcat.qim.App;
import cc.adcat.qim.bean.ChatMessage;
import cc.adcat.qim.bean.sql.gen.ChatMessageDao;
import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatPresenter implements ChatContract.IPresenter{
    private Context mContext;
    private ChatContract.IView mView;
    private IClient mClient;
    private ChatMessageDao mChatMessageDao;
    public ChatPresenter(Context context, ChatContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
        mChatMessageDao = App.getInstance().getDaoSession().getChatMessageDao();
    }

    @Override
    public void loadChatMessages(String user, long time, int limit, ChatCallback callback) {
        List<ChatMessage> results = mChatMessageDao.queryBuilder()
                .whereOr(ChatMessageDao.Properties.From.eq(user),ChatMessageDao.Properties.To.eq(user))
                .where(ChatMessageDao.Properties.Time.lt(time))
//                .limit(limit)
                .orderAsc(ChatMessageDao.Properties.Id)
                .build()
                .list();
        if (callback != null){
            callback.onLoadMsgComplete(results);
        }
    }

    @Override
    public void send(String user, String msg, ChatCallback callback) {
        if (TextUtils.isEmpty(msg)){
            mView.toast("消息不能为空....");
            return;
        }
        mClient.send(user,msg)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean && callback != null){
                            String from = mClient.getCurrentUser();
                            if (from.contains("/")){
                                from = from.substring(0,from.indexOf('/'));
                            }
                            String to = user;
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setFrom(from);
                            chatMessage.setTo(to);
                            chatMessage.setType("chat");
                            chatMessage.setTime(System.currentTimeMillis());
                            chatMessage.setBody(msg);
                            mChatMessageDao.insert(chatMessage);
                            callback.onSuccess(chatMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null){
                            callback.onFailed(e);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void restore(ChatMessage message) {
        mChatMessageDao.insert(message);
    }
}
