package com.chen.fy.niceshop.network.reptile;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GetReptileData {

    /**
     * 好价搜索爬虫
     */
    public static List<GoodPriceCommodity> spiderSearchInfo(String html) {
        List<GoodPriceCommodity> list = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements elements = document
                .select("ul[id=feed-main-list]")
                .select("li[class=feed-row-wide ]");
        for (Element element : elements) {
            String title = element
                    .select("h5[class=feed-block-title]")
                    .text();

            String pic_url_ = element
                    .select("div[class=z-feed-img]")
                    .select("a")
                    .select("img")
                    .attr("src");
            String pic_url = "http:" + pic_url_;

            String price = element
                    .select("h5[class=feed-block-title]")
                    .select("a")
                    .select("div[class=z-highlight]")
                    .text();

            String content = element
                    .select("div[class=feed-block-descripe]")
                    .text();

            String url = element
                    .select("div[class=z-feed-foot-r]")
                    .select("div[class=feed-link-btn]")
                    .select("div[class=feed-link-btn-inner]")
                    .select("a")
                    .attr("href");

            String platform_ = element
                    .select("div[class=z-feed-foot-r]")
                    .select("span[class=feed-block-extras]")
                    .text();
            String[] ps = platform_.split(" ");
            String platform = ps[ps.length - 1];

            GoodPriceCommodity commodity =
                    new GoodPriceCommodity(title, pic_url, price, content, url, platform);
            list.add(commodity);
            Log.e("DATA>>", commodity.toString());
        }
        Log.e("DATA>>", list.toString());
        return list;
    }

    /**
     * 好价首页爬虫
     */
    public static List<GoodPriceCommodity> spiderGoodPrice(String html) {
        List<GoodPriceCommodity> list = new ArrayList<>();

        Document document = Jsoup.parse(html);
        Elements elements = document
                .select("ul[id=feed-main-list]")
                .select("li[class=J_feed_za feed-row-wide]");
        for (Element element : elements) {
            String title = element
                    .select("h5[class=feed-block-title]")
                    .text();
            String pic_url = element
                    .select("div[class=z-feed-img]")
                    .select("a")
                    .select("img")
                    .attr("src");
            String price = element
                    .select("a[class=z-highlight]")
                    .text();
            String content = element
                    .select("div[class=feed-block-descripe]")
                    .text();
            String url = element
                    .select("div[class=z-feed-foot-r]")
                    .select("div[class=feed-link-btn]")
                    .select("div[class=feed-link-btn-inner]")
                    .select("a")
                    .attr("href");
            String platform = element
                    .select("div[class=z-feed-foot-r]")
                    .select("span[class=feed-block-extras]")
                    .select("a")
                    .text();

            GoodPriceCommodity commodity =
                    new GoodPriceCommodity(title, pic_url, price, content, url, platform);
            list.add(commodity);
            Log.e("DATA>>", commodity.toString());
        }
        Log.e("DATA>>", list.toString());
        return list;
    }
}
