package cc.adcat.qim.ui.main.fragments.contacts;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.adcat.qim.R;
import cc.adcat.qim.adapter.ContactsAdapter;
import cc.adcat.qim.base.BaseFragment;
import cc.adcat.qim.bean.Contact;
import cc.adcat.qim.ui.chat.ChatActivity;
import cc.adcat.qim.utils.Log;
import cc.adcat.qim.utils.ToastUtil;
import cc.adcat.qim.widget.EmptyRecyclerView;

public class ContactsFragment extends BaseFragment implements ContactsContract.IView, ContactsCallback, SwipeRefreshLayout.OnRefreshListener, ContactsAdapter.OnItemClickListener {
    private static final String TAG = "ContactsFragment";
    @BindView(R.id.erv_contacts)
    EmptyRecyclerView ervContacts;
    @BindView(R.id.srl_contacts)
    SwipeRefreshLayout srlContacts;
    private ContactsContract.IPresenter mPresenter;
    private List<Contact> mContacts;
    private ContactsAdapter mContactsAdapter;
    @Override
    protected int getContentView() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void createPresenter() {
        mPresenter = new ContactsPresenter(getContext(),this);
    }

    @Override
    public String getTitle() {
        return "联系人";
    }

    @Override
    protected void initViews() {
        super.initViews();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ervContacts.setLayoutManager(layoutManager);
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_friends,ervContacts,false);
        ervContacts.setEmptyView(emptyView);
        mContacts = new ArrayList<>();
        mContactsAdapter = new ContactsAdapter(getContext(),mContacts);
        mContactsAdapter.setOnItemClickListener(this);
        ervContacts.setAdapter(mContactsAdapter);
        srlContacts.setOnRefreshListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getContacts(this);
    }

    @Override
    public void onGetContactsSuccess(List<Contact> contacts) {
        if (srlContacts.isRefreshing()){
            srlContacts.setRefreshing(false);
        }
        Log.d(TAG,"onGetContactsSuccess:size->"+contacts.size());
        mContactsAdapter.reLoad(contacts);
    }

    @Override
    public void onGetContactsFailed(Throwable e) {
        if (srlContacts.isRefreshing()){
            srlContacts.setRefreshing(false);
        }
        ToastUtil.showLong(getContext(),"获取联系人列表失败...");
    }

    @Override
    public void onRefresh() {
        mPresenter.getContacts(this);
    }

    @Override
    public void onClick(Contact contact) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("user",contact.getEntry().getUser());
        startActivity(intent);
    }
}
