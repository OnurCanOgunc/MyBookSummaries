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
                UiAction.OnCancelClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnAuthorChange -> updateUiState { copy(author = uiAction.author) }
                is UiAction.OnCurrentPageChange -> updateUiState { copy(currentPage = uiAction.currentPage) }
                is UiAction.OnFinishedReadingDateChange -> updateFinishedReadingDate(
                    uiAction.publicationDate
                )

                is UiAction.OnGenreChange -> updateUiState { copy(genre = uiAction.genre) }
                is UiAction.OnImageUriChange -> updateUiState { copy(imageBase64 = uiAction.imageUri) }
                is UiAction.OnPageCountChange -> updateUiState { copy(pageCount = uiAction.pageCount) }
                is UiAction.OnReadingStatusChange -> updateUiState {
                    copy(
                        readingStatus = uiAction.readingStatus
                    )
                }

                is UiAction.OnStartedReadingDateChange -> updateStartedReadingDate(
                    uiAction.publicationDate
                )

                is UiAction.OnSummaryChange -> updateUiState { copy(summary = uiAction.summary) }
                is UiAction.OnTitleChange -> updateUiState { copy(title = uiAction.title) }
                UiAction.OnBackClick -> emitUiEffect(UiEffect.NavigateBack)
                is UiAction.OnDatePickerTypeChange -> updateUiState {
                    copy(
                        datePickerType = uiAction.datePickerType
                    )
                }

                is UiAction.OnImageSelected -> updateUiState { copy(imageUri = uiAction.imageUri) }
                is UiAction.LoadBook -> getBookId(uiAction.bookId)
            }
        }
    }

    private fun updateStartedReadingDate(publicationDate: Long?) {
        val timestamp = publicationDate?.let { Timestamp(Date(it)) }
        updateUiState { copy(startedReadingDate = timestamp) }
    }

    private fun updateFinishedReadingDate(publicationDate: Long?) {
        val timestamp = publicationDate?.let { Timestamp(Date(it)) }
        updateUiState { copy(finishedReadingDate = timestamp) }
    }

    private fun getBookId(bookId: String) {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }
            isConnected.collectLatest { connected ->
                when (val result = bookUseCase.getBookById(bookId, connected)) {
                    is Response.Success -> {
                        updateUiState {
                            copy(
                                bookId = result.data.id,
                                title = result.data.title,
                                author = result.data.author,
                                pageCount = result.data.pageCount,
                                currentPage = result.data.currentPage,
                                genre = result.data.genre,
                                summary = result.data.summary,
                                readingStatus = result.data.readingStatus,
                                startedReadingDate = result.data.startedReadingDate,
                                finishedReadingDate = result.data.finishedReadingDate,
                                imageUri = result.data.imageUrl.base64ToBitmap(),
                                imageBase64 = result.data.imageUrl,
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
        bookFromState()
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

    private fun bookFromState() {
        Log.e("AddBookScreen", "onAddBookClick called")
        updateUiState {
            copy(
                isLoading = true,
                book = Book(
                    id = bookId,
                    title = title,
                    author = author,
                    currentPage = currentPage,
                    genre = genre,
                    summary = summary,
                    readingStatus = readingStatus,
                    startedReadingDate = startedReadingDate,
                    finishedReadingDate = finishedReadingDate,
                    pageCount = pageCount,
                    imageUrl = imageBase64 ?: ""
                )
            )
        }

    }

}