package cc.adcat.qim.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.packet.RosterPacket;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.adcat.qim.R;
import cc.adcat.qim.bean.Contact;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private static final String TAG = "ContactsAdapter";
    private List<Contact> contacts;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ContactsAdapter(Context context, List<Contact> contacts) {
        this.contacts = contacts == null ? new ArrayList<>():contacts;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.civHead.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.tab_me_icon));
        holder.tvName.setText(contact.getEntry().getName());
        holder.ivStatus.setVisibility(contact.getPresence().getType() == Presence.Type.available ? View.VISIBLE : View.INVISIBLE );
        holder.ivPhone.setVisibility(contact.getPresence().getType() == Presence.Type.available ? View.VISIBLE : View.INVISIBLE);
        holder.item.setOnClickListener(v->{
            if (onItemClickListener != null){
                onItemClickListener.onClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void reLoad(List<Contact> newContacts){
        contacts.clear();
        contacts.addAll(newContacts);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_phone)
        ImageView ivPhone;
        @BindView(R.id.item)
        RelativeLayout item;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener{
        void onClick(Contact contact);
    }
}
