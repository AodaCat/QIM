package cc.adcat.qim.ui.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.adcat.qim.ActivityManager;
import cc.adcat.qim.R;
import cc.adcat.qim.adapter.FragmentsAdapter;
import cc.adcat.qim.base.BaseActivity;
import cc.adcat.qim.events.Event;
import cc.adcat.qim.global.Constant;
import cc.adcat.qim.services.QIMService;
import cc.adcat.qim.ui.addfriend.AddFriendActivity;
import cc.adcat.qim.ui.main.fragments.contacts.ContactsFragment;
import cc.adcat.qim.ui.main.fragments.dongtai.DongTaiFragments;
import cc.adcat.qim.ui.main.fragments.message.MessageFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements MainContarct.IView, ViewPager.OnPageChangeListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_add_friend)
    Button btnAddFriend;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.btn_message)
    Button btnMessage;
    @BindView(R.id.btn_contacts)
    Button btnContacts;
    @BindView(R.id.btn_dongtai)
    Button btnDongtai;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private MainContarct.IPresenter mPresenter;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    @Override
    protected void creatPresenter() {
        mPresenter = new MainPresenter(this,this);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mFragments = new ArrayList<>();
        mFragments.add(new MessageFragment());
        mFragments.add(new ContactsFragment());
        mFragments.add(new DongTaiFragments());
        mFragmentPagerAdapter = new FragmentsAdapter(getSupportFragmentManager(),mFragments);
        vpContent.setAdapter(mFragmentPagerAdapter);
        tvTitle.setText(mFragmentPagerAdapter.getPageTitle(0));
        vpContent.addOnPageChangeListener(this);
        btnAddFriend.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnContacts.setOnClickListener(this);
        btnDongtai.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getStringExtra(Constant.ACTION);
        if (TextUtils.equals(action,Constant.ACTION_NEW_MESSAGE)){
            EventBus.getDefault().post(new Event(Event.TYPE_CLEAR_UNREADMESSAGE));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, QIMService.class);
        stopService(intent);
        ActivityManager.getInstance().finishAll();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        String title = mFragmentPagerAdapter.getPageTitle(position).toString();
        tvTitle.setText(title);
        if (position == 1){
            btnAddFriend.setVisibility(View.VISIBLE);
        }else {
            btnAddFriend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_message:
                vpContent.setCurrentItem(0,true);
                break;
            case R.id.btn_contacts:
                vpContent.setCurrentItem(1,true);
                break;
            case R.id.btn_dongtai:
                vpContent.setCurrentItem(2,true);
                break;
            case R.id.btn_add_friend:
                addFriends();
                break;
        }
    }

    private void addFriends() {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btn_exit:
                finish();
                return true;
        }
        return false;
    }
}
