package com.decode.mybooksummaries.presentation.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.core.ui.extensions.timestampToString
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.domain.usecase.QuoteUseCases
import com.decode.mybooksummaries.presentation.detail.DetailContract.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
                DetailContract.UiAction.OnMessageShown -> updateUiState { copy(error = "") }
            }
        }
    }


    private fun getBook(id: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = bookUseCase.getBookById(id, connected)) {
                    is Response.Success -> {
                        updateUiState {
                            copy(
                                book = result.data,
                                isLoading = false,
                                bookStartDate = result.data.startedReadingDate.timestampToString(),
                                bookFinishDate = result.data.finishedReadingDate.timestampToString()
                            )
                        }
                    }

                    is Response.Failure -> {
                        updateUiState { copy(error = result.message, isLoading = false) }
                        emitUiEffect(UiEffect.ShowSnackbar(result.message))
                    }

                    Response.Empty -> {
                        updateUiState { copy(isLoading = false) }
                    }
                }
            }

        }
    }

    private fun addQuote(quote: String) {
        viewModelScope.launch {
            Log.d("DetailViewModel", "Current Quotes: ${uiState.value.quotes}")
            updateUiState { copy(isLoading = true) }
            if (quote.isEmpty()) {
                updateUiState { copy(error = "Quote cannot be empty", isLoading = false) }
                return@launch
            }
            val quoteModel = Quote(bookId = uiState.value.book.id, quote = quote)
            updateUiState { copy(quoteModel = quoteModel) }


            when (val result = quoteUseCase.addQuote(quoteModel, isConnected.value)) {
                is Response.Success -> {
                    Log.d("DetailViewModel", "Quote added successfully")
                    updateUiState {
                        copy(
                            isLoading = false,
                            quote = "",
                            quoteModel = Quote(),
                        )
                    }
                    getQuotes(uiState.value.book.id)
                    emitUiEffect(UiEffect.ShowSnackbar("Quote added successfully"))
                }

                is Response.Failure -> {
                    Log.d("DetailViewModel", "Quote adding failed: ${result.message}")
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = result.message,
                            quoteModel = Quote()
                        )
                    }
                    emitUiEffect(UiEffect.ShowSnackbar(result.message))
                }

                Response.Empty -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            error = "Something went wrong",
                            quoteModel = Quote()
                        )
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
                            Log.d("DetailViewModel", "getQuotes: ${result.data}")
                            updateUiState { copy(isLoading = false, quotes = result.data.toImmutableList()) }
                        }

                        is Response.Failure -> {
                            Log.d("DetailViewModel", "getQuotes: ${result.message}")
                            updateUiState { copy(isLoading = false, error = result.message) }
                        }

                        Response.Empty -> {
                            updateUiState { copy(isLoading = false, quotes = persistentListOf()) }
                        }
                    }
                }
            }
        }
    }

    private fun deleteQuote(quote: Quote) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            when (val result = quoteUseCase.deleteQuote(quote, isConnected.value)) {
                is Response.Success -> {
                    updateUiState { copy(isLoading = false) }
                    emitUiEffect(UiEffect.ShowSnackbar("Quote deleted successfully"))
                }

                is Response.Failure -> {
                    updateUiState { copy(isLoading = false, error = result.message) }
                    emitUiEffect(UiEffect.ShowSnackbar(result.message))
                }

                Response.Empty -> {
                    updateUiState { copy(isLoading = false, error = "Something went wrong") }
                }
            }
        }
    }

    private fun deleteQuotes() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = quoteUseCase.deleteAllQuotes(connected)) {
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
                        deleteQuotes()
                        updateUiState { copy(isLoading = false) }
                        delay(100)
                        emitUiEffect(UiEffect.NavigateBack)
                    }

                    is Response.Failure -> {
                        updateUiState { copy(isLoading = false, error = result.message) }
                    }

                    Response.Empty -> {
                        updateUiState {
                            copy(
                                isLoading = false,
                                error = "Something went wrong"
                            )
                        }
                    }
                }
            }
        }
    }
}
