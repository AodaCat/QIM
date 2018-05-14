package cc.adcat.qim;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager implements Application.ActivityLifecycleCallbacks{
    private List<Activity> activities;
    private static ActivityManager instance;

    private ActivityManager() {
        activities = new ArrayList<>();
    }

    public static ActivityManager getInstance(){
        if (instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    public void finishAll(){
        for (Activity activity:activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }
}
