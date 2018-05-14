package cc.adcat.qim.ui.register;

public interface RegisterCallback {
    void onSuccess();
    void onFailed(Throwable e);
}
