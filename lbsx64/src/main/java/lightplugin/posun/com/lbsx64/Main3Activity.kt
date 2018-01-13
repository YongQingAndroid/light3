package lightplugin.posun.com.lbsx64

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        click.setOnClickListener({view ->
            setResult(Activity.RESULT_OK, Intent().putExtra("name","tom"))
            finish()
        })
    }
}
