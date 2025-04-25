package com.decode.mybooksummaries.presentation.addbook

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.usecase.BookUseCases
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiEffect
import com.decode.mybooksummaries.core.ui.extensions.base64ToBitmap
import com.decode.mybooksummaries.core.ui.viewmodel.BaseViewModel
import com.decode.mybooksummaries.core.utils.Response
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiAction
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiState
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val bookUseCase: BookUseCases, connectivityObserver: ConnectivityObserver
) : BaseViewModel<UiState, UiAction, UiEffect>(connectivityObserver, UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.OnAddBookClick -> onAddBookClick()
                UiAction.OnCancelClick, UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnTitleChange -> updateBook { copy(title = uiAction.title) }
                is UiAction.OnAuthorChange -> updateBook { copy(author = uiAction.author) }
                is UiAction.OnPageCountChange -> updateBook { copy(pageCount = uiAction.pageCount) }
                is UiAction.OnCurrentPageChange -> updateCurrentPage(uiAction.currentPage)
                is UiAction.OnGenreChange -> updateBook { copy(genre = uiAction.genre) }
                is UiAction.OnSummaryChange -> updateBook { copy(summary = uiAction.summary) }
                is UiAction.OnReadingStatusChange -> updateBook { copy(readingStatus = uiAction.readingStatus) }
                is UiAction.OnStartedReadingDateChange -> updateStartedReadingDate(uiAction.publicationDate)
                is UiAction.OnFinishedReadingDateChange -> updateFinishedReadingDate(uiAction.publicationDate)
                is UiAction.OnImageUriChange -> updateBook { copy(imageUrl = uiAction.imageUri) }
                is UiAction.OnDatePickerTypeChange -> updateUiState { copy(datePickerType = uiAction.datePickerType) }
                is UiAction.LoadBook -> getBookById(uiAction.bookId)
            }
        }
    }

    private fun updateBook(update: Book.() -> Book) {
        updateUiState { copy(book = book.update()) }
    }

    private fun updateCurrentPage(currentPage: String) {
        if (currentPage.isNotEmpty() && currentPage.toInt() > uiState.value.book.pageCount.toInt()) {
            updateUiState { copy(message = "Current page cannot be greater than total pages") }
            return
        }
        updateBook { copy(currentPage = currentPage) }
    }

    private fun updateStartedReadingDate(publicationDate: Long?) {
        val timestamp = publicationDate?.let { Timestamp(Date(it)) }
        updateBook { copy(startedReadingDate = timestamp) }
    }

    private fun updateFinishedReadingDate(publicationDate: Long?) {
        publicationDate?.let {
            if (it <= (uiState.value.book.startedReadingDate?.toDate()?.time ?: 0)) {
                updateUiState { copy(message = "Finished reading date cannot be before started reading date") }
                return
            }
        }
        val timestamp = publicationDate?.let { Timestamp(Date(it)) }
        updateBook { copy(finishedReadingDate = timestamp) }
    }

    private fun getBookById(bookId: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = bookUseCase.getBookById(bookId, connected)) {
                    is Response.Success -> {
                        updateUiState {
                            copy(
                                book = result.data,
                                isLoading = false,
                                message = "Book fetched successfully!"
                            )
                        }
                    }

                    is Response.Failure -> {
                        updateUiState { copy(isLoading = false, message = result.message) }
                    }

                    Response.Empty -> {
                        updateUiState { copy(isLoading = false, message = "No data returned!") }
                    }

                }
            }
        }
    }

    private fun onAddBookClick() = viewModelScope.launch {
        Log.e("AddBookScreen", "onAddBookClick called")
        isConnected.collectLatest { connected ->
            Log.d("AddBookScreen", "Connected: $connected")
            val book = uiState.value.book
            when (val result = bookUseCase.addBook(book, connected)) {
                is Response.Success -> {
                    updateUiState {
                        copy(
                            isLoading = false,
                            message = "Book added successfully!"
                        )
                    }
                    Log.d("AddBookScreen", "Navigating back")
                    emitUiEffect(UiEffect.NavigateBack)
                }

                is Response.Failure -> {
                    updateUiState { copy(isLoading = false, message = result.message) }
                    Log.d("AddBookScreen", "Error: ${result.message}")
                }

                is Response.Empty -> {
                    updateUiState { copy(isLoading = false, message = "No data returned!") }
                }
            }
        }
    }

}