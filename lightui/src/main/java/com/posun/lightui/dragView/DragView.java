package com.posun.lightui.dragView;

/**
 * Created by dell on 2017/1/12.
 */
public interface DragView {
    void onMove(float delta);
    void recovery();
    void praseState(boolean isrecovery);
    void continueAnimation();
    void praseDragFaceState();
}
