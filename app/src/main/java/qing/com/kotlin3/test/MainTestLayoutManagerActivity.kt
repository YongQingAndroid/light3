package qing.com.kotlin3.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_test_layout_manager.*
import qing.com.kotlin3.R

class MainTestLayoutManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_test_layout_manager)
        mlist
    }
}
