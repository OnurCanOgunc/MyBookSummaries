package com.decode.mybooksummaries.presentation.detail

import com.decode.mybooksummaries.domain.model.Book
import com.decode.mybooksummaries.domain.model.Quote

object DetailContract {
    data class UiState(
        val book: Book = Book(),
        val quoteModel: Quote = Quote(),
        val quotes: List<Quote> = emptyList(),
        val quote: String = "",
        val bookStartDate: String = "",
        val bookFinishDate: String = "",
        val isLoading: Boolean = false,
        val error: String = "",
        val expanded: Boolean = false
    )
    sealed interface UiAction {
        data class LoadBook(val bookId: String) : UiAction
        data class LoadQuotes(val bookId: String) : UiAction
        data object OnBackClick : UiAction
        data class AddQuote(val quote: String): UiAction
        data class DeleteQuote(val quote: Quote): UiAction
        data class QuoteChange(val quote: String): UiAction
        data class OnEditClick(val bookId: String) : UiAction
        data class OnDeleteClick(val bookId: String) : UiAction
        data object OnMessageShown : UiAction
        data object OnDismissRequest : UiAction
        data object OnMoveCartClick : UiAction
    }

    sealed interface UiEffect {
        data object NavigateBack : UiEffect
        data object NavigateToEdit : UiEffect
    }

}