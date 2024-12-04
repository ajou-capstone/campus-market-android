package kr.linkerbell.campusmarket.android.presentation.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            MainScreen(
                navController = navController
            )

            intent.extras?.getString("deeplink")?.toUri()?.let {
                navController.navigate(deepLink = it)
            }
        }
    }
}
