package com.chen.fy.niceshop.main.home.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.home.data.model.Commodity;

import java.util.List;


public class CabbageZoneAdapter extends RecyclerView.Adapter<CabbageZoneAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Commodity> mList;
    private IClickListener mListener;

    public CabbageZoneAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setDataList(List<Commodity> list) {
        this.mList = list;
    }

    public void setListener(IClickListener listener) {
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
        Commodity commodity = mList.get(position);

        String imgPath = "http:" + commodity.getPic_url();
        Glide.with(mContext).load(imgPath).into(holder.ivImage);
        holder.tvName.setText(commodity.getTitle());
        holder.tvPrice.setText(String.valueOf(commodity.getView_price()));
        holder.tvType.setText(commodity.getPlatform());
        // 商品详情
        holder.itemView.setOnClickListener(v -> {
            mListener.gotoDetail(commodity.getId());
        });
        // 去逛逛
        holder.llGoLookBox.setOnClickListener(v -> {
            mListener.gotoLookAround(position);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvType;
        private LinearLayout llGoLookBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvType = itemView.findViewById(R.id.tv_type);
            llGoLookBox = itemView.findViewById(R.id.ll_go_look_box);
        }
    }

    public interface IClickListener {
        // 商品详情
        void gotoDetail(int id);

        // 去逛逛
        void gotoLookAround(int position);
    }
}
