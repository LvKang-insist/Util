package com.admin.utill.dimen;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Copyright (C)
 *
 * @file: DimenUtil
 * @author: 345
 * @Time: 2019/4/18 16:57
 * @description: 测量
 */
public class DimenUtil {
    /**
     * @return 可用显示大小的绝对宽度(以像素为单位)
     */
    public static int getScreenWidth(Context context){
        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();

        return dm.widthPixels;
    }
    /**
     * @return 可用显示大小的绝对高度(以像素为单位)
     */
    public static int getScreenHeight(Context context){
        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
