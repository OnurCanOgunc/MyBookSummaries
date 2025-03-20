package com.decode.mybooksummaries.presentation.addbook

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decode.mybooksummaries.presentation.addbook.component.ButtonGroup
import com.decode.mybooksummaries.presentation.addbook.component.CoverImageUpload
import com.decode.mybooksummaries.presentation.addbook.component.DropdownField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiAction
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiEffect
import com.decode.mybooksummaries.presentation.addbook.AddBookContract.UiState
import com.decode.mybooksummaries.presentation.addbook.component.DatePickerModalInput
import com.decode.mybooksummaries.presentation.addbook.component.InputField
import com.decode.mybooksummaries.presentation.addbook.component.ReadingStatusSelector
import com.decode.mybooksummaries.core.ui.components.TopBar
import com.decode.mybooksummaries.core.ui.extensions.CollectWithLifecycle
import com.decode.mybooksummaries.core.ui.extensions.timestampToString
import com.decode.mybooksummaries.presentation.addbook.utils.uriToBase64
import com.decode.mybooksummaries.presentation.addbook.utils.uriToBitmap
import kotlinx.coroutines.flow.Flow
import com.decode.mybooksummaries.R
import com.decode.mybooksummaries.core.ui.theme.CustomTheme

@Composable
fun AddBookScreen(
    bookId: String?,
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    popBackStack: () -> Unit,
) {
    val context = LocalContext.current
    uiEffect.CollectWithLifecycle {
        when (it) {
            UiEffect.NavigateBack -> {
                popBackStack()
            }
        }
    }
    LaunchedEffect(bookId) {
        bookId?.let {
            Log.e("AddBookScreen", "LaunchedEffect called")
            onAction(UiAction.LoadBook(it))
        }
    }

    val scrollState = rememberScrollState()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopBar(
                modifier = Modifier,
                title = if (bookId == null) stringResource(R.string.add_book) else stringResource(R.string.edit_book),
                popBackStack = { onAction(UiAction.OnBackClick) })
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoverImageUpload(
                    imageUri = uiState.imageUri,
                    onImageSelected = { uri ->
                        val bitmap = uriToBitmap(context, uri)
                        onAction(UiAction.OnImageSelected(bitmap))
                        val base64Image = uriToBase64(context, uri)
                        base64Image?.let {
                            onAction(UiAction.OnImageUriChange(it.toString()))
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                ReadingStatusSelector(
                    selectedStatus = uiState.book.readingStatus,
                    onStatusSelected = { onAction(UiAction.OnReadingStatusChange(it)) })
            }

            BookInfoFields(
                title = uiState.book.title,
                author = uiState.book.author,
                pageCount = uiState.book.pageCount,
                currentPage = uiState.book.currentPage,
                onAction = onAction
            )

            DropdownField(
                label = stringResource(R.string.genre),
                selectedItem = uiState.book.genre,
                onItemSelected = { onAction(UiAction.OnGenreChange(it)) }
            )
            BookDate(onAction, uiState)
            BookSummary(uiState = uiState, onAction = onAction)
            ButtonGroup(
                isEditMode = bookId != null,
                onCancel = { onAction(UiAction.OnCancelClick) },
                onAddBook = { onAction(UiAction.OnAddBookClick) })
        }

}

@Composable
private fun BookSummary(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.summary),
            style = CustomTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp),
            color = CustomTheme.colors.textBlack

        )
        OutlinedTextField(
            value = uiState.book.summary,
            onValueChange = { onAction(UiAction.OnSummaryChange(it)) },
            shape = RoundedCornerShape(6.dp),
            modifier = modifier
                .height(100.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = CustomTheme.colors.textBlack,
                unfocusedTextColor = CustomTheme.colors.textBlack,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = CustomTheme.colors.charcoalBlack,
                unfocusedIndicatorColor = CustomTheme.colors.coolGray,
            ),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                color = CustomTheme.colors.textBlack
            ),
        )
    }
}

@Composable
private fun BookDate(
    onAction: (UiAction) -> Unit,
    uiState: UiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onAction(UiAction.OnDatePickerTypeChange(DatePickerType.START_DATE)) },
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.electricOrange,
                contentColor = CustomTheme.colors.textWhite
            )
        ) {
            val formattedDate = uiState.book.startedReadingDate?.timestampToString()
                ?: stringResource(R.string.started_date)
            Text(text = formattedDate, style = CustomTheme.typography.labelMedium,color = CustomTheme.colors.softWhite)
        }
        Button(
            onClick = { onAction(UiAction.OnDatePickerTypeChange(DatePickerType.FINISH_DATE)) },
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomTheme.colors.electricOrange,
                contentColor = CustomTheme.colors.textWhite
            )
        ) {
            val formattedDate = uiState.book.finishedReadingDate?.timestampToString()
                ?: stringResource(R.string.finished_date)
            Text(text = formattedDate,style = CustomTheme.typography.labelMedium,color = CustomTheme.colors.softWhite)
        }
    }
    uiState.datePickerType?.let {
        DatePickerModalInput(
            onDateSelected = { dateMillis ->
                when (uiState.datePickerType) {
                    DatePickerType.START_DATE -> onAction(
                        UiAction.OnStartedReadingDateChange(
                            dateMillis
                        )
                    )

                    DatePickerType.FINISH_DATE -> onAction(
                        UiAction.OnFinishedReadingDateChange(
                            dateMillis
                        )
                    )
                }
                onAction(UiAction.OnDatePickerTypeChange(null))
            },
            onDismiss = { onAction(UiAction.OnDatePickerTypeChange(null)) }
        )
    }
}

@Composable
fun BookInfoFields(
    title: String,
    author: String,
    pageCount: String,
    currentPage: String,
    onAction: (UiAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InputField(
            label = stringResource(R.string.book_title),
            value = title,
            onValueChange = { onAction(UiAction.OnTitleChange(it)) })
        InputField(
            label = stringResource(R.string.author),
            value = author,
            onValueChange = { onAction(UiAction.OnAuthorChange(it)) })
        InputField(
            label = stringResource(R.string.total_pages),
            value = pageCount,
            onValueChange = { onAction(UiAction.OnPageCountChange(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        InputField(
            label = stringResource(R.string.current_pages),
            value = currentPage,
            onValueChange = { onAction(UiAction.OnCurrentPageChange(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
    }
}