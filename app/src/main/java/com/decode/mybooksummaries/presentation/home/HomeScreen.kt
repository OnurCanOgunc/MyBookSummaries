package com.decode.mybooksummaries.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.home.component.BookItem
import com.decode.mybooksummaries.presentation.home.HomeContract.UiEffect
import com.decode.mybooksummaries.presentation.home.component.TopBar
import kotlinx.coroutines.flow.Flow
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.presentation.home.component.CategorySelector

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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TopBar(
            searchText = uiState.searchQuery,
            updateSearchQuery = { onAction(HomeContract.UiAction.UpdateSearchQuery(it)) },
            onProfileClick = { onAction(HomeContract.UiAction.OnProfileClick) },
            onAddClick = { onAction(HomeContract.UiAction.OnAddBookClick) },
            onSearchClick = { onAction(HomeContract.UiAction.OnSearchClick) },
            clearSearchQuery = { onAction(HomeContract.UiAction.ClearSearchQuery) }
        )
        CategorySelector(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = {
                onAction(HomeContract.UiAction.OnCategorySelected(it))
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.books.isEmpty()) {
            Spacer(modifier = Modifier.weight(5f))
            Text(
                style = CustomTheme.typography.titleLarge,
                text = stringResource(R.string.empty_book),
                color = CustomTheme.colors.textBlack,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onAction(HomeContract.UiAction.OnAddBookClick) },
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.charcoalBlack,
                    contentColor = CustomTheme.colors.softWhite
                )
            ) {
                Text(text = stringResource(R.string.add_book), style = CustomTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(5f))
        }
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val books =
                if (uiState.searchQuery.isNotEmpty()) uiState.searchResults else uiState.books
            items(books, key = { it.id }) { book ->
                BookItem(
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
