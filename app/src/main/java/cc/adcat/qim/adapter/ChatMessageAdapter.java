package cc.adcat.qim.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.adcat.qim.R;
import cc.adcat.qim.bean.ChatMessage;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    private static final int TYPE_GET = 0;
    private static final int TYPE_SEND = 1;


    private Context mContext;
    private List<ChatMessage> messages;
    private String user;

    public ChatMessageAdapter(Context context, String user, List<ChatMessage> messages) {
        this.mContext = context;
        this.user = user;
        this.messages = messages == null ? new ArrayList<>() : messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_GET:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_get, parent, false);
                break;
            case TYPE_SEND:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_send, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = messages.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.civHead.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.tab_me_icon));
        holder.tvTime.setText(sdf.format(new Date(chatMessage.getTime())));
        holder.tvName.setText(chatMessage.getFrom());
        holder.tvMsg.setText(chatMessage.getBody());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (TextUtils.equals(message.getFrom(), user)) {
            return TYPE_GET;
        } else {
            return TYPE_SEND;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
