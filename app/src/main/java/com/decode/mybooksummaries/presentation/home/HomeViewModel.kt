package com.decode.mybooksummaries.presentation.home

import android.util.Log
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

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
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

                is UiAction.OnCategorySelected -> getBooksByCategory(uiAction.category)
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
                            Log.d("HomeViewModel", "getBooks: ${response.data.size}")
                            updateUiState { copy(books = response.data, isLoading = false) }
                        }

                        is Response.Failure -> {
                            Log.d("HomeViewModel", "getBooks: ${response.message}")
                            updateUiState { copy(error = response.message, isLoading = false) }
                        }

                        is Response.Empty -> {
                            Log.d("HomeViewModel", "getBooks: Empty")
                            updateUiState {
                                copy(
                                    error = "No books found",
                                    isLoading = false,
                                    books = emptyList()
                                )
                            }
                        }
                    }
                }

            }

        }
    }

    private fun searchBooks() {
        Log.d("HomeViewModel", "searchBooks: ${uiState.value.searchQuery}")
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, searchResults = emptyList()) }
            bookUseCase.getSearchBooks(uiState.value.searchQuery, isConnected.value)
                .collect { response ->
                    updateUiState {
                        when (response) {
                            is Response.Success -> {
                                Log.d("HomeViewModel", "searchBooks: ${response.data}")
                                copy(
                                    searchResults = response.data,
                                    isLoading = false
                                )
                            }

                            is Response.Failure -> {
                                Log.d("HomeViewModel", "searchBooks: ${response.message}")
                                copy(
                                    error = response.message,
                                    searchResults = emptyList(),
                                    isLoading = false
                                )
                            }

                            is Response.Empty -> {
                                Log.d("HomeViewModel", "searchBooks: Empty")
                                copy(searchResults = emptyList(), isLoading = false)
                            }
                        }
                    }
                }
        }
    }

    private fun getBooksByCategory(selectedCategory: String) {
        viewModelScope.launch {
            updateUiState {
                copy(
                    isLoading = true,
                    books = emptyList(),
                    selectedCategory = selectedCategory
                )
            }
            if (selectedCategory == "All") getBooks()
            bookUseCase.getBooksByCategory(selectedCategory, isConnected.value)
                .collect { response ->
                    when (response) {
                        is Response.Success -> {
                            Log.d("HomeViewModel", "getBooksByCategory: ${response.data.size}")
                            updateUiState { copy(books = response.data, isLoading = false) }
                        }

                        is Response.Failure -> {
                            Log.d("HomeViewModel", "getBooksByCategory: ${response.message}")
                            updateUiState { copy(error = response.message, isLoading = false) }
                        }

                        is Response.Empty -> {
                            Log.d("HomeViewModel", "getBooksByCategory: Empty")
                            updateUiState {
                                copy(
                                    error = "No books found",
                                    isLoading = false,
                                    books = emptyList()
                                )
                            }
                        }
                    }
                }
        }
    }
}