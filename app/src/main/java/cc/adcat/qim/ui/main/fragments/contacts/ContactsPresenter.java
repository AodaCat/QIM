package cc.adcat.qim.ui.main.fragments.contacts;

import android.content.Context;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

import cc.adcat.qim.bean.Contact;
import cc.adcat.qim.im.IClient;
import cc.adcat.qim.im.QIMClient;
import cc.adcat.qim.utils.Log;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ContactsPresenter implements ContactsContract.IPresenter{
    private static final String TAG = "ContactsPresenter";
    private Context mContext;
    private ContactsContract.IView mView;
    private IClient mClient;

    public ContactsPresenter(Context context, ContactsContract.IView view) {
        this.mContext = context;
        this.mView = view;
        mClient = QIMClient.getInstance();
    }

    @Override
    public void getContacts(final ContactsCallback callback) {
        mClient.getContacts()
                .subscribe(new Observer<List<Contact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Contact> contacts) {
                        if (callback != null){
                            callback.onGetContactsSuccess(contacts);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null){
                            callback.onGetContactsFailed(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"联系人获取完成...");
                    }
                });
    }
}
