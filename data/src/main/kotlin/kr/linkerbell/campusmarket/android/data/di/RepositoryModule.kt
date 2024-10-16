package kr.linkerbell.campusmarket.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.MockAuthenticationRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.RealAuthenticationRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.token.MockTokenRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.token.RealTokenRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.file.MockFileRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.file.RealFileRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.item.ConcreteItemListRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.term.MockTermRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.term.RealTermRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking.MockTrackingRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking.RealTrackingRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.user.MockUserRepository
import kr.linkerbell.campusmarket.android.data.repository.nonfeature.user.RealUserRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SummarizedItemListRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

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
        trackingRepository: RealTrackingRepository
    ): TrackingRepository

    @Binds
    @Singleton
    internal abstract fun bindsTermRepository(
        termRepository: RealTermRepository
    ): TermRepository

    @Binds
    @Singleton
    internal abstract fun bindsFileRepository(
        fileRepository: RealFileRepository
    ): FileRepository

    @Binds
    @Singleton
    internal abstract fun bindsItemListRepository(
        concreteItemListRepository: ConcreteItemListRepository
    ): SummarizedItemListRepository
}
