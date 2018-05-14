package cc.adcat.qim.ui.main.fragments.message;

import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseFragment;

public class MessageFragment extends BaseFragment{
    @Override
    protected int getContentView() {
        return R.layout.fragment_messages;
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    public String getTitle() {
        return "消息";
    }
}
