package cc.adcat.qim.ui.chat;

import cc.adcat.qim.bean.ChatMessage;

public interface ChatContract {
    interface IView{
        void toast(String msg);
    }
    interface IPresenter{
        void loadChatMessages(String user,long time,int limit,ChatCallback callback);
        void send(String user,String msg,ChatCallback callback);
        void restore(ChatMessage message);
    }
}
