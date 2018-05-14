package cc.adcat.qim.base;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    protected BaseActivity baseActivity;
    protected View mRootView;
    private Unbinder unbinder;
    /**
     * Toolbar.
     */
    private Toolbar mToolbar;
    private Activity mActivity;
    public BaseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentView(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        createPresenter();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            baseActivity = (BaseActivity) getActivity();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    protected void initViews() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract int getContentView();

    protected abstract void createPresenter();

    public String getTitle(){
        return null;
    }

    protected void replaceFragment(Class<? extends Fragment> cls) {
        Intent intent = new Intent(baseActivity, ContentActivity.class);
        intent.putExtra(ContentActivity.TAG_CLASS_NAME, cls.getName());
        startActivity(intent);
    }

    protected void replaceFragment(Class<? extends Fragment> cls, String title) {
        Intent intent = new Intent(baseActivity, ContentActivity.class);
        intent.putExtra(ContentActivity.TAG_CLASS_NAME, cls.getName());
        intent.putExtra(ContentActivity.TAG_TITLE, title);
        startActivity(intent);
    }

    protected void replaceFragment(Fragment cls, String title, Bundle bundle) {
        Intent intent = new Intent(baseActivity, ContentActivity.class);
        intent.putExtra(ContentActivity.TAG_CLASS_NAME, cls.getClass().getName());
        intent.putExtra(ContentActivity.TAG_TITLE, title);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /**
     * Set Toolbar.
     *
     * @param toolbar {@link Toolbar}.
     */
    @SuppressLint("RestrictedApi")
    public final void setToolbar(@NonNull Toolbar toolbar, Context context) {
        this.mToolbar = toolbar;
        onCreateOptionsMenu(mToolbar.getMenu(), new SupportMenuInflater(context));
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
    }
    /**
     * Display home up button.
     *
     * @param drawableId drawable id.
     */
    public final void displayHomeAsUpEnabled(@DrawableRes int drawableId, Context context) {
        displayHomeAsUpEnabled(ContextCompat.getDrawable(context, drawableId));
    }

    /**
     * Display home up button.
     *
     * @param drawable {@link Drawable}.
     */
    public final void displayHomeAsUpEnabled(Drawable drawable) {
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onInterceptToolbarBack())
                    finish();
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }
    /**
     * Destroy me.
     */
    public void finish() {
        mActivity.onBackPressed();
    }
    /**
     * Override this method, intercept backPressed of ToolBar.
     *
     * @return true, other wise false.
     */
    public boolean onInterceptToolbarBack() {
        return false;
    }
}
