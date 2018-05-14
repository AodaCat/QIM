package cc.adcat.qim.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.jivesoftware.smack.packet.Message;

@Entity
public class ChatMessage {
    @Id(autoincrement = true)
    private Long id;
    private String from;
    private String to;
    private String body;
    private String type;
    private Long time;
    @Generated(hash = 1908760189)
    public ChatMessage(Long id, String from, String to, String body, String type,
            Long time) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.body = body;
        this.type = type;
        this.time = time;
    }
    @Generated(hash = 2271208)
    public ChatMessage() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public static ChatMessage parse(Message message){
        ChatMessage chatMessage = new ChatMessage();
        String from = message.getFrom();
        chatMessage.setFrom(from.substring(0,from.indexOf('/')));
        chatMessage.setTo(message.getTo());
        chatMessage.setTime(System.currentTimeMillis());
        chatMessage.setBody(message.getBody());
        chatMessage.setType(message.getType().name());
        return chatMessage;
    }
}
