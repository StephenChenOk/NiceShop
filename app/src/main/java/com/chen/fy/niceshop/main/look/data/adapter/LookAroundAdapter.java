package com.chen.fy.niceshop.main.look.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.look.data.model.LookAroundItem;
import com.chen.fy.niceshop.main.look.data.model.dynamic.Dynamic;
import com.chen.fy.niceshop.network.ServiceCreator;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class LookAroundAdapter extends RecyclerView.Adapter<LookAroundAdapter.ViewHolder> {

    private Context mContext;
    private int mResourceId;
    private List<Dynamic> mList;
    private IClickItemListener mListener;

    public LookAroundAdapter(Context context, int resourceId) {
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

        // image
        String[] strings = dynamic.getImgs().split(",");
        String imgUrl = ServiceCreator.ROOT_URL + strings[0];
        // image layout
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(
                        (int) mContext.getResources().getDimension(R.dimen.look_around_width)
                        , getImageHeight(position));
        holder.ivImage.setLayoutParams(params);
        Glide.with(mContext).load(imgUrl).into(holder.ivImage);

        holder.tvContent.setText(dynamic.getContent());
        Glide.with(mContext)
                .load(ServiceCreator.ROOT_URL + dynamic.getUser_img()).into(holder.civHeadIcon);
        holder.tvName.setText(dynamic.getUser_nickname());
        holder.tvLikeNum.setText(String.valueOf(dynamic.getGive_num()));

        // click
        holder.itemView.setOnClickListener(v -> {
            mListener.clickItem(dynamic.getId());
        });
    }

    private int getImageHeight(int position) {
        int height = 0;
        switch (position % 3) {
            case 0:
                height = (int) mContext.getResources().getDimension(R.dimen.look_around_height1);
                break;
            case 1:
                height = (int) mContext.getResources().getDimension(R.dimen.look_around_height2);
                break;
            case 2:
                height = (int) mContext.getResources().getDimension(R.dimen.look_around_height3);
                break;
        }
        return height;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;
        private TextView tvContent;
        private CircleImageView civHeadIcon;
        private TextView tvName;
        private TextView tvLikeNum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_image_look_around_item);
            tvContent = itemView.findViewById(R.id.tv_content_look_around_item);
            civHeadIcon = itemView.findViewById(R.id.civ_headIcon_look_around_item);
            tvName = itemView.findViewById(R.id.tv_name_look_around_item);
            tvLikeNum = itemView.findViewById(R.id.tv_like_num);
        }
    }

    public interface IClickItemListener {
        void clickItem(int id);
    }
}
