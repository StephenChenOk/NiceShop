package com.chen.fy.niceshop.main.home.data.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.utils.ShowUtils;

import java.util.List;

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Commodity> mData;
    private IClickGoodsListener mListener;
    private String searchStr;

    public CommodityAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setData(List<Commodity> list) {
        this.mData = list;
    }

    public void setListener(IClickGoodsListener listener) {
        this.mListener = listener;
    }

    public void setSearchStr(String msg) {
        this.searchStr = msg;
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
        Commodity commodity = mData.get(position);

        String path = "http:" + commodity.getPic_url();
        // set
        Glide.with(mContext).load(path).into(holder.ivImage);
        holder.tvTitle.setText(commodity.getTitle());
        holder.tvPrice.setText(String.valueOf(commodity.getView_price()));
        holder.tvPlatform.setText(commodity.getPlatform());
        holder.tvCommentNum.setText(String.valueOf(commodity.getComment_num()));
        holder.tvHeatNum.setText(String.valueOf(commodity.getHot()));

        // click
        holder.itemView.setOnClickListener(v -> {
            mListener.clickItem(commodity.getId());
        });

        /// 搜索时高亮
        if (searchStr != null) {
            configHighLightText(commodity, holder.tvTitle, holder.tvPlatform);
        }
    }

    private void configHighLightText(Commodity commodity, TextView tvName, TextView tvType) {
        //设置搜索高亮
        if (commodity.getTitle().toLowerCase().contains(searchStr.toLowerCase())) {
            tvName.setText(ShowUtils.highLightText(commodity.getTitle(), searchStr));
        }
        if (commodity.getPlatform().toLowerCase().contains(searchStr.toLowerCase())) {
            tvType.setText(ShowUtils.highLightText(commodity.getPlatform(), searchStr));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvPrice;
        private TextView tvPlatform;
        private TextView tvCommentNum;
        private TextView tvHeatNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPlatform = itemView.findViewById(R.id.tv_platform);
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num);
            tvHeatNum = itemView.findViewById(R.id.tv_heat_num);
        }
    }

    public interface IClickGoodsListener {
        void clickItem(int id);
    }
}
