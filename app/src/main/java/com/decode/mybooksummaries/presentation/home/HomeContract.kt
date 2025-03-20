package com.decode.mybooksummaries.presentation.home

import com.decode.mybooksummaries.domain.model.Book

object HomeContract {
    data class UiState(
        val books: List<Book> = emptyList(),
        val searchQuery: String = "",
        val searchResults: List<Book> = emptyList(),
        val selectedCategory: String? = null,
        val isLoading: Boolean = false,
        val error: String = "",
    )
    sealed interface UiAction {
        data class OnBookClick(val id: String) : UiAction
        data class OnCategorySelected(val category: String) : UiAction
        data class UpdateSearchQuery(val query: String) : UiAction
        data object OnSearchClick : UiAction
        data object OnAddBookClick : UiAction
        data object ClearSearchQuery : UiAction
        data object OnProfileClick : UiAction
    }
    sealed interface UiEffect {
        data class NavigateToBookDetails(val bookId: String) : UiEffect
        data object NavigateToAddBook : UiEffect
        data object NavigateToProfile : UiEffect
    }
}