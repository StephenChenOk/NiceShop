package com.chen.fy.niceshop.main.guide;

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

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Category> mList;
    private IClickItemListener mListener;

    public GuideAdapter(Context context, int resourceId) {
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

        String path_base = category.getImg();

        String path = "http://39.106.225.65" + path_base;
        Glide.with(mContext).load(path).into(holder.ivImage);

        holder.tvName.setText(category.getName());

        // click
        holder.itemView.setOnClickListener(v -> {
            // 根据点击状态改变颜色
            if (v.isSelected()) {
                v.setSelected(false);
            } else {
                v.setSelected(true);
            }
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
