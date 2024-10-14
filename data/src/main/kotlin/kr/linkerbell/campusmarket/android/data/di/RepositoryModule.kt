package kr.linkerbell.campusmarket.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.RealAuthenticationRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.token.RealTokenRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking.RealTrackingRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.user.RealUserRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindsAuthenticationRepository(
        authenticationRepository: RealAuthenticationRepository
    ): AuthenticationRepository

    @Binds
    @Singleton
    internal abstract fun bindsTokenRepository(
        tokenRepository: RealTokenRepository
    ): TokenRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserRepository(
        userRepository: RealUserRepository
    ): UserRepository

    @Binds
    @Singleton
    internal abstract fun bindsTrackingRepository(
        userRepository: RealTrackingRepository
    ): TrackingRepository
}
