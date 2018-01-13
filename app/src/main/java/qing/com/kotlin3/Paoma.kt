package qing.com.kotlin3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import android.util.DisplayMetrics
import android.view.WindowManager


/**
 * Created by dell on 2017/11/9.
 */

@SuppressLint("AppCompatCustomView")
class Paoma(context: Context, attrs: AttributeSet?) : TextView(context, attrs) {
    var paint: Paint
    private var isfist = true
    private var i = 10.0f
    private var w = 500.0f
    private var aa = 0.0f

    init {
        paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 30f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(text.toString(), i, 30f, paint)
        if (isfist) {
            aa = paint.measureText(text.toString())
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            w = dm.widthPixels.toFloat()
            startAm()
        }

    }
    private fun startAm() {
        isfist = false
        Thread {
            var add = 3
            while (true) {
                i += add
                postInvalidate()
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (i >= (w  - add)) {
                    i = 0 - aa
                }
            }
        }.start()
    }
}
