package com.decode.mybooksummaries.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decode.mybooksummaries.core.network.ConnectivityObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, UiAction, UiEffect>(
    connectivityObserver: ConnectivityObserver,
    initialUiState: UiState
) : ViewModel() {

    val isConnected = connectivityObserver.isConnected.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: StateFlow<UiState> by lazy {  //Burada kullanımı, by lazy { }içeriğin yalnızca veri alınırken çağrılmasını ve değişkene onStarther eriştiğimizde çağrılmamasını sağlamak içindir.state
        _uiState.onStart {
            viewModelScope.launch {
                initialDataLoad()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialUiState
        )
    }

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    abstract fun onAction(uiAction: UiAction)

    fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }

    open suspend fun initialDataLoad() {}
}