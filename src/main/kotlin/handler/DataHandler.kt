package handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import model.User

class DataHandler {
    private val coroutineContext = Dispatchers.Default + SupervisorJob()
    private val coroutineScope = CoroutineScope(coroutineContext)

    private val _userData: MutableStateFlow<User?> = MutableStateFlow(null)
    val userData: StateFlow<User?> get() = _userData.asStateFlow()

    private val _messages = Channel<String?>()
    val messages: Flow<String?> get() = _messages.consumeAsFlow()

    fun updateUserData(user: User?) {
        coroutineScope.launch {
            _userData.emit(user)
            println("emited ${_userData.value?.wallets}")
        }
    }

    fun sendMessage(text: String) {
        _messages.trySend(text)
    }
}