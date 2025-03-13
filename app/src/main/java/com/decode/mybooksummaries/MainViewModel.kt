package com.decode.mybooksummaries

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import com.decode.mybooksummaries.data.local.db.BookDatabase
import com.decode.mybooksummaries.data.workers.SyncWorkManager
import com.decode.mybooksummaries.domain.usecase.auth.CurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    currentUser: CurrentUserUseCase,
    private val syncWorkManager: SyncWorkManager,
    private val connectivityObserver: ConnectivityObserver,
    private val db: BookDatabase
) : ViewModel() {
    val authUser = currentUser.getCurrentUser() == null

    init {
        startSync()
    }

    private fun startSync() {
        viewModelScope.launch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                if (isConnected) {
                    val unsyncedBooks = db.bookDao().getUnsyncedBooks().isNotEmpty()
                    val deletedUnsyncedBooks = db.bookDao().getDeletedUnsyncedBooks().isNotEmpty()
                    Log.d("SyncViewModel", "$deletedUnsyncedBooks")
                    if (unsyncedBooks || deletedUnsyncedBooks) {
                        Log.d("SyncViewModel", "İnternet geldi, senkronizasyon başlatılıyor")
                        syncWorkManager.startOneTimeSync()
                    } else {
                        Log.d("SyncViewModel", "Senkronize edilecek veri yok")
                    }
                }
            }
        }
    }

}