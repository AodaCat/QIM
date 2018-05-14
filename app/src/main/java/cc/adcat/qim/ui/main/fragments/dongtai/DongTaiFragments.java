package cc.adcat.qim.ui.main.fragments.dongtai;

import cc.adcat.qim.R;
import cc.adcat.qim.base.BaseFragment;

public class DongTaiFragments extends BaseFragment{
    @Override
    protected int getContentView() {
        return R.layout.fragment_dongtai;
    }

    @Override
    protected void createPresenter() {

    }

    @Override
    public String getTitle() {
        return "动态";
    }
}
