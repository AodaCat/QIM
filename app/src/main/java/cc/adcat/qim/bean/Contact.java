package cc.adcat.qim.bean;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;

import java.io.Serializable;

public class Contact implements Serializable{
   private RosterEntry entry;
   private Presence presence;

    public RosterEntry getEntry() {
        return entry;
    }

    public void setEntry(RosterEntry entry) {
        this.entry = entry;
    }

    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }
}
