package com.chen.fy.niceshop.main.look.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chen.fy.niceshop.R;

import java.util.List;

public class ShareGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> mUriList;

    private boolean isAdd = true;

    public ShareGridViewAdapter(Context context) {
        mContext = context;
    }

    public void setUris(List<Object> list) {
        mUriList = list;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    /**
     * 决定了ListView中一共有多少个Item
     */
    @Override
    public int getCount() {
        return (mUriList == null) ? 0 : mUriList.size();
    }

    /**
     * 它也不会被自动调用，它是用来在我们设置setOnItemClickListener、setOnItemLongClickListener、
     * setOnItemSelectedListener的点击选择处理事件中方便地调用来获取当前行数据的。
     * 官方解释:Implementers can call getItemAtPosition(position) if they need to access the data
     */
    @Override
    public Object getItem(int position) {
        return mUriList.get(position);
    }

    /**
     * 返回的是该position对应item的id,adapterView也有类似方法：
     * 某些方法（如onClickListener的onclick方法）有id这个参数，而这个id参数就是取决于getItemId()这个返回值的
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 决定了每个Item布局所显示的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder viewHolder;
        if (convertView == null) {      //判断缓冲池是否已经有view ,若有则可以直接用,不需要再继续反射
            view = LayoutInflater.from(mContext).inflate(R.layout.gv_publish_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = view.findViewById(R.id.gv_publish_iv_picture);

            view.setTag(viewHolder);
        } else {    //若缓冲池中已经有view则可以直接用holder对象
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        if (isAdd && position == mUriList.size() - 1) {
            Glide.with(mContext)
                    .load(String.valueOf(mUriList.get(position)))
                    .apply(new RequestOptions().fitCenter())
                    .into(viewHolder.ivPicture);
        } else if (position != mUriList.size()) {
            Glide.with(mContext)
                    .load(mUriList.get(position))
                    .into(viewHolder.ivPicture);
        }

        return view;
    }

    //创建一个内部类,放着要显示的view控件,通过实例化这个类,把其对象一起放到view中
    class ViewHolder {
        ImageView ivPicture;
    }

}
