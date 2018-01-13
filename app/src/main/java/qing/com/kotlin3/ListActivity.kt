package qing.com.kotlin3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    //    var adapter: TestAdapter? = null
    var adapter: Radapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initUi()
    }

    fun initUi() {
//        adapter = TestAdapter()
//        list.adapter = adapter
        adapter = Radapter()
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
}
