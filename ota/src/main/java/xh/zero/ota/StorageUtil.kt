package xh.destiny.core.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.IOException

import android.os.Environment.MEDIA_MOUNTED

/**
 * Provides application storage paths
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
internal object StorageUtil {

    private const val EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE"

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * *("/Android/data/[app_package_name]/cache")* if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application mContext
     * @return Cache [directory][File]
     */
    fun getCacheDirectory(context: Context): File {
        var appCacheDir: File? = null
        if (MEDIA_MOUNTED == Environment.getExternalStorageState() && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            //7.0以上如果不给外部存储卡读写权限，那么app下载到内部files目录下，FileProvider默认内部共享files目录
            appCacheDir = File(context.cacheDir, "ApkFiles")
            if (!appCacheDir.exists()) {
                appCacheDir.mkdir()
            }
        }
        if (appCacheDir == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.")
        }
        return appCacheDir
    }


    private fun getExternalCacheDir(context: Context): File? {
        //        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        //        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        val appCacheDir = File(context.externalCacheDir, "ApkFiles")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w("StorageUtils", "Unable to create external cache directory")
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
                Log.i("StorageUtils", "Can't create \".nomedia\" file in application external cache directory")
            }

        }
        return appCacheDir
    }

    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }
}
