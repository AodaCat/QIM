package cc.adcat.qim.ui.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.packet.Message;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.adapter.ChatMessageAdapter;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.bean.ChatMessage;
import cc.adcat.qim.events.Event;
import cc.adcat.qim.global.Constant;
import cc.adcat.qim.utils.Log;
import cc.adcat.qim.utils.ToastUtil;

public class ChatActivity extends BaseActivity implements ChatContract.IView, View.OnClickListener, ChatCallback {
    private static final String TAG = "ChatActivity";
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_messages)
    RecyclerView rvMessages;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.btn_send)
    Button btnSend;
    private ChatContract.IPresenter mPresenter;
    private String user;
    private List<ChatMessage> messages;
    private ChatMessageAdapter mMessageAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void creatPresenter() {
        mPresenter = new ChatPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getIntent().getStringExtra("user");
        super.onCreate(savedInstanceState);
        String action = getIntent().getStringExtra(Constant.ACTION);
        if (TextUtils.equals(action,Constant.ACTION_NEW_MESSAGE)){
            EventBus.getDefault().post(new Event(Event.TYPE_CLEAR_UNREADMESSAGE));
        }
        EventBus.getDefault().register(this);
        Log.d(TAG,"user:"+user);
    }

    @Override
    protected void initViews() {
        super.initViews();
        tvTitle.setText(user);
        messages = new LinkedList<>();
        mMessageAdapter = new ChatMessageAdapter(this,user,messages);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(mMessageAdapter);
        btnBack.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.loadChatMessages(user,System.currentTimeMillis(),20,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void toast(String msg) {
        ToastUtil.showShort(this,msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        String msg = etMsg.getText().toString();
        mPresenter.send(user,msg,this);
        etMsg.setText("");
    }

    @Override
    public void onSuccess(ChatMessage message) {
        ToastUtil.showShort(this,"发送成功...");
        messages.add(message);
        mMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailed(Throwable e) {
        ToastUtil.showLong(this,e.getMessage());
    }

    @Override
    public void onLoadMsgComplete(List<ChatMessage> msgs) {
        messages.addAll(0,msgs);
        mMessageAdapter.notifyDataSetChanged();
//        rvMessages.scrollTo(0,0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100)
    public void onMessageRecivied(Message message){
        if (TextUtils.isEmpty(message.getBody())){
            return;
        }
        ChatMessage chatMessage = ChatMessage.parse(message);
        mPresenter.restore(chatMessage);
        String from = chatMessage.getFrom();
        if (TextUtils.equals(from,user)){//当前界面user==message发送者
            messages.add(chatMessage);
            mMessageAdapter.notifyItemChanged(messages.size()-1);

        }else {
            ToastUtil.showShort(this,"收到新消息...");
        }
        EventBus.getDefault().cancelEventDelivery(message) ;//优先级高的订阅者可以终止事件往下传递
    }
}
