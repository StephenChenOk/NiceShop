package com.chen.fy.niceshop.main.home.detail.share;

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

import java.util.List;

class ShareRecyclerViewAdapter extends RecyclerView.Adapter<ShareRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<ShareItem> mList;
    private IClickListener mClickListener;

    public ShareRecyclerViewAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setData(List<ShareItem> list) {
        this.mList = list;
    }

    public void setClickListener(IClickListener clickListener) {
        this.mClickListener = clickListener;
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
        ShareItem shareItem = mList.get(position);
        // set
        Glide.with(mContext).load(shareItem.getIcon()).into(holder.icon);
        holder.name.setText(shareItem.getName());
        // click
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(v -> mClickListener.onClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.iv_icon);
            name = itemView.findViewById(R.id.tv_name);
        }
    }

    interface IClickListener {
        void onClick(int position);
    }
}