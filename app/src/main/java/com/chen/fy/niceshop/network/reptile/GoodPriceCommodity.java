package com.chen.fy.niceshop.network.reptile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 爬虫爬取的数据
 */
public class GoodPriceCommodity implements Parcelable {

    // 商品 title
    private String title;
    // 商品图片
    private String imgUrl;
    // 价格
    private String price;
    // 内容详情
    private String content;
    // 跳转url
    private String url;
    // 平台
    private String platform;

    protected GoodPriceCommodity(Parcel in) {
        title = in.readString();
        imgUrl = in.readString();
        price = in.readString();
        content = in.readString();
        url = in.readString();
        platform = in.readString();
    }

    public static final Creator<GoodPriceCommodity> CREATOR = new Creator<GoodPriceCommodity>() {
        @Override
        public GoodPriceCommodity createFromParcel(Parcel in) {
            return new GoodPriceCommodity(in);
        }

        @Override
        public GoodPriceCommodity[] newArray(int size) {
            return new GoodPriceCommodity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imgUrl);
        dest.writeString(price);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(platform);
    }

    public GoodPriceCommodity(String title, String imgUrl, String price
            , String content, String url, String platform) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.price = price;
        this.content = content;
        this.url = url;
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
