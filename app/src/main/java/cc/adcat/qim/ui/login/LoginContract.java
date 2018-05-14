package cc.adcat.qim.ui.login;

public interface LoginContract {
    interface IView{
        void showProgressBar(String msg);
        void hideProgressBar();
        void toast(String msg);
    }
    interface IPersenter{
        void login(String username,String password,LoginCallback callback);
    }
}
