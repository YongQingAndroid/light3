package qing.com.kotlin3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.posun.lightui.richView.drawbale.LabTextDrawable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        overridePendingTransition()
//        edtext.backgroundDrawable= LabTextDrawable()
    }

    fun something(string: String) {

    }
}
