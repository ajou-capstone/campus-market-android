package kr.linkerbell.campusmarket.android

import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.SetFcmTokenUseCase
import kr.linkerbell.campusmarket.android.presentation.common.util.showNotification

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

        showNotification(
            channelId = getString(kr.linkerbell.campusmarket.android.presentation.R.string.channel_campus_market),
            notificationId = message.sentTime.toInt(),
            title = message.notification?.title,
            content = message.notification?.body,
            icon = kr.linkerbell.campusmarket.android.presentation.R.drawable.ic_launcher,
            group = null,
            pendingIntent = null,
            priority = NotificationCompat.PRIORITY_DEFAULT
        )
    }
}
