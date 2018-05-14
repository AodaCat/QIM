package cc.adcat.qim.im.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import cc.adcat.qim.global.Config;

public class QIMConnection extends XMPPTCPConnection{
    private static final String TAG = "QIMConnection";
    private static QIMConnection instance;
    private QIMConnection(XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    private QIMConnection(CharSequence jid, String password) {
        super(jid, password);
    }

    private QIMConnection(CharSequence username, String password, String serviceName) {
        super(username, password, serviceName);
    }

    public static QIMConnection getInstance(){
        //初始化XMPPTCPConnection相关配置
        if(instance == null){
           synchronized (QIMConnection.class){
               if (instance == null){
                   XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                   //设置连接超时的最大时间
                   builder.setConnectTimeout(10000);
                   //设置登录openfire的用户名和密码
                   builder.setUsernameAndPassword(Config.USERNAME, Config.PASSWORD);
                   //设置安全模式
                   builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                   builder.setResource("Android");
                   //设置服务器名称
                   builder.setServiceName(Config.SERVER_NAME);
                   //设置主机地址
                   builder.setHost(Config.SERVER_HOST);
                   //设置端口号
                   builder.setPort(Config.PORT);
                   //是否查看debug日志
                   builder.setDebuggerEnabled(true);
                   instance = new QIMConnection(builder.build());
                   instance.setPacketReplyTimeout(10000);
               }
           }
        }
        return instance;
    }

}
