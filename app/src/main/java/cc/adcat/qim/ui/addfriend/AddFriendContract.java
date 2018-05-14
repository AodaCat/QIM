package cc.adcat.qim.ui.addfriend;

public interface AddFriendContract {
    interface IView{
        void showProgressBar(String msg);
        void hideProgressBar();
        void toast(String msg);
    }
    interface IPresenter{
        void find(String username,AddFriendCallback callback);
    }
}
