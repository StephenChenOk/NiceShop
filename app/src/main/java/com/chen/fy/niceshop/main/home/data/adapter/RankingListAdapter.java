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
import com.chen.fy.niceshop.main.home.data.model.Commodity;
import com.chen.fy.niceshop.main.home.data.model.RankingItem;

import java.util.List;

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Commodity> mList;
    private IClickListener mListener;

    public RankingListAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setList(List<Commodity> list) {
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

        // set
        int index = position+1;
        if (index == 1) {
            holder.tvTop.setBackgroundResource(R.drawable.top_one_box);
        } else {
            holder.tvTop.setBackgroundResource(R.drawable.top_two_box);
        }
        holder.tvTop.setText("TOP  " + index);
        Glide.with(mContext).load("http://" + commodity.getPic_url()).into(holder.ivImage);
        holder.tvName.setText(commodity.getTitle());
        holder.tvPrice.setText(String.valueOf(commodity.getView_price()));
        holder.tvType.setText(commodity.getPlatform());
        holder.tvCommentNum.setText(String.valueOf(commodity.getComment_num()));
        holder.tvHeatNum.setText(String.valueOf(commodity.getHot()));

        // click
        holder.itemView.setOnClickListener(v -> {
            mListener.clickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTop;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvType;
        private TextView tvCommentNum;
        private TextView tvHeatNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTop = itemView.findViewById(R.id.tv_top);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvType = itemView.findViewById(R.id.tv_type);
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num);
            tvHeatNum = itemView.findViewById(R.id.tv_heat_num);
        }
    }

    public interface IClickListener {
        void clickItem(int position);
    }
}
