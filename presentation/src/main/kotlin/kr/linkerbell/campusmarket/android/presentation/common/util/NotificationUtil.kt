package kr.linkerbell.campusmarket.android.presentation.common.util

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kr.linkerbell.campusmarket.android.presentation.R

fun Context.showNotification(
    channelId: String,
    notificationId: Int,
    title: String? = null,
    content: String? = null,
    @DrawableRes icon: Int = R.drawable.ic_launcher,
    group: String? = null,
    pendingIntent: PendingIntent? = null,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT
): Boolean {

    fun Context.checkNotificationPermissionGranted(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun Context.checkNotificationChannelEnabled(
        id: String
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val channel = manager.getNotificationChannel(id) ?: return false
            val isChannelEnabled = channel.importance != NotificationManager.IMPORTANCE_NONE

            if (!isChannelEnabled) {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, id)
                }
                startActivity(intent)
            }
            return isChannelEnabled
        } else {
            true
        }
    }

    val isPermissionGranted = checkNotificationPermissionGranted()
    val isChannelEnabled = checkNotificationChannelEnabled(channelId)

    if (!isPermissionGranted || !isChannelEnabled) return false

    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(priority)
        .setGroup(group)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    NotificationManagerCompat.from(this)
        .notify(notificationId, notification)

    return true
}
