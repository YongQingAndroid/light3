package qing.com.kotlin3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.posun.lightui.recyclerview.event.LightOnItemTouchListener
import com.posun.lightui.recyclerview.lightdefult.LightDefultLayoutManager
import com.posun.lightui.recyclerview.suspension.SuspensionManager
import kotlinx.android.synthetic.main.activity_list.*


class ListActivity : AppCompatActivity() {
    //    var adapter: TestAdapter? = null
    var adapter: GroupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        initUi()
    }

    fun initUi() {
//        adapter = TestAdapter()
//        list.adapter = adapter
        adapter = GroupAdapter()
        recyclerview.adapter = adapter
        var defultLayoutManager = LightDefultLayoutManager(this);
        recyclerview.layoutManager = defultLayoutManager
        recyclerview.addOnItemTouchListener(
                LightOnItemTouchListener(recyclerview, object : LightOnItemTouchListener.OnItemTouchListener<GroupAdapter.Holder> {
                    override fun onItemClick(vh: GroupAdapter.Holder) {
                        Toast.makeText(vh.itemView.context, vh.adapterPosition.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onLongItemClick(vh: GroupAdapter.Holder) {

                    }
                }
                ))
        defultLayoutManager.setSpanSizeLookup { position ->
            if (position % 5 == 0) 2 else 1
        }
//        var mSuspensionManager = SuspensionManager()
//        mSuspensionManager.setRecyclerView(recyclerview)
        btn.setOnClickListener {
            adapter!!.setTest()
            adapter!!.notifyDataSetChanged()
        }
    }
}
