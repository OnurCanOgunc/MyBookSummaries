package com.decode.mybooksummaries.presentation.detail

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decode.mybooksummaries.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.graphicsLayer
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
import com.decode.mybooksummaries.core.ui.extensions.timestampToString
import com.decode.mybooksummaries.core.ui.theme.HomeBackgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailScreen(
    bookId: String,
    uiState: DetailContract.UiState,
    uiEffect: Flow<DetailContract.UiEffect>,
    onAction: (DetailContract.UiAction) -> Unit,
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    onAddBookClick: (String) -> Unit
) {
    val imageBitmap by produceState<Bitmap?>(initialValue = null, key1 = uiState.book.imageUrl) {
        value = uiState.book.imageUrl.base64ToBitmap()
    }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onAction(DetailContract.UiAction.LoadBook(bookId))
        onAction(DetailContract.UiAction.LoadQuotes(bookId))
    }

    uiEffect.CollectWithLifecycle {
        when (it) {
            DetailContract.UiEffect.NavigateBack -> {
                popBackStack()
            }

            DetailContract.UiEffect.NavigateToEdit -> {
                onAddBookClick(bookId)
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, start = 16.dp),
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
                        tint = Color.White
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
        }
    ) { paddingValues ->

        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .graphicsLayer {
                        renderEffect = BlurEffect(radiusX = 10f, radiusY = 10f)
                    }
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(HomeBackgroundColor)
                .padding(paddingValues),
        ) {
            Header(
                imageBitmap = imageBitmap,
                title = uiState.book.title,
                author = uiState.book.author,
                genre = uiState.book.genre,
                startedDate = uiState.book.startedReadingDate.timestampToString(),
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
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap?,
    title: String,
    author: String,
    genre: String,
    startedDate: String,
    currentPage: String,
    pageCount: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = author,
                fontSize = 16.sp,
                color = Color.LightGray
            )

            Text(
                text = stringResource(R.string.started, startedDate),
                fontSize = 14.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = genre,
                    fontSize = 14.sp,
                    color = Color.White
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
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { (currentPage.toFloatOrNull() ?: 0f) / (pageCount.toFloatOrNull() ?: 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = Color(0xFF4A90E2),
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.pages, currentPage, pageCount),
            fontSize = 12.sp,
            color = Color.LightGray,
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
            .padding(horizontal = 12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            tonalElevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.summary),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = summary,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { onReadMoreClick(true) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.read_more))
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SummaryPager(summary: String, onDismiss: () -> Unit) {
    val words = summary.chunked(620)
    val pagerState = rememberPagerState(pageCount = { words.size })

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(12.dp), modifier = Modifier.height(600.dp)) {
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = words[page],
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        "${page + 1}/${words.size}",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 10.dp)
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
    onQuoteChange: (String) -> Unit,
    quotes: List<Quote>,
    onQuoteDelete: (Quote) -> Unit,
    onQuoteAdd: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.favorite_quotes),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
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
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    cursorColor = Color.LightGray,
                    focusedLabelColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                ),
                textStyle = TextStyle(textAlign = TextAlign.Start)
            )
            Button(
                onClick = {
                    if (quote.isNotEmpty()) {
                        onQuoteAdd()
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 8.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                )
            ) {
                Text("Add", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(quotes, key = {it.id}) { quote ->
                SwipeToDeleteContainer(
                    item = quote,
                    onDelete = onQuoteDelete
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
        if(isRemoved) {
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
            backgroundContent  = {
                DeleteBackground(swipeDismissState = state)
            },
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\"${item.quote}\"",
                            modifier = Modifier.weight(1f),
                            color = Color.Black
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
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}