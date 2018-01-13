package qing.com.kotlin3

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.posun.lightui.MaterialDBCityPIckerView
import com.posun.lightui.citypicker.CityDataSource
import com.posun.lightui.citypicker.LightDialog
import com.posun.lightui.dragView.LightDrageView
import com.posun.lightui.timePicker.MaterialTimePickerLayout
import com.posun.lightui.timePicker.calender.LightCalenderView
import kotlinx.android.synthetic.main.activity_light_ui.*
import org.jetbrains.anko.toast
import org.joda.time.DateTime

class LightUIActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_ui)
        timepicker.setOnClickListener {
            var m = MaterialTimePickerLayout(this)
            LightDialog.MakeDialog(m.view, LightDialog.QGriavty.BOTTOM).show()
        }
        citypicker.setOnClickListener {
            var cview = MaterialDBCityPIckerView(this)
            CityDataSource.configFile = "qing/com/kotlin3/souce/city.db"
            LightDialog.MakeDialog(cview, LightDialog.QGriavty.BOTTOM).show()
        }
        var ismoth = false
        finger.setOnClickListener {
            lightcalenderview.isIsmonth = ismoth
            ismoth = !ismoth
//            MeterialCityDialog(this).show()
        }
        lightcalenderview.setListener(object : LightCalenderView.LightListener {
            override fun DateChange(dateTime: DateTime) {
//                  toast(dateTime.toString("yyyy-MM-dd"))
            }

            override fun select(dateTime: DateTime) {

            }
        })
        lightdrageview.addItems(*LightDrageView.ItemBuilder().addLableString(" add ", " remove ").build(this))
        lightdrageview.submit()

//        finger.setOnClickListener{
//            var fingerprintManager: FingerprintManager? = null
//            try {
//                fingerprintManager = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
//                fingerprintManager.authenticate(null, null, 0, callback, null)
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            }
//        Thread {
//            var view = BaseCalenderView(this)
//        }.start()
    }

    fun something(string: String) {
        toast(string)
    }
}
//    val callback = object : FingerprintManager.AuthenticationCallback() {
//        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//            super.onAuthenticationError(errorCode, errString)
//            toast(errString)
//        }
//
//        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
//            super.onAuthenticationHelp(helpCode, helpString)
//            toast(helpString)
//        }
//
//        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
//            super.onAuthenticationSucceeded(result)
//            toast("成功")
//        }
//
//        override fun onAuthenticationFailed() {
//            super.onAuthenticationFailed()
//            toast("失败")
//        }
//}
//}
