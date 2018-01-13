package com.posun.lightui;

import android.view.View;

/**
 * package kotlinTest:com.qing.lightview.common.PickerViewInterface.class
 * 作者：zyq on 2017/8/8 16:09
 * 邮箱：zyq@posun.com
 */
public interface PickerViewInterface<T> {
    View getView();
    View getOkView();
    View getCancelView();
    T getValue();
}
