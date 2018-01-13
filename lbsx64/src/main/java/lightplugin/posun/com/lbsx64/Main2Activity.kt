package lightplugin.posun.com.lbsx64

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.qing.lighthttp.LightHandlerThread
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        hello.setOnClickListener {
            //            Toast.makeText(this, "111111", Toast.LENGTH_SHORT).show()
            LightHandlerThread.postMsgToMainThread("123456", this::test)
        }
    }

    fun test(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun test(string: String, item:String) {
//        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
//        FMTest().with(this).startforResult(intent) {
//        string-> Log.i("tag", string as String)}
    }
}
