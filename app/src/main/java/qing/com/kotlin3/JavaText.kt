package qing.com.kotlin3

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.support.annotation.RequiresApi

/**
 * package Kotlin3:qing.com.kotlin3.JavaText.class
 * 作者：zyq on 2017/11/22 10:22
 * 邮箱：zyq@posun.com
 */

@RequiresApi(api = Build.VERSION_CODES.M)
class JavaText {
    init {
        val callback = object : FingerprintManager.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }
    }
}
