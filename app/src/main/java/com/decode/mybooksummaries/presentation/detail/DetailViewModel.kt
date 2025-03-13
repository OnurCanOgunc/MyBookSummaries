package com.decode.mybooksummaries.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.domain.usecase.QuoteUseCases
import com.decode.mybooksummaries.presentation.detail.DetailContract.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookUseCase: BookUseCases,
    private val quoteUseCase: QuoteUseCases,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel<DetailContract.UiState, DetailContract.UiAction, UiEffect>(
    connectivityObserver,
    DetailContract.UiState()
) {

    override fun onAction(uiAction: DetailContract.UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                DetailContract.UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is DetailContract.UiAction.AddQuote -> addQuote(uiAction.quote)
                is DetailContract.UiAction.LoadBook -> getBook(uiAction.bookId)
                is DetailContract.UiAction.LoadQuotes -> getQuotes(uiAction.bookId)
                is DetailContract.UiAction.DeleteQuote -> deleteQuote(uiAction.quote)
                is DetailContract.UiAction.QuoteChange -> updateUiState { copy(quote = uiAction.quote) }
                is DetailContract.UiAction.OnEditClick -> {
                    updateUiState { copy(expanded = false) }
                    delay(100)
                    emitUiEffect(UiEffect.NavigateToEdit)
                }

                is DetailContract.UiAction.OnDeleteClick -> deleteBook(uiAction.bookId)
                DetailContract.UiAction.OnDismissRequest -> updateUiState { copy(expanded = false) }
                DetailContract.UiAction.OnMoveCartClick -> updateUiState { copy(expanded = !expanded) }

            }
        }
    }


    private fun getBook(id: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = bookUseCase.getBookById(id, connected)) {
                    is Response.Success -> {
                        updateUiState { copy(book = result.data, isLoading = false) }
                    }

                    is Response.Failure -> {
                        updateUiState { copy(error = result.message, isLoading = false) }
                    }

                    Response.Empty -> {
                        updateUiState { copy(error = "Book not found", isLoading = false) }
                    }
                }
            }

        }
    }

    private fun addQuote(quote: String) {
        viewModelScope.launch {
            val currentQuotes = uiState.value.quotes
            updateUiState { copy(isLoading = true, quote = quote) }
            if (quote.isEmpty()) {
                updateUiState { copy(error = "Quote cannot be empty") }
                return@launch
            }
            val bookID = uiState.value.book.id
            val quoteModel = Quote(
                bookId = bookID,
                quote = quote,
            )
            isConnected.collectLatest { connected ->
                when (val result = quoteUseCase.addQuote(quoteModel, connected)) {
                    is Response.Success -> {
                        updateUiState {
                            copy(
                                isLoading = false,
                                quote = "",
                                quotes = currentQuotes.plus(result.data)
                            )
                        }
                    }

                    is Response.Failure -> {
                        updateUiState { copy(isLoading = false, error = result.message) }
                    }

                    Response.Empty -> {
                        updateUiState { copy(isLoading = false, error = "Something went wrong") }
                    }
                }
            }
        }
    }

    private fun getQuotes(bookId: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                quoteUseCase.getQuotes(bookId, connected).collectLatest { result ->
                    when (result) {
                        is Response.Success -> {
                            updateUiState { copy(isLoading = false, quotes = result.data) }
                        }

                        is Response.Failure -> {
                            updateUiState { copy(isLoading = false, error = result.message) }
                        }

                        Response.Empty -> {
                            updateUiState { copy(isLoading = false, quotes = emptyList()) }
                        }
                    }
                }
            }
        }
    }

    private fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = quoteUseCase.deleteQuote(quote, connected)) {
                    is Response.Success -> {
                        updateUiState { copy(isLoading = false) }
                    }

                    is Response.Failure -> {
                        updateUiState { copy(isLoading = false, error = result.message) }
                    }

                    Response.Empty -> {
                        updateUiState { copy(isLoading = false, error = "Something went wrong") }
                    }
                }
            }
        }
    }

    private fun deleteBook(bookId: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = bookUseCase.deleteBook(bookId, connected)) {
                    is Response.Success -> {
                        updateUiState { copy(isLoading = false) }
                        emitUiEffect(UiEffect.NavigateBack)
                    }

                    is Response.Failure -> {
                        updateUiState { copy(isLoading = false, error = result.message) }
                    }

                    Response.Empty -> {
                        updateUiState { copy(isLoading = false, error = "Something went wrong") }
                    }
                }
            }
        }
    }
}
