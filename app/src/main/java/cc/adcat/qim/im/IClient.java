package cc.adcat.qim.im;

import org.jivesoftware.smack.StanzaListener;

import java.util.List;

import cc.adcat.qim.bean.Contact;
import io.reactivex.Observable;

public interface IClient {
    Observable<Boolean> login(String username, String password);
    Observable<Boolean> register(String username,String password);
    Observable<Boolean> logout();
    Observable<List<Contact>> getContacts();
    Observable<Boolean> send(String user,String msg);
    void addAsyncStanzaListener(StanzaListener listener);
    String getCurrentUser();
    Observable<String> findUser(String username);
    Observable<Boolean> addFriend(String user);
}
