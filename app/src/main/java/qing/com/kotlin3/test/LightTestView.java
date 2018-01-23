package qing.com.kotlin3.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qing on 2018/1/22.
 */

public class LightTestView extends View {
    private Paint paint;
    private Bitmap bitmap;

    public LightTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI() {
        paint = new Paint();
    }

    public void setView(View view) {
        bitmap = getViewBitmap(view);
        invalidate();
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, mSrcRect, mSrcRect, paint);
        }
        super.onDraw(canvas);
    }
}
