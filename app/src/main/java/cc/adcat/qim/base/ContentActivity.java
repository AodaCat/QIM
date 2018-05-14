package cc.adcat.qim.base;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import cc.adcat.qim.R;


/**
 * Fragemnt容器界面,
 */

public class ContentActivity extends BaseActivity {
    public final static String TAG_CLASS_NAME = "ClassName";
    public final static String TAG_TITLE = "title";
    public final static String TAG_EXTRA = "params";
    private String fragmentClassName;
    private Fragment fragment;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_content;
    }

    @Override
    protected void creatPresenter() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        fragmentClassName = intent.getStringExtra(TAG_CLASS_NAME);
        if (!TextUtils.isEmpty(fragmentClassName)) {
            try {
                fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                if (null != intent.getExtras()) {
                    fragment.setArguments(intent.getExtras());
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        replaceFragment(R.id.contentPanel, fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
