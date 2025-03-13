package com.decode.mybooksummaries.presentation.addbook

import android.graphics.Bitmap
import com.decode.mybooksummaries.domain.model.Book
import com.google.firebase.Timestamp

object AddBookContract {
    data class UiState(
        val isLoading: Boolean = false,
        val message: String? = null,
        val book: Book = Book(),
        val title: String = "",
        val author: String = "",
        val bookId: String = book.id,
        val pageCount: String = "0",
        val currentPage: String = "0",
        val genre: String = "Select Genre",
        val summary: String = "",
        val readingStatus: String = "To Read",
        val startedReadingDate: Timestamp? = null,
        val finishedReadingDate: Timestamp? = null,
        val imageUri: Bitmap? = null,
        val imageBase64: String? = book.imageUrl,
        val datePickerType: DatePickerType? = null
    )
    sealed interface UiAction {
        data class OnTitleChange(val title: String) : UiAction
        data class OnAuthorChange(val author: String) : UiAction
        data class OnPageCountChange(val pageCount: String) : UiAction
        data class OnCurrentPageChange(val currentPage: String) : UiAction
        data class OnGenreChange(val genre: String) : UiAction
        data class OnSummaryChange(val summary: String) : UiAction
        data class OnReadingStatusChange(val readingStatus: String) : UiAction
        data class OnStartedReadingDateChange(val publicationDate: Long?) : UiAction
        data class OnFinishedReadingDateChange(val publicationDate: Long?) : UiAction
        data class OnImageUriChange(val imageUri: String) : UiAction
        data class OnDatePickerTypeChange(val datePickerType: DatePickerType?) : UiAction
        data class OnImageSelected(val imageUri: Bitmap?) : UiAction
        object OnAddBookClick : UiAction
        object OnCancelClick : UiAction
        object OnBackClick : UiAction
        data class LoadBook(val bookId: String) : UiAction
    }
    sealed interface UiEffect {
        object NavigateBack : UiEffect
    }
}