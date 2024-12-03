package kr.linkerbell.campusmarket.android

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.SetFcmTokenUseCase
import kr.linkerbell.campusmarket.android.presentation.common.DOMAIN
import kr.linkerbell.campusmarket.android.presentation.common.util.showNotification
import kr.linkerbell.campusmarket.android.presentation.ui.main.MainActivity

@AndroidEntryPoint
class CampusMarketFcmService : FirebaseMessagingService() {

    @Inject
    lateinit var setFcmTokenUseCase: SetFcmTokenUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        runBlocking {
            setFcmTokenUseCase(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        fun getDeeplinkPendingIntent(): PendingIntent? {
            return message.data["deeplink"]
                ?.takeIf { it.startsWith(DOMAIN) }
                ?.runCatching { toUri() }
                ?.getOrNull()
                ?.let { uri ->
                    Intent(
                        Intent.ACTION_VIEW,
                        uri,
                        this@CampusMarketFcmService,
                        MainActivity::class.java
                    )
                }?.let { intent ->
                    TaskStackBuilder.create(this@CampusMarketFcmService)
                        .addNextIntentWithParentStack(intent)
                        .getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                }
        }

        showNotification(
            channelId = getString(kr.linkerbell.campusmarket.android.presentation.R.string.channel_campus_market),
            notificationId = message.sentTime.toInt(),
            title = message.notification?.title,
            content = message.notification?.body,
            icon = kr.linkerbell.campusmarket.android.presentation.R.drawable.ic_launcher,
            group = null,
            pendingIntent = getDeeplinkPendingIntent(),
            priority = NotificationCompat.PRIORITY_DEFAULT
        )
    }
}
