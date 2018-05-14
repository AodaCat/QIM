package cc.adcat.qim.ui.chat;

import java.util.List;

import cc.adcat.qim.bean.ChatMessage;

public interface ChatCallback {
    void onSuccess(ChatMessage message);
    void onFailed(Throwable e);
    void onLoadMsgComplete(List<ChatMessage> messages);
}
