package com.decode.mybooksummaries.domain.usecase

import com.decode.mybooksummaries.domain.usecase.auth.CreateAccountUseCase
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginUseCase
import com.decode.mybooksummaries.domain.usecase.auth.LoginWithGoogleUseCase
import com.decode.mybooksummaries.domain.usecase.auth.ResetPasswordUseCase
import com.decode.mybooksummaries.domain.usecase.auth.SignOutUseCase
import com.decode.mybooksummaries.domain.usecase.auth.UpdateDisplayNameUseCase
import com.decode.mybooksummaries.domain.usecase.auth.UpdatePasswordUseCase

data class AuthUseCases(
    val createAccountUseCase: CreateAccountUseCase,
    val loginUseCase: LoginUseCase,
    val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    val resetPasswordUseCase: ResetPasswordUseCase,
    val signOutUseCase: SignOutUseCase,
    val currentUserUseCase: CurrentUserUseCase,
    val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
    val updatePasswordUseCase: UpdatePasswordUseCase
)