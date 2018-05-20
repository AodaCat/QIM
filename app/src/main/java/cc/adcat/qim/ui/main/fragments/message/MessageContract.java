package cc.adcat.qim.ui.main.fragments.message;

import cc.adcat.qim.bean.ChatBean;

public interface MessageContract {
    interface IView{
        void refresh();
        void stopRefresh();
    }
    interface IPresenter{
        void loadChats(MessageCallback callback);
        void deleteChat(ChatBean chatBean,MessageCallback callback);
    }
}
