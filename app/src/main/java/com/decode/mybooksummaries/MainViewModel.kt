package com.decode.mybooksummaries

import androidx.lifecycle.ViewModel
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    currentUser: CurrentUserUseCase,
) : ViewModel() {
    val authUser = currentUser.getCurrentUser() == null

}