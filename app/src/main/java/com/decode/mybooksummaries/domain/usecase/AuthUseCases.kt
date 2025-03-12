package com.decode.mybooksummaries.domain.usecase

import com.decode.mybooksummaries.domain.usecase.auth.CreateAccountUseCase
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginWithGoogleUseCase
import com.decode.mybooksummaries.domain.usecase.auth.ResetPasswordUseCase
import com.decode.mybooksummaries.domain.usecase.auth.SignOutUseCase

data class AuthUseCases(
    val createAccountUseCase: CreateAccountUseCase,
    val loginUseCase: LoginUseCase,
    val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase,
    val signOutUseCase: SignOutUseCase,
    val currentUserUseCase: CurrentUserUseCase,
)