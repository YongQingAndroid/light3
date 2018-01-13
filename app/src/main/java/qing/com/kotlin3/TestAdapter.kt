package qing.com.kotlin3
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.jetbrains.anko.textColor
import qing.com.lightdatabinding.LightDataBingding

/**
 * Created by dell on 2017/11/9.
 */
class TestAdapter:BaseAdapter(){
    private var colors:IntArray=intArrayOf(Color.RED,Color.BLUE)
    var string_array:Array<String> = arrayOf("How", "Are", "You","How", "Are", "You","How", "Are", "You","How"
            , "Are", "You","How", "Are", "You","How", "Are", "You","How", "Are", "You")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var binding : LightDataBingding<String>
        var view=p1
        if(view==null){
            view=LayoutInflater.from(p2!!.context).inflate(R.layout.list_item,null)
            binding=LightDataBingding()
            binding.bind_viewGroup(view)
            binding.bind_holder(R.id.text_item,{data: String? ->  data})
            view!!.tag=binding
        }else{
            binding=view!!.tag as LightDataBingding<String>
        }
        binding.getView<TextView>(R.id.text_item)!!.setTextColor(Color.RED)
        binding!!.dataNotify(string_array[p0])
        return view
    }

    override fun getItem(p0: Int): Any {
        return string_array[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return string_array.size
    }

}