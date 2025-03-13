package com.decode.mybooksummaries.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.HomeBackgroundColor
import com.decode.mybooksummaries.presentation.home.component.BookItem
import com.decode.mybooksummaries.presentation.home.HomeContract.UiEffect
import com.decode.mybooksummaries.presentation.home.component.TopBar
import kotlinx.coroutines.flow.Flow


@Composable
fun HomeScreen(
    uiState: HomeContract.UiState,
    onAction: (HomeContract.UiAction) -> Unit,
    uiEffect: Flow<UiEffect>,
    onDetailClick: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    uiEffect.CollectWithLifecycle {
        when (it) {
            UiEffect.NavigateToAddBook -> {
                onAddClick()
            }
            is UiEffect.NavigateToBookDetails -> {
                onDetailClick(it.bookId)
            }

            UiEffect.NavigateToProfile -> {
                onProfileClick()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = HomeBackgroundColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopBar(
                searchText = uiState.searchQuery,
                updateSearchQuery = { onAction(HomeContract.UiAction.UpdateSearchQuery(it)) },
                onProfileClick = { onAction(HomeContract.UiAction.OnProfileClick) },
                onAddClick = {onAction(HomeContract.UiAction.OnAddBookClick)},
                onSearchClick = {onAction(HomeContract.UiAction.OnSearchClick)},
                clearSearchQuery = {onAction(HomeContract.UiAction.ClearSearchQuery)}
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyVerticalGrid(
                modifier = Modifier,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val books = if (uiState.searchQuery.isNotEmpty()) uiState.searchResults else uiState.books
                items(books, key = { it.id }) { book ->
                    BookItem(
                        title = book.title,
                        author = book.author,
                        pageCount = book.pageCount,
                        currentPage = book.currentPage,
                        status = book.readingStatus,
                        coverImage = book.imageUrl,
                        onItemClick = { onDetailClick(book.id) }
                    )
                }
            }
        }
    }
}
