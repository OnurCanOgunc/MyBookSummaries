package com.decode.mybooksummaries.di

import com.decode.mybooksummaries.data.repository.AuthRepositoryImpl
import com.decode.mybooksummaries.domain.repository.AuthRepository
import com.decode.mybooksummaries.domain.usecase.AuthUseCases
import com.decode.mybooksummaries.domain.usecase.auth.CreateAccountUseCase
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginWithGoogleUseCase
import com.decode.mybooksummaries.domain.usecase.auth.ResetPasswordUseCase
import com.decode.mybooksummaries.domain.usecase.auth.SignOutUseCase
import com.decode.mybooksummaries.domain.usecase.auth.UpdateDisplayNameUseCase
import com.decode.mybooksummaries.domain.usecase.auth.UpdatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl


    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository
    ) = AuthUseCases(
        createAccountUseCase = CreateAccountUseCase(authRepository),
        loginUseCase = LoginUseCase(authRepository),
        loginWithGoogleUseCase = LoginWithGoogleUseCase(authRepository),
        resetPasswordUseCase = ResetPasswordUseCase(authRepository),
        signOutUseCase = SignOutUseCase(authRepository),
        currentUserUseCase = CurrentUserUseCase(authRepository),
        updateDisplayNameUseCase = UpdateDisplayNameUseCase(authRepository),
        updatePasswordUseCase = UpdatePasswordUseCase(authRepository)
    )
}