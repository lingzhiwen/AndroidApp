package com.ling.common.callback;

import androidx.fragment.app.Fragment;

/**
 * Created by ling on 2020/7/1 11:24
 */
public interface TabPagerListener {

    Fragment getFragment(int position);
}
