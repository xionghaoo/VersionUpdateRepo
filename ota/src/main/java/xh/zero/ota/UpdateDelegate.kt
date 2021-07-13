package xh.zero.ota

import android.app.Activity
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class UpdateDelegate(private val context: Activity) : DownloadService.DownloadCallback {

    private lateinit var mService: DownloadService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as DownloadService.DownloadBinder
            mService = binder.getService()
            if (mService.isForceUpdate == true) {
                mService.setDownloadCallback(this@UpdateDelegate)
            }
            mService.downloadApk()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    private var downloadDialog: AlertDialog? = null
    private var tvDownload: TextView? = null
    private var progressBar: ProgressBar? = null
    private var btnConfirm: TextView? = null
    private var urlReinstall: String? = null

    fun downloadApk(url: String, versionName: String?, content: String?, isForce: Boolean?, cancel: () -> Unit) {
        urlReinstall = url
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("发现新的版本$versionName")
            .setMessage(content)
            .setCancelable(false)
            .setPositiveButton("立即更新") { _, _ ->
                DownloadService.startService(context, connection, url, isForce)
            }
        dialog.setNegativeButton(if (isForce == true) "退出App" else "取消") { _, _ ->
            if (isForce == true) { cancel() }
        }
        dialog.show()
    }

    override fun onPrepare() {
        val view = LayoutInflater.from(context).inflate(R.layout.common_dialog_update, null)
        tvDownload = view.findViewById(R.id.common_tv_download)
        progressBar = view.findViewById(R.id.common_progress_bar)
        btnConfirm = view.findViewById(R.id.common_btn_confirm)
        downloadDialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()
        downloadDialog?.show()
    }

    override fun onProgress(progress: Int) {
        context.runOnUiThread {
            tvDownload?.text = "下载进度${progress}%"
            progressBar?.progress = progress
        }
    }

    override fun onComplete() {
        context.runOnUiThread {
            tvDownload?.text = "下载完成"
            btnConfirm?.text = "点击安装"
            btnConfirm?.setOnClickListener {
                if (mService.reInstallApk != null) {
                    mService.installApk(mService.reInstallApk!!)
                }
            }
        }
        DownloadService.stopService(context, connection)
    }

    override fun onError(error: String) {
        context.runOnUiThread {
            tvDownload?.text = "下载失败，请重新下载"
            btnConfirm?.text = "重新下载"
            btnConfirm?.setOnClickListener {
                downloadDialog?.dismiss()
                DownloadService.startService(context, connection, urlReinstall, mService.isForceUpdate)
            }
        }
        DownloadService.stopService(context, connection)
    }
}