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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.adcat.qim.R;
import cc.adcat.qim.bean.ChatBean;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBeanAdapter extends RecyclerView.Adapter<ChatBeanAdapter.ViewHolder> {
    private Context mContext;
    private List<ChatBean> mChatBeans;
    private SimpleDateFormat sdf;
    private OnItemClickListener mOnItemClickListener;
    public ChatBeanAdapter(Context context, List<ChatBean> chatBeans) {
        this.mContext = context;
        this.mChatBeans = chatBeans == null? new LinkedList<>():chatBeans;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatBean chatBean = mChatBeans.get(position);
        holder.civHead.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.tab_me_icon));
        holder.tvName.setText(chatBean.getUser());
        holder.tvMessage.setText(chatBean.getMessage());
        if (chatBean.getUnReadCount() >0 ){
            if (chatBean.getUnReadCount() <= 99){
                holder.tvUnreadCount.setText(chatBean.getUnReadCount()+"");
            }else {
                holder.tvUnreadCount.setText("99+");
            }
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
        }else {
            holder.tvUnreadCount.setVisibility(View.INVISIBLE);
        }
        holder.tvTime.setText(sdf.format(new Date(chatBean.getTime())));
        holder.item.setOnClickListener(v -> {
             if (mOnItemClickListener != null){
                 mOnItemClickListener.onClick(chatBean);
             }
        });
        holder.item.setOnLongClickListener(v -> {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onLongClick(chatBean);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mChatBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.civ_head)
        CircleImageView civHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_unread_count)
        TextView tvUnreadCount;
        @BindView(R.id.item)
        RelativeLayout item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public interface OnItemClickListener{
        void onClick(ChatBean chatBean);
        void onLongClick(ChatBean chatBean);
    }
}
