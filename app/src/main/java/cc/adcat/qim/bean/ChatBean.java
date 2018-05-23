package cc.adcat.qim.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 对应于消息列表中的每一项
 */
@Entity
public class ChatBean {
    @Id
    private Long id;
    private String user;//会话的来源，会话窗口的对方，而不是消息的对方
    private String message;
    private Long time;
    private String to;//对应于当前登录用户，会话的接收者，而不是消息的接收者，这里就是当前登陆用户
    private int unReadCount;
    @Generated(hash = 653795555)
    public ChatBean(Long id, String user, String message, Long time, String to,
            int unReadCount) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.time = time;
        this.to = to;
        this.unReadCount = unReadCount;
    }
    @Generated(hash = 1872716502)
    public ChatBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public int getUnReadCount() {
        return this.unReadCount;
    }
    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
   
    
}
