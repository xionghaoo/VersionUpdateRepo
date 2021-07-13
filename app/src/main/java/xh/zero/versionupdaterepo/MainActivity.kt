package xh.zero.versionupdaterepo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import xh.zero.ota.UpdateDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var updateDelegate: UpdateDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateDelegate = UpdateDelegate(this)
    }

    fun onClick(v: View) {
        updateDelegate.downloadApk(
            url = "https://dl-tc.coolapkmarket.com/down/apk_file/2021/0629/Coolapk-11.2.6.1-2106291-coolapk-app-sign.apk?t=1626149030&sign=60810fe39b13a8c2e909ac8cad4f7428",
            versionName = "1.0.0",
            content = "有新的版本",
            isForce = false,
            cancel = {

            }
        )
    }
}