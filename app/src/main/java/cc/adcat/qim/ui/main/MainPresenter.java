package cc.adcat.qim.ui.main;

import android.content.Context;

public class MainPresenter implements MainContarct.IPresenter{
    private Context mContext;
    private MainContarct.IView mView;

    public MainPresenter(Context context, MainContarct.IView view) {
        this.mContext = context;
        this.mView = view;
    }
}
