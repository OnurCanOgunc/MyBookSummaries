package com.decode.mybooksummaries.presentation.addbook

import com.decode.mybooksummaries.domain.model.Book

object AddBookContract {
    data class UiState(
        val isLoading: Boolean = false,
        val message: String? = null,
        val book: Book = Book(),
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
        data object OnAddBookClick : UiAction
        data object OnCancelClick : UiAction
        data object OnBackClick : UiAction
        data class LoadBook(val bookId: String) : UiAction
    }
    sealed interface UiEffect {
        object NavigateBack : UiEffect
    }
}