package cc.adcat.qim.ui.register;

public interface RegisterContract {
    interface IView{
        void showProgressBar(String msg);
        void hideProgressBar();
        void toast(String msg);
    }
    interface IPresenter{
        void register(String username,String password,String rePassword,RegisterCallback callback);
    }
}
