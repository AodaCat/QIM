package cc.adcat.qim.ui.newfriend;

public interface NewFriendContract {
    interface IView{
        void showProgressBar(String msg);
        void hideProgressBar();
        void toast(String msg);
    }
    interface IPresenter{
        void addFriend(String username, NewFriendCallback callback);
    }
}
