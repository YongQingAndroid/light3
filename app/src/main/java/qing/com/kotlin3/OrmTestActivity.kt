package lightplugin.posun.com.fuchuang

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lightormdatabase.LightOrmHelper
import org.jetbrains.anko.*
import qing.com.kotlin3.R
import qing.com.lightdatabinding.LightDataBingding

class OrmTestActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            button {
                text = "insertDb"
            }.lparams {
                width = matchParent
                height = wrapContent
            }.onClick {
                insertData()
            }
            button {
                text = "query"
            }.lparams {
                width = matchParent
                height = wrapContent
            }.onClick {
                queryData()
            }
            button {
                text = "excuteNet"
            }.lparams {
                width = matchParent
                height = wrapContent
            }.onClick {
                //                executeNet()
            }
            button { text = "notify" }.lparams {
                width = matchParent
                height = wrapContent
            }.onClick {
                sendNotification()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun  sendNotification() {
        var notifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder = Notification.Builder(this)
                .setContentTitle("最简单的Notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("只有小图标、标题、内容")
        notifyManager.notify(0, builder.build())
    }
    fun binding() {
        var binding = LightDataBingding<LightDbBean>()

        binding.bind_id(1, { item -> item.name })
        binding.bind_id(2, { item -> item.sex })
    }

    fun queryData() {
        var list = LightOrmHelper.getInstent().query(LightDbBean::class.java)
        toast("list size=" + list.size)
    }

    fun insertData() {
        var mLightDbBean = LightDbBean("lisa", "sex")
        mLightDbBean.insert()
    }

    fun executeNet() {
//        lighthttp {
//            url = "http://www.163.com/"
//            method = "get"
//            onSuccess { string ->
//                Log.i("light", string)
//            }
//
//            onFail { e ->
//                Log.i("light", e!!.message)
//            }
//
//        }
    }
}
