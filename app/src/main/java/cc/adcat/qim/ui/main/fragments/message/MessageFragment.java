package cc.adcat.qim.ui.main.fragments.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.adapter.ChatBeanAdapter;
import cc.adcat.qim.base.BaseFragment;
import cc.adcat.qim.bean.ChatBean;
import cc.adcat.qim.events.Event;
import cc.adcat.qim.ui.chat.ChatActivity;
import cc.adcat.qim.widget.EmptyRecyclerView;

public class MessageFragment extends BaseFragment implements MessageContract.IView, SwipeRefreshLayout.OnRefreshListener, ChatBeanAdapter.OnItemClickListener, MessageCallback {

    @BindView(R.id.erv_messages)
    EmptyRecyclerView ervMessages;
    @BindView(R.id.sfl_messages)
    SwipeRefreshLayout sflMessages;
    private MessageContract.IPresenter mPresenter;
    List<ChatBean> mChatBeans;
    private ChatBeanAdapter mChatBeanAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_messages;
    }

    @Override
    protected void createPresenter() {
        mPresenter = new MessagePresenter(getContext(),this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ervMessages.setLayoutManager(layoutManager);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_layout,ervMessages,false);
        ervMessages.setEmptyView(emptyView);
        mChatBeans = new LinkedList<>();
        mChatBeanAdapter = new ChatBeanAdapter(getContext(),mChatBeans);
        ervMessages.setAdapter(mChatBeanAdapter);
        registerForContextMenu(ervMessages);
        sflMessages.setOnRefreshListener(this);
        mChatBeanAdapter.setmOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.loadChats(this);
    }

    @Override
    public String getTitle() {
        return "消息";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
        menu.setHeaderIcon(R.mipmap.ic_launcher);
        menu.add(0, 0, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()){
            case 0:
                mPresenter.deleteChat(mChatBeans.get(menuInfo.position),this);
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRecivied(Event event) {
        switch (event.getType()) {
            case Event.TYPE_REFRESH_MESSAGE_LIST:
                mPresenter.loadChats(this);
                break;
        }
    }

    @Override
    public void refresh() {
        sflMessages.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        if (sflMessages.isRefreshing()){
            sflMessages.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.loadChats(this);
    }

    @Override
    public void onClick(ChatBean chatBean) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("user",chatBean.getUser());
        startActivity(intent);
    }

    @Override
    public void onLongClick(ChatBean chatBean) {

    }

    @Override
    public void onLoadSuccess(List<ChatBean> chatBeans) {
        mChatBeans.clear();
        mChatBeans.addAll(chatBeans);
        mChatBeanAdapter.notifyDataSetChanged();
        stopRefresh();
    }

    @Override
    public void onDeleteSuccess() {
        mPresenter.loadChats(this);
    }
}
