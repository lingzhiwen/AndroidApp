package com.ling.common.callback;

/**
 * Created by ling on 2020/7/1 13:41
 */
public interface TabSelectListener {

    /**
     * 选中
     */
    void onSelect(int index, int totalCount);

    /**
     * 未选中
     */
    void onDeselected(int index, int totalCount);
}
