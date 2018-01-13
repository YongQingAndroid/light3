package com.posun.lightui.dragView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
/**
 * 执行下拉拖拽
 * Created by zyq on 2017/1/12.
 */
public class DragFace extends LinearLayout implements DragView {
    public Constant.DragState state = Constant.DragState.NORMAL;
    private DragContentView DragLayout;
    private final int loadingHeight = 130;
    private final int maxHeight = 160;
    public DragFace(Context context,Class<? extends DragContentView> clazz) {
        super(context);
        init(clazz);
    }
    private void init(Class<? extends DragContentView> clazz) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        this.setLayoutParams(layoutParams);
        this.setPadding(0, 0, 0, 0);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        try {
            DragLayout=clazz.newInstance();
            DragLayout.init(getContext());
            this.addView(DragLayout.getView(), lp);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMove(float delta) {
        if(state== Constant.DragState.LOADING||state== Constant.DragState.LOADINGCONTINUE){
            setVisibleLoadingHeight((int) delta);
            return;
        }
        Constant.DragState item_state=state;
        if (delta < maxHeight) {
            state = Constant.DragState.DRAGE;
        } else {
            state = Constant.DragState.DRAGE_DOWN;
        }
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta);
        }
        praseState(false);
        if(item_state!=state){
            DragLayout.stateChange(state);
        }
    }
    @Override
    public void recovery() {
        if(state == Constant.DragState.LOADINGCONTINUE||state == Constant.DragState.LOADING){
            smoothScrollTo(loadingHeight);
            return;
        }
        if (state == Constant.DragState.DRAGE_DOWN) {
            state = Constant.DragState.LOADING;
            praseState(true);
            DragLayout.stateChange(state);
        }else{
            praseState(true);
            state = Constant.DragState.NORMAL;
        }
    }
    @Override
    public void praseState(boolean isrecovery) {
        switch (state) {
            case NORMAL:
                smoothScrollTo(0);
                break;
            case DRAGE:
                if(isrecovery){
                    smoothScrollTo(0);
                }
                break;
            case DRAGE_DOWN:
                break;
            case LOADING:
                smoothScrollTo(loadingHeight);
                break;
            case  FINISH:
                smoothScrollTo(0);
                DragLayout.stateChange(state);
                state = Constant.DragState.NORMAL;
                break;
        }
    }


    @Override
    public void continueAnimation() {
        DragLayout.stateChange(Constant.DragState.LOADINGCONTINUE);
    }

    @Override
    public void praseDragFaceState() {
        praseState(true);
        DragLayout.stateChange(state);
    }

    public int getVisibleHeight() {
        ViewGroup.LayoutParams lp =DragLayout.getView().getLayoutParams();
        return lp.height;
    }
    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(350).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        ViewGroup.LayoutParams lp =  DragLayout.getView().getLayoutParams();
        lp.height = height;
        DragLayout.getView().setLayoutParams(lp);
        DragLayout.drage(height);
    }
    public void setVisibleLoadingHeight(int height) {
        ViewGroup.LayoutParams lp =  DragLayout.getView().getLayoutParams();
        lp.height = height+loadingHeight;
        DragLayout.getView().setLayoutParams(lp);
    }
}
