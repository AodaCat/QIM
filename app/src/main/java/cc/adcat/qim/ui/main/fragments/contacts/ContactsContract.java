package cc.adcat.qim.ui.main.fragments.contacts;

public interface ContactsContract {
    interface IView{

    }
    interface IPresenter{
        void getContacts(ContactsCallback callback);
    }
}
