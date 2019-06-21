package com.admin.utill.dimen;

import android.content.Context;
import android.widget.Toolbar;


/**
 * Copyright (C)
 *
 * @file: SetToolBar
 * @author: 345
 * @Time: 2019/5/5 21:23
 * @description: ${DESCRIPTION}
 */
public class SetToolBar {
    public static void setToolBar(Toolbar mToolbar , Context context) {
        //获取 状态栏的高度，以像素为单位
        int statusBar = StatusBarHeight.getStaticBarHeight(context);
        //设置toolbar 位于 状态栏 的下方，并且距底部 20个像素
        //注意：toolbar的布局中高度只能是 wrap_content
        mToolbar.setPadding(0, statusBar, 0, 20);
    }
}
