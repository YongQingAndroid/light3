package qing.com.kotlin3.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qing on 2018/1/21.
 */

public class FrmetDrawable extends Drawable {
    private Paint paint;
    private String text = "555555555555";

    public FrmetDrawable() {
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(18);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(text, 60, 20, paint);
        int size = (int) paint.measureText(text);
        canvas.clipRect(50, 0, 60 + size + 10, 35);
        canvas.clipRect(0, 0, 400, 250, Region.Op.REVERSE_DIFFERENCE);
        RectF rectF = new RectF(10, 10, 400, 200);
        paint.setStrokeWidth(2);
        canvas.drawRoundRect(rectF, 30, 30, paint);
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
