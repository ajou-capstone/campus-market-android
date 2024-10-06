package kr.linkerbell.campusmarket.android.data.di

import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.MockAuthenticationRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.token.MockTokenRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking.MockTrackingRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.user.MockUserRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun bindsAuthenticationRepository(
        authenticationRepository: MockAuthenticationRepository
    ): AuthenticationRepository

    @Binds
    @Singleton
    internal abstract fun bindsTokenRepository(
        tokenRepository: MockTokenRepository
    ): TokenRepository

    @Binds
    @Singleton
    internal abstract fun bindsUserRepository(
        userRepository: MockUserRepository
    ): UserRepository

    @Binds
    @Singleton
    internal abstract fun bindsTrackingRepository(
        userRepository: MockTrackingRepository
    ): TrackingRepository
}
