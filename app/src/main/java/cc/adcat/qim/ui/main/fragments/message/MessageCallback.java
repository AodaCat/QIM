package cc.adcat.qim.ui.main.fragments.message;

import java.util.List;

import cc.adcat.qim.bean.ChatBean;

public interface MessageCallback {
    void onLoadSuccess(List<ChatBean> chatBeans);
    void onDeleteSuccess();
}
