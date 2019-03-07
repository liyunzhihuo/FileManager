package com.xiao.filemanager.bga_photopicker.adapter;

import android.view.MotionEvent;
import android.view.View;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/28 上午7:28
 * 描述:RecyclerView的item中子控件触摸事件监听器
 */
public interface BGAOnRVItemChildTouchListener {
    boolean onRvItemChildTouch(BGARecyclerViewHolder holder, View childView, MotionEvent event);
}