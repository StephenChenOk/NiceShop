package com.chen.fy.niceshop.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.fragment.app.FragmentManager;
import com.chen.fy.niceshop.main.home.detail.share.ShareFragment;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShareUtil {

    public static void share(FragmentManager fragmentManager, String title, String url) {
        ShareFragment.getInstance(title, url).show(fragmentManager, "dialog");
    }

    /// 获取手机中可接收我们分享的数据的APP集合
    public static List<ResolveInfo> getShareList(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);

        //调整顺序，把微信、QQ提到前面来
        Collections.sort(list, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo r1, ResolveInfo r2) {
                ActivityInfo activityInfo1 = r1.activityInfo;
                ActivityInfo activityInfo2 = r2.activityInfo;
                if (activityInfo1.packageName.contains("com.tencent.")
                        && !activityInfo2.packageName.contains("com.tencent.")) {
                    return -1;
                } else if (!activityInfo1.packageName.contains("com.tencent.")
                        && activityInfo2.packageName.contains("com.tencent.")) {
                    return 1;
                }
                return 0;
            }
        });

        return list;
    }
}
