package kr.linkerbell.campusmarket.android.data.remote.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import java.util.Optional
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import okhttp3.Interceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {

    @Provides
    @Singleton
    internal fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    @NoAuthHttpClient
    internal fun provideNoAuthHttpClient(
        @ApplicationContext context: Context,
        @DebugInterceptor debugInterceptor: Optional<Interceptor>,
        json: Json
    ): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = false

            engine {
                if (debugInterceptor.isPresent) {
                    addInterceptor(debugInterceptor.get())
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            // TODO : 이거 왜 안 들어가고 있는지 확인
            defaultRequest {
                header("Content-Type", "application/json")
            }
        }
    }

    @Provides
    @Singleton
    @AuthHttpClient
    internal fun provideAuthHttpClient(
        @ApplicationContext context: Context,
        @DebugInterceptor debugInterceptor: Optional<Interceptor>,
        tokenRepository: TokenRepository,
        json: Json
    ): HttpClient {
        return HttpClient(OkHttp) {
            expectSuccess = false

            engine {
                if (debugInterceptor.isPresent) {
                    addInterceptor(debugInterceptor.get())
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenRepository.getAccessToken()
                        val refreshToken = tokenRepository.getRefreshToken()
                        if (accessToken.isEmpty() || refreshToken.isEmpty()) {
                            return@loadTokens null
                        }

                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    }

                    refreshTokens {
                        val refreshToken = tokenRepository.getRefreshToken()
                        if (refreshToken.isEmpty()) {
                            return@refreshTokens null
                        }

                        tokenRepository.refreshToken(
                            refreshToken
                        ).getOrNull()?.let { token ->
                            BearerTokens(
                                accessToken = token.accessToken,
                                refreshToken = token.refreshToken
                            )
                        }
                    }
                }
            }

            install(WebSockets)

            // TODO : 이거 왜 안 들어가고 있는지 확인
            defaultRequest {
                header("Content-Type", "application/json")
            }
        }
    }

    @Provides
    @Singleton
    internal fun provideStompClient(
        @AuthHttpClient authClient: HttpClient
    ): StompClient {
        val webSocketClient = KtorWebSocketClient(authClient)
        val stompClient = StompClient(webSocketClient)

        return stompClient
    }
}

@Qualifier
annotation class NoAuthHttpClient

@Qualifier
annotation class AuthHttpClient
