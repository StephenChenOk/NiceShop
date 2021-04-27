package com.chen.fy.niceshop.main.home.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.model.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Comment> mCommentList;
    private IClickCommentListener mListener;

    public CommentAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setData(List<Comment> list) {
        this.mCommentList = list;
    }

    public void setListener(IClickCommentListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mResourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Comment comment = mCommentList.get(position);

        // set
        Glide.with(mContext).load("http://39.106.225.65" + comment.getUser_img()).into(holder.civHeadIcon);
        holder.tvName.setText(comment.getUser_nickname());
        holder.tvContent.setText(comment.getContent());
        holder.tvDate.setText(comment.getDate());
        if (comment.isLike()) {
            holder.ivLike.setImageResource(R.drawable.ic_favorite_red_20dp);
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_favorite_border_black_20dp);
        }
        // click
        holder.itemView.setOnClickListener(v -> {
            mListener.clickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civHeadIcon;
        private TextView tvName;
        private TextView tvContent;
        private TextView tvDate;
        private ImageView ivLike;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            civHeadIcon = itemView.findViewById(R.id.civ_headIcon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivLike = itemView.findViewById(R.id.iv_like);
        }
    }

    public interface IClickCommentListener {
        void clickItem(int position);
    }
}
