package cc.adcat.qim.ui.main.fragments.contacts;

import java.util.List;

import cc.adcat.qim.bean.Contact;

public interface ContactsCallback {
    void onGetContactsSuccess(List<Contact> contacts);
    void onGetContactsFailed(Throwable e);
}
