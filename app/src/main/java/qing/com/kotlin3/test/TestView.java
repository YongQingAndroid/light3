package qing.com.kotlin3.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * package light3:qing.com.kotlin3.test.TestView.class
 * 作者：zyq on 2018/1/22 11:45
 * 邮箱：zyq@posun.com
 */

public class TestView extends View{
    Paint paint;
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
    }
    private void triangle(Canvas canvas) {
        Path path = new Path();
        int x=200,y=30;
        path.moveTo(x, 0);
        path.lineTo(x+(y/2), (y/2));
        path.lineTo(x+y, 0);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        triangle(canvas);
        super.onDraw(canvas);
    }
}
