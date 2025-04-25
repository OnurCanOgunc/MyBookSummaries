package com.decode.mybooksummaries.presentation.detail

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.domain.model.Quote
import com.decode.mybooksummaries.presentation.detail.component.MinimalDropdownMenu
import com.decode.mybooksummaries.core.ui.extensions.base64ToBitmap
import com.decode.mybooksummaries.core.ui.theme.CustomTheme
import com.decode.mybooksummaries.presentation.detail.util.splitTextByWords
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailScreen(
    bookId: String,
    uiState: DetailContract.UiState,
    uiEffect: Flow<DetailContract.UiEffect>,
    onAction: (DetailContract.UiAction) -> Unit,
    popBackStack: () -> Unit,
    onAddBookClick: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var isMessageVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        onAction(DetailContract.UiAction.LoadBook(bookId))
        onAction(DetailContract.UiAction.LoadQuotes(bookId))
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            isMessageVisible = true
            delay(3000)
            isMessageVisible = false
            delay(600)
            onAction(DetailContract.UiAction.OnMessageShown)
        }
    }

    uiEffect.CollectWithLifecycle {
        when (it) {
            DetailContract.UiEffect.NavigateBack -> {
                popBackStack()
            }

            DetailContract.UiEffect.NavigateToEdit -> {
                onAddBookClick(bookId)
            }

            is DetailContract.UiEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    action = {
                        IconButton(onClick = { snackbarData.dismiss() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Snackbar",
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    Text(text = snackbarData.visuals.message)
                }
            }
        },
        containerColor = CustomTheme.colors.backgroundColor
    ) { _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onAction(DetailContract.UiAction.OnBackClick)
                    },
                    modifier = Modifier,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                        tint = CustomTheme.colors.textBlack
                    )
                }
                MinimalDropdownMenu(
                    expanded = uiState.expanded,
                    onDismissRequest = { onAction(DetailContract.UiAction.OnDismissRequest) },
                    onMoveCartClick = { onAction(DetailContract.UiAction.OnMoveCartClick) },
                    onEditClick = { onAction(DetailContract.UiAction.OnEditClick(bookId)) },
                    onDeleteClick = { onAction(DetailContract.UiAction.OnDeleteClick(bookId)) }
                )
            }

            Header(
                imageUrl = uiState.book.imageUrl,
                title = uiState.book.title,
                author = uiState.book.author,
                genre = uiState.book.genre,
                startedDate = uiState.bookStartDate,
                finishedDate = uiState.bookFinishDate,
                currentPage = uiState.book.currentPage,
                pageCount = uiState.book.pageCount
            )
            Spacer(modifier = Modifier.height(16.dp))
            BookSummary(
                summary = uiState.book.summary,
                showFullSummary = showDialog,
                onReadMoreClick = {
                    showDialog = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FavoriteQuotes(
                quote = uiState.quote,
                errorMessage = uiState.error,
                isMessageVisible = isMessageVisible,
                onQuoteChange = { onAction(DetailContract.UiAction.QuoteChange(it)) },
                quotes = uiState.quotes,
                onQuoteDelete = { onAction(DetailContract.UiAction.DeleteQuote(it)) },
                onQuoteAdd = {
                    Log.d("FavoriteQuotes", "Adding quote: ${uiState.quote}")
                    onAction(DetailContract.UiAction.AddQuote(uiState.quote))
                }
            )
        }
    }
}

@Composable
fun Header(
    imageUrl: String,
    title: String,
    author: String,
    genre: String,
    startedDate: String,
    finishedDate: String,
    currentPage: String,
    pageCount: String,
) {

    val imageBitmap: Bitmap? by remember(imageUrl) {
        derivedStateOf { imageUrl.base64ToBitmap() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageBitmap ?: R.drawable.img,
            contentDescription = null,
            modifier = Modifier
                .width(120.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = CustomTheme.typography.titleLarge,
                color = CustomTheme.colors.textBlack,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = author,
                style = CustomTheme.typography.bodyLarge,
                color = CustomTheme.colors.slateGray,
            )

            Text(
                text = stringResource(R.string.started, startedDate),
                style = CustomTheme.typography.bodyMedium,
                color = CustomTheme.colors.slateGray
            )

            Text(
                text = stringResource(R.string.finished, finishedDate),
                style = CustomTheme.typography.bodyMedium,
                color = CustomTheme.colors.slateGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .background(
                        CustomTheme.colors.charcoalBlack,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = genre,
                    style = CustomTheme.typography.bodySmall,
                    color = CustomTheme.colors.softWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            BookPage(currentPage = currentPage, pageCount = pageCount)
        }

    }
}

@Composable
fun BookPage(currentPage: String, pageCount: String) {
    Column {
        Text(
            text = stringResource(R.string.reading_progress),
            style = CustomTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = CustomTheme.colors.slateGray
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { (currentPage.toFloatOrNull() ?: 0f) / (pageCount.toFloatOrNull() ?: 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = CustomTheme.colors.deepRose,
            trackColor = CustomTheme.colors.slateGray,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.pages, currentPage, pageCount),
            style = CustomTheme.typography.bodySmall,
            color = CustomTheme.colors.slateGray,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun BookSummary(
    modifier: Modifier = Modifier,
    summary: String,
    showFullSummary: Boolean,
    onReadMoreClick: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            color = CustomTheme.colors.charcoalBlack
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.summary),
                    style = CustomTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CustomTheme.colors.textWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = summary,
                    style = CustomTheme.typography.bodyMedium,
                    color = CustomTheme.colors.softWhite,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { onReadMoreClick(true) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        stringResource(R.string.read_more),
                        color = CustomTheme.colors.softWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showFullSummary && summary.isNotEmpty()) {
        SummaryPager(summary = summary) {
            onReadMoreClick(false)
        }
    }
}

@Composable
fun SummaryPager(summary: String, onDismiss: () -> Unit) {
    val words = splitTextByWords(summary, 700)
    val pagerState = rememberPagerState(pageCount = { words.size })

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(600.dp),
            color = CustomTheme.colors.charcoalBlack
        ) {
            HorizontalPager(state = pagerState) { page ->

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Text(
                        text = words[page],
                        style = CustomTheme.typography.bodyLarge,
                        color = CustomTheme.colors.softWhite,
                        maxLines = 22,
                        overflow = TextOverflow.Clip
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "${page + 1}/${pagerState.pageCount}",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 10.dp),
                        style = CustomTheme.typography.bodySmall,
                        color = CustomTheme.colors.softWhite
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteQuotes(
    modifier: Modifier = Modifier,
    quote: String,
    isMessageVisible: Boolean,
    errorMessage: String = "",
    onQuoteChange: (String) -> Unit,
    quotes: ImmutableList<Quote>,
    onQuoteDelete: (Quote) -> Unit,
    onQuoteAdd: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.favorite_quotes),
            style = CustomTheme.typography.bodyExtraLarge,
            fontWeight = FontWeight.Bold,
            color = CustomTheme.colors.textBlack
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = quote,
                onValueChange = onQuoteChange,
                label = { Text(stringResource(R.string.add_a_quote)) },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(textAlign = TextAlign.Start),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = CustomTheme.colors.electricOrange,
                    unfocusedIndicatorColor = CustomTheme.colors.coolGray,
                    focusedContainerColor = CustomTheme.colors.softWhite,
                    unfocusedContainerColor = CustomTheme.colors.softWhite,
                    unfocusedLabelColor = CustomTheme.colors.coolGray,
                    focusedLabelColor = CustomTheme.colors.coolGray,
                    cursorColor = CustomTheme.colors.coolGray,
                )
            )
            Button(
                onClick = {
                    onQuoteAdd()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 8.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomTheme.colors.charcoalBlack,
                )
            ) {
                Text("Add", color = CustomTheme.colors.softWhite)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = isMessageVisible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut()
        ) {
            Text(
                text = errorMessage,
                color = CustomTheme.colors.errorColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                style = CustomTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold

            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(quotes, key = { it.id }) { quote ->
                SwipeToDeleteContainer(
                    item = quote,
                    onDelete = onQuoteDelete,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    item: Quote,
    onDelete: (Quote) -> Unit,
    animationDuration: Int = 500,
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeDismissState = state)
            },
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = CustomTheme.colors.charcoalBlack)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\"${item.quote}\"",
                            modifier = Modifier.weight(1f),
                            color = CustomTheme.colors.textWhite
                        )
                    }
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        CustomTheme.colors.errorColor
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, shape = RoundedCornerShape(12.dp))
            .padding(6.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = CustomTheme.colors.softWhite
        )
    }
}