package com.chen.fy.niceshop.main.home.detail.score;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.XWApplication;
import com.chen.fy.niceshop.main.home.detail.CommodityDetailActivity;
import com.chen.fy.niceshop.main.user.collection.BaseCollectionResponse;
import com.chen.fy.niceshop.network.CollectionService;
import com.chen.fy.niceshop.network.ScoreService;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.RUtil;
import com.chen.fy.niceshop.utils.UserSP;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreFragment extends BottomSheetDialogFragment {

    // 所要评分的商品ID
    private static int commodityID;
    // 评分
    private float scoreNumber = 0;

    private static IClickListener mClickListener;

    public static ScoreFragment getInstance(int id,IClickListener clickListener) {
        commodityID = id;
        mClickListener = clickListener;
        return new ScoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        TextView tvNumber = view.findViewById(R.id.tv_number);

        //星级评价条点击事件
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            scoreNumber = rating;
            tvNumber.setText(String.format("评分:%s", rating));
        });

        // 提交评分
        view.findViewById(R.id.btn_score).setOnClickListener(v -> {
            if(mClickListener!=null){
                mClickListener.doScore(scoreNumber);
            }
        });
    }

    public interface IClickListener{
        void doScore(float submitScore);
    }
}
