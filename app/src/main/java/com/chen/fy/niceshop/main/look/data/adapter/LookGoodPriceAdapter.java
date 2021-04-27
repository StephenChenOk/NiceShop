package com.chen.fy.niceshop.main.look.data.adapter;

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
import com.chen.fy.niceshop.main.look.data.model.LookGoodPrice;
import com.chen.fy.niceshop.main.look.data.model.dynamic.BaseDynamicResponse;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.network.ServiceCreator;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LookGoodPriceAdapter extends RecyclerView.Adapter<LookGoodPriceAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Dynamic> mList;
    private IClickItemListener mListener;

    public LookGoodPriceAdapter(Context context, int resourceId) {
        this.mContext = context;
        this.mResourceId = resourceId;
    }

    public void setDataList(List<Dynamic> list) {
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
        Dynamic dynamic = mList.get(position);

        // set
        Glide.with(mContext)
                .load(ServiceCreator.ROOT_URL + dynamic.getUser_img()).into(holder.civHeadIcon);
        holder.tvName.setText(dynamic.getUser_nickname());
        Glide.with(mContext)
                .load(ServiceCreator.ROOT_URL + dynamic.getImgs()).into(holder.ivImage);
        holder.tvContent.setText(dynamic.getContent());
        holder.tvLikeNum.setText(String.valueOf(dynamic.getGive_num()));
        holder.tvStarNum.setText(String.valueOf(dynamic.getGive_num()));
        holder.tvCommentNum.setText(String.valueOf(dynamic.getGive_num()));

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

        private CircleImageView civHeadIcon;
        private TextView tvName;
        private ImageView ivImage;
        private TextView tvContent;
        private TextView tvLikeNum;
        private TextView tvStarNum;
        private TextView tvCommentNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            civHeadIcon = itemView.findViewById(R.id.civ_headIcon_look_item);
            tvName = itemView.findViewById(R.id.tv_name_look_item);
            ivImage = itemView.findViewById(R.id.iv_image_look_item);
            tvContent = itemView.findViewById(R.id.tv_content_look_item);
            tvLikeNum = itemView.findViewById(R.id.tv_like_num_look_item);
            tvStarNum = itemView.findViewById(R.id.tv_star_num_look_item);
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num_look_item);
        }
    }

    public interface IClickItemListener {
        void clickItem(int position);
    }
}
