package cc.adcat.qim.ui.login;

public interface LoginCallback {
    void onSuccess();
    void onFailed(Throwable e);
}
