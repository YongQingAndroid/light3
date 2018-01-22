package com.posun.lightui.richView.drawbale;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by qing on 2018/1/21.
 */

public class LabTextDrawable extends Drawable {
    private Paint paint;
    private String text = "user Name";
    private int round = 8, textSize = 18, space = 10, color = android.graphics.Color.BLACK;
    private Bitmap bitmapbg;

    public LabTextDrawable() {
        init();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        paint.setTextSize(textSize);
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(2);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect rect = getBounds();
        getBitMap(rect);
        if (bitmapbg != null) {
            canvas.drawBitmap(bitmapbg, rect, rect, paint);
        }
    }

    private void getBitMap(Rect rect) {
        bitmapbg = Bitmap.createBitmap(rect.right, rect.bottom, Bitmap.Config.ARGB_8888);
        Canvas bitmapcanvas = new Canvas(bitmapbg);
        int left = rect.left;
        int right = rect.right;
        int bottom = rect.bottom;
        int top = rect.top;
        paint.setStyle(Paint.Style.FILL);
        int size = (int) paint.measureText(text);
        bitmapcanvas.drawText(text, round * 2 + left + space, top + textSize, paint);
        int start = (left + round * 2);
        bitmapcanvas.clipRect(start, top, start + size + (space * 2), textSize);
        bitmapcanvas.clipRect(rect, Region.Op.REVERSE_DIFFERENCE);
        RectF rectF = new RectF(left + 2, top + (textSize / 2), right, bottom - 2);
        paint.setStyle(Paint.Style.STROKE);
        bitmapcanvas.drawRoundRect(rectF, round, round, paint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }
}
