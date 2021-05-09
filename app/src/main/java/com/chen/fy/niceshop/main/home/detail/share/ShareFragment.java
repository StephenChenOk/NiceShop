package com.chen.fy.niceshop.main.home.detail.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.utils.ShareUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends BottomSheetDialogFragment {

    private List<ResolveInfo> mShareResolveInfoList;
    private List<ShareItem> mShareList;
    private Context mContext;
    private static String mTitle;
    private static String mUrl;

    public static ShareFragment getInstance(String title, String url) {
        mTitle = title;
        mUrl = url;
        return new ShareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fragment_share_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ShareRecyclerViewAdapter adapter = new ShareRecyclerViewAdapter(mContext, R.layout.share_item);
        adapter.setData(mShareList);
        adapter.setClickListener(position -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "我在NiceShop上发现一个好商品：" + "\n" + mTitle + "\n" + mUrl);
            intent.setType("text/plain");
            ActivityInfo activityInfo = mShareResolveInfoList.get(position).activityInfo;
            intent.setClassName(activityInfo.packageName, activityInfo.name);
            startActivity(intent);
            dismiss();
        });
        recyclerView.setAdapter(adapter);

    }

    private void initData() {
        mShareList = new ArrayList<>();
        mShareResolveInfoList = ShareUtil.getShareList(mContext);
        for (int i = 0; i < mShareResolveInfoList.size(); i++) {
            ShareItem item = new ShareItem();
            item.setIcon(mShareResolveInfoList.get(i).loadIcon(mContext.getPackageManager()));
            item.setName(mShareResolveInfoList.get(i).loadLabel(mContext.getPackageManager()).toString());
            mShareList.add(item);
        }
    }
}