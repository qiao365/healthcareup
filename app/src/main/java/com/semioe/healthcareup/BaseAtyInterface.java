package com.semioe.healthcareup;

import android.content.Context;
import android.view.View;

/**
 * Created by Ebon-lax on 16/9/23.
 */

public interface BaseAtyInterface {
    /**
     * 绑定渲染视图的布局文件
     *
     * @return 布局文件资源id
     */
    int bindLayout();

    /**
     * 初始化控件
     */
    void initView(final View view);

    /**
     * 业务处理操作（onCreate方法中调用）
     *
     * @param mContext
     *            当前Activity对象
     */
    void doBusiness(Context mContext);

}
