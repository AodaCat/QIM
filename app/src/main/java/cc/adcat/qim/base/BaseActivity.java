package cc.adcat.qim.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by ygj on 2017/12/14.
 */

public abstract class BaseActivity extends AppCompatActivity{
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewResId());
        ButterKnife.bind(this);
        creatPresenter();
        initViews();
    }

    protected  void initViews(){

    }

    protected abstract int getContentViewResId();
    protected abstract void creatPresenter();

    protected void launcherActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void launcherActivity(Intent intent) {
        startActivity(intent);
    }

    public void replaceFragment(int id_content, Fragment fragment) {
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(id_content, fragment);
        fragmentTransaction.commit();
    }

    protected void replaceFragment(Class<? extends Fragment> cls, String title) {
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra(ContentActivity.TAG_CLASS_NAME, cls.getName());
        intent.putExtra(ContentActivity.TAG_TITLE, title);
        startActivity(intent);
    }
    protected void replaceFragment(Class<? extends Fragment> cls, String title, Bundle bundle) {
        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra(ContentActivity.TAG_CLASS_NAME, cls.getName());
        intent.putExtra(ContentActivity.TAG_TITLE, title);
        intent.putExtra(ContentActivity.TAG_EXTRA,bundle);
        startActivity(intent);
    }
}
