package com.chen.fy.niceshop.main.home.view.fragment;

import androidx.fragment.app.Fragment;

public class AttentionFragment extends Fragment {
//    private View mView;
//    private Context mContext;
//    private SmartRefreshLayout mRefreshLayout;
//
//    private CommodityAdapter goodsAdapter;
//    private RecyclerView mRecyclerView;
//
//    // 关注列表
//    private ArrayList<Goods> attentionGoodsList;
//
//    // 点赞
//    public static final int DO_LIKE = 2;
//    private Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DO_LIKE:
//                    doLike(msg.arg1);
//                    break;
//            }
//        }
//    };
//
//    /// 点赞刷新
//    private void doLike(int position) {
//        goodsAdapter.notifyItemChanged(position, "payload");
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.recommend_fragment, container, false);
//        return mView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mContext = getContext();
//        ShowUtils.changeStatusBarTextImgColor(getActivity(), false);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        mRecyclerView = mView.findViewById(R.id.rv_home);
//        mRefreshLayout = mView.findViewById(R.id.refreshLayout_found);
//        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);//1 表示列数
//        mRecyclerView.setLayoutManager(layoutManager);
//    }
//
//    private void initData() {
//        attentionGoodsList = new ArrayList<>();
//
//        if (goodsAdapter == null) {
//            goodsAdapter = new CommodityAdapter(mContext, R.layout.commodity_item);
//            goodsAdapter.setGoodsList(attentionGoodsList);
//            mRecyclerView.setAdapter(goodsAdapter);
//        }
//
//        // 顶部下拉刷新`
//        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
//            int userID = UserSP.getUserID();
//            if (userID != Integer.valueOf(RUtil.toString(R.string.not_login))) {
//                getAttentions(userID);
//            } else {
//                mRefreshLayout.finishRefresh();
//                Toast.makeText(getContext(), "请先登录账号", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    /// 刷新
//    public void refresh() {
//        mRefreshLayout.autoRefresh();
//    }
//
//    private void getAttentions(int userID) {
//
//    }
//
//    /// 得到当前关注
//    private void initAttentionHeadline(String attentions) {
//
//    }
}
