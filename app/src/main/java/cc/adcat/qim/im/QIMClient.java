package cc.adcat.qim.im;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.address.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.commands.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.delay.provider.DelayInformationProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.disco.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.iqprivate.PrivateDataManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.packet.GroupChatInvitation;
import org.jivesoftware.smackx.muc.provider.MUCAdminProvider;
import org.jivesoftware.smackx.muc.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.muc.provider.MUCUserProvider;
import org.jivesoftware.smackx.offline.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.offline.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.privacy.provider.PrivacyProvider;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.sharedgroups.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.si.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.provider.DataFormProvider;
import org.jivesoftware.smackx.xhtmlim.provider.XHTMLExtensionProvider;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.adcat.qim.bean.Contact;
import cc.adcat.qim.im.xmpp.QIMConnection;
import cc.adcat.qim.utils.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QIMClient implements IClient, ConnectionListener, ChatMessageListener {
    private static final String TAG = "QIMClient";
    private static QIMClient instance;
    private QIMConnection mConnection;
    private Roster mRoster;
    private ChatManager mChatManager;
    private UserSearchManager mUserSearchManager;
    private Map<String,Chat> chats;
    private QIMClient() {
        init();
    }

    public static QIMClient getInstance(){
        if (instance == null){
            synchronized (QIMClient.class){
                if (instance == null){
                    instance = new QIMClient();
                }
            }
        }
        return instance;
    }

    private void init() {
        Log.d(TAG,"init");
        mConnection = QIMConnection.getInstance();
        mConnection.addConnectionListener(this);
        mRoster = Roster.getInstanceFor(mConnection);
        mChatManager = ChatManager.getInstanceFor(mConnection);
        chats = new HashMap<>();
    }

    private void configure(ProviderManager pm) {

//  Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

//  Time
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

//  Roster Exchange
//        pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());

//  Message Events
//        pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());

//  Chat State
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

//  XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

//  Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());

//  Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

//  Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

//  Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

//  MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());

//  MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

//  MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

//  Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());

//  Version
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            //  Not sure what's happening here.
        }

//  VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

//  Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

//  Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

//  Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

//  User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

//  SharedGroupsInfo
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

//  JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());

//   FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());

        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

//  Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());
    }

    @Override
    public Observable<Boolean> login(final String username, final String password) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if (!mConnection.isConnected()){
                mConnection.connect();
            }
            mConnection.login(username,password);
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus("在线");
            mConnection.sendStanza(presence);
            emitter.onNext(Boolean.TRUE);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> register(final String username, final String password) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            if (!mConnection.isConnected()){
                mConnection.connect();
            }
            AccountManager.getInstance(mConnection).createAccount(username,password);
            mConnection.login(username,password);
            Presence presence = new Presence(Presence.Type.available);
            presence.setStatus("在线");
            mConnection.sendStanza(presence);
            emitter.onNext(Boolean.TRUE);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> logout() {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                //这里需要先将登陆状态改变为“离线”，再断开连接，不然在后台还是上线的状态
                Presence presence = new Presence(Presence.Type.unavailable);
                mConnection.sendStanza(presence);
                mConnection.disconnect();
                emitter.onNext(Boolean.TRUE);
                emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Contact>> getContacts() {
        return Observable.create((ObservableOnSubscribe<List<Contact>>) emitter -> {
            Set<RosterEntry> entrySet = mRoster.getEntries();
            List<Contact> contacts = new ArrayList<>();
            for (RosterEntry e : entrySet) {
                Contact contact = new Contact();
                contact.setEntry(e);
                contact.setPresence(mRoster.getPresence(e.getUser()));
                contacts.add(contact);
            }
            emitter.onNext(contacts);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> send(String user, String msg) {
        return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            Chat chat = chats.get(user);
            if (chat == null){
                chat = mChatManager.createChat(user,QIMClient.this);
                chats.put(user,chat);
            }
            chat.sendMessage(msg);
            emitter.onNext(Boolean.TRUE);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void addAsyncStanzaListener(StanzaListener listener) {
        try {
            if (!mConnection.isConnected()){
                mConnection.connect();
            }
            mConnection.addAsyncStanzaListener(listener,null);
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCurrentUser() {
        return mConnection.getUser();
    }

    @Override
    public Observable findUser(String username) {
        try {
            if (mConnection == null){
                return null;
            }
            if (!mConnection.isConnected()){
                mConnection.connect();
            }
            if (mUserSearchManager == null){
                mUserSearchManager = new UserSearchManager(mConnection);
            }
            HashMap<String, String> user;
            List<HashMap<String, String>> results = new ArrayList<>();

            Form searchForm = mUserSearchManager.getSearchForm(mConnection.getServiceName());
            if (searchForm == null)
                return null;

            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("userAccount", true);
//            answerForm.setAnswer("userPhote", userName);
            ReportedData data = mUserSearchManager.getSearchResults(answerForm, String.valueOf(JidCreate.domainBareFrom("search" + mConnection.getServiceName())));

            List<ReportedData.Row> rowList = data.getRows();
            for (ReportedData.Row row : rowList) {
                user = new HashMap<>();
                user.put("userAccount", row.getValues("userAccount").toString());
                user.put("userPhote", row.getValues("userPhote").toString());
                results.add(user);
                // 若存在，则有返回,UserName一定非空，其他两个若是有设，一定非空
            }
            return null;
        } catch (SmackException | XmppStringprepException | XMPPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void connected(XMPPConnection connection) {
        Log.d(TAG,"connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d(TAG,"authenticated");
    }

    @Override
    public void connectionClosed() {
        Log.d(TAG,"connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(TAG,"connectionClosedOnError");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(TAG,"reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(TAG,"reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(TAG,"reconnectionFailed");
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        Log.d(TAG,"processMessage->chat:"+chat.toString()+"msg:"+message.getBody());
    }
}
