package com.decode.mybooksummaries.presentation.home

import com.decode.mybooksummaries.domain.model.Book

object HomeContract {
    data class UiState(
        val books: List<Book> = emptyList(),
        val searchQuery: String = "",
        val searchResults: List<Book> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )
    sealed interface UiAction {
        object OnSearchClick : UiAction
        object OnAddBookClick : UiAction
        data class OnBookClick(val id: String) : UiAction
        object ClearSearchQuery : UiAction
        data class UpdateSearchQuery(val query: String) : UiAction
        object OnProfileClick : UiAction
    }
    sealed interface UiEffect {
        data class NavigateToBookDetails(val bookId: String) : UiEffect
        object NavigateToAddBook : UiEffect
        object NavigateToProfile : UiEffect
    }
}