package com.chen.fy.niceshop.main.home.view.activity.navigation;

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
import com.chen.fy.niceshop.main.goodprice.data.model.Category;

import java.util.List;


public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Category> mList;
    private IClickItemListener mListener;

    public NavigationAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setDataList(List<Category> list) {
        this.mList = list;
    }

    public void setListener(IClickItemListener listener) {
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
        Category category = mList.get(position);

        Glide.with(mContext).load(category.getImg_id()).into(holder.ivImage);
        holder.tvName.setText(category.getName());

        // click
        holder.itemView.setOnClickListener(v -> {
            mListener.clickItem(category.getName());
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    public interface IClickItemListener {
        void clickItem(String title);
    }
}
