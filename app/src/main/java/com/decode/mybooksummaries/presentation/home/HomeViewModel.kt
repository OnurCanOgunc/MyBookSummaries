package com.decode.mybooksummaries.presentation.home

import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.presentation.home.HomeContract.UiAction
import com.decode.mybooksummaries.presentation.home.HomeContract.UiEffect
import com.decode.mybooksummaries.presentation.home.HomeContract.UiEffect.*
import com.decode.mybooksummaries.presentation.home.HomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookUseCase: BookUseCases,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel<UiState, UiAction, UiEffect>(
    connectivityObserver,
    UiState()
) {

    override fun onAction(uiAction: UiAction) { viewModelScope.launch {
        when (uiAction) {
            UiAction.OnAddBookClick -> {
                emitUiEffect(NavigateToAddBook)
            }

            is UiAction.OnBookClick -> {
                emitUiEffect(NavigateToBookDetails(uiAction.id))
            }

            UiAction.OnSearchClick -> searchBooks()
            UiAction.ClearSearchQuery -> {
                updateUiState { copy(searchQuery = "", searchResults = emptyList()) }
                getBooks()
            }

            is UiAction.UpdateSearchQuery -> {
                updateUiState { copy(searchQuery = uiAction.query) }
            }

            UiAction.OnProfileClick -> {
                emitUiEffect(NavigateToProfile)
            }
        }
    }
    }

    override suspend fun initialDataLoad() {
        getBooks()
    }

    private fun getBooks() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, books = emptyList()) }
            isConnected.collectLatest { connected ->
                bookUseCase.getBooks(connected).collect { response ->
                    when (response) {
                        is Response.Success -> {
                            updateUiState { copy(books = response.data, isLoading = false) }
                        }

                        is Response.Failure -> {
                            updateUiState { copy(error = response.message, isLoading = false) }
                        }

                        is Response.Empty -> {
                            updateUiState { copy(error = "No books found", isLoading = false) }
                        }
                    }
                }

            }

        }
    }

    private fun searchBooks() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, searchResults = emptyList()) }
            bookUseCase.getSearchBooks(uiState.value.searchQuery).collect { response ->
                updateUiState {
                    when (response) {
                        is Response.Success -> copy(
                            searchResults = response.data,
                            isLoading = false
                        )

                        is Response.Failure -> copy(
                            error = response.message,
                            searchResults = emptyList(),
                            isLoading = false
                        )

                        is Response.Empty -> copy(searchResults = emptyList(), isLoading = false)
                    }
                }
            }
        }
    }
}