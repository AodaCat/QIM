package cc.adcat.qim;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;

import cc.adcat.qim.bean.sql.gen.DaoMaster;
import cc.adcat.qim.bean.sql.gen.DaoSession;

public class App extends Application{
    private static App instance;
    private DaoMaster mDaoMaster;
    private DaoMaster.OpenHelper mOpenHelper;
    private DaoSession mDaoSession;
    private SQLiteDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(ActivityManager.getInstance());
        initDatabase();
        if (BuildConfig.DEBUG){//debug 模式开启数据库调试
            Stetho.initializeWithDefaults(this);
        }
    }

    private void initDatabase() {
        mOpenHelper = new DaoMaster.DevOpenHelper(this,"qim-db");
        database = mOpenHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(database);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public static App getInstance(){
        return instance;
    }
}
