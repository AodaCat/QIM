package cc.adcat.qim.events;

public class Event {
    public static final int TYPE_CLEAR_UNREADMESSAGE = 1;
    public static final int TYPE_REFRESH_MESSAGE_LIST = 2;
    private int type;

    public Event(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
