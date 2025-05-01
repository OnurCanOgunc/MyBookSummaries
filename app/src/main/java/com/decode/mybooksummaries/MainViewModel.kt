package com.decode.mybooksummaries

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.worker.WorkScheduler
import com.decode.mybooksummaries.di.IoDispatcher
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    currentUser: CurrentUserUseCase,
    private val syncWorkManager: WorkScheduler,
    private val connectivityObserver: ConnectivityObserver,
    private val db: BookDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val authUser = currentUser.getCurrentUser() == null

    init {
        startSync()
    }

    private fun startSync() {
        viewModelScope.launch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (isConnected && !authUser) {
                    val shouldSync = withContext(ioDispatcher) {
                        val unsyncedBooks = db.bookDao().getUnsyncedBooks().isNotEmpty()
                        val deletedUnsyncedBooks = db.bookDao().getDeletedUnsyncedBooks().isNotEmpty()
                        val monthlyGoals = db.monthlyGoalDao().getUnsyncedGoals().isNotEmpty()
                        val unsyncedQuotes = db.quoteDao().getUnsyncedQuotes().isNotEmpty()
                        val deletedUnsyncedQuotes = db.quoteDao().getDeletedUnsyncedQuotes().isNotEmpty()
                        unsyncedBooks || deletedUnsyncedBooks || monthlyGoals || unsyncedQuotes || deletedUnsyncedQuotes
                    }

                    if (shouldSync) {
                        Log.d("SyncViewModel", "Internet is here, synchronization is starting")
                        syncWorkManager.startOneTimeSync()
                    } else {
                        Log.d("SyncViewModel", "No data to sync")
                    }
                    syncWorkManager.startPeriodicSync()
                }
            }
        }
    }

}