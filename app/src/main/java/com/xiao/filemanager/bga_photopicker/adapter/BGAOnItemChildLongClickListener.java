package com.xiao.filemanager.bga_photopicker.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/28 上午7:28
 * 描述:AdapterView和RecyclerView的item中子控件长按事件监听器
 */
public interface BGAOnItemChildLongClickListener {
    boolean onItemChildLongClick(ViewGroup parent, View childView, int position);
}