package cc.adcat.qim.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.adcat.qim.App;
import cc.adcat.qim.R;
import cc.adcat.qim.bean.ChatBean;
import cc.adcat.qim.bean.ChatMessage;
import cc.adcat.qim.bean.sql.gen.ChatBeanDao;
import cc.adcat.qim.bean.sql.gen.ChatMessageDao;
import cc.adcat.qim.events.Event;
import cc.adcat.qim.global.Constant;
import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import cc.adcat.qim.ui.chat.ChatActivity;
import cc.adcat.qim.ui.main.MainActivity;
import cc.adcat.qim.utils.Log;

public class QIMService extends Service implements StanzaListener {
    private static final String TAG = "QIMService";
    private IClient mClient;
    private NotificationManager mNotificationManager;
    private Map<String,Integer> unReadMessages;
    private ChatMessageDao mChatMessageDao;
    private ChatBeanDao mChatBeanDao;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        unReadMessages = new HashMap<>();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mChatMessageDao = App.getInstance().getDaoSession().getChatMessageDao();
        mChatBeanDao = App.getInstance().getDaoSession().getChatBeanDao();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        mClient = QIMClient.getInstance();
        mClient.addAsyncStanzaListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        mClient.logout().subscribe();//退出登录
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void processPacket(Stanza packet) {
        Log.d(TAG,packet.toXML().toString());
        if (packet instanceof Presence){
            switch (((Presence) packet).getType()){
                case unsubscribed://拒绝好友
                    break;
                case error://包含错误
                    break;
                case available://在线
                    break;
                case probe://
                    break;
                case subscribe://添加好友申请
                    break;
                case subscribed://同意添加好友
                    break;
                case unavailable://离线
                    break;
                case unsubscribe://删除好友
                    break;
            }
        }
        if (packet instanceof Message){
            EventBus.getDefault().post(packet);
        }
        if (packet instanceof IQ){

        }
    }
    //收到消息时候会发出消息通过EventBus抛出message，
    //如果被ChatActivity接受并处理，那这里就不会被处理
    @Subscribe(threadMode = ThreadMode.MAIN,priority = 1)
    public void onMessageRecivied(Message message){
        if (TextUtils.isEmpty(message.getBody())){
            return;
        }
        ChatMessage chatMessage = ChatMessage.parse(message);
        Integer count = unReadMessages.get(chatMessage.getFrom());
        if (count == null){
            count = 0;
        }
        count++;
        unReadMessages.put(chatMessage.getFrom(),count);
        mChatMessageDao.insert(chatMessage);
        ChatBean chatBean = null;
        List<ChatBean> chatBeans = mChatBeanDao.queryBuilder()
                .where(ChatBeanDao.Properties.User.eq(chatMessage.getFrom()))
                .build()
                .list();
        if (chatBeans.size() == 1){
            chatBean = chatBeans.get(0);
        }else {
            chatBean = new ChatBean();
        }
        chatBean.setMessage(chatMessage.getBody());
        chatBean.setUser(chatMessage.getFrom());//这里是接收消息，自然而然from是消息发送者，即对方
        chatBean.setTime(chatMessage.getTime());
        chatBean.setTo(chatMessage.getTo());//这里是接收消息，自然而然to是消息接收者，即当前登陆用户
        int unread = chatBean.getUnReadCount();
        chatBean.setUnReadCount(unread+1);
        mChatBeanDao.insertOrReplace(chatBean);
        EventBus.getDefault().post(new Event(Event.TYPE_REFRESH_MESSAGE_LIST));
        int fromCount = unReadMessages.keySet().size();
        String user = "";
        for(String s:unReadMessages.keySet()){
            user = s;
        }
        int allCount = 0;
        for (Integer i : unReadMessages.values()) {
            allCount+=i;
        }
        Intent intent;
        String text = "";
        if (fromCount == 1){
            intent = new Intent(this,ChatActivity.class);
            intent.putExtra("user",user);
            intent.putExtra(Constant.ACTION,Constant.ACTION_NEW_MESSAGE);
            text = "收到"+allCount+"条新消息";
        }else {
            intent = new Intent(this,MainActivity.class);
            intent.putExtra(Constant.ACTION,Constant.ACTION_NEW_MESSAGE);
            text = fromCount+"个联系人发来"+allCount+"条新消息";
        }
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build();
        mNotificationManager.notify(0,notification);
    }

    @Subscribe
    public void onEvent(Event event){
        switch (event.getType()){
            case Event.TYPE_CLEAR_UNREADMESSAGE://清除未读消息
                unReadMessages.clear();
                mNotificationManager.cancel(0);
                break;
        }
    }
}
