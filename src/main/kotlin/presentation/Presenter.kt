package presentation

import db.DB
import handler.DataHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.OperationType
import presentation.Contract.State
import repository.AuthRepository
import repository.AuthRepositoryImpl
import repository.DataRepository
import repository.DataRepositoryImpl

class Presenter(
    val db: DB = DB(),
    val dataHandler: DataHandler = DataHandler(),
    private val authRepository: AuthRepository = AuthRepositoryImpl(db, dataHandler),
    private val dataRepository: DataRepository = DataRepositoryImpl(db, dataHandler),
) {
    private val coroutineContext = Dispatchers.Default + SupervisorJob()
    private val coroutineScope = CoroutineScope(coroutineContext)

    private val _state = MutableStateFlow<State>(State.NoAuthorized)
    val state get() = _state.asStateFlow()

    val messages: Flow<String> get() = authRepository.getMessages()

    init {
        coroutineScope.launch {
            authRepository.getUserData().collectLatest { user ->
                when (user) {
                    null -> _state.emit(State.NoAuthorized)
                    else -> {
                        _state.emit(State.Authorized(user))
                        try {
                            println("-----------> " + user.wallets[0].categories)
                            println("-----------> " + user.wallets[0].categories[0])
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }

    fun auth(
        login: String,
        password: String,
    ) {
        authRepository.auth(login, password)
    }

    fun createUser(
        login: String,
        password: String,
    ) {
        authRepository.createUser(login, password)
    }

    fun logout() {
        authRepository.logOut()
    }

    fun createWallet(
        userId: Int,
        name: String,
    ) {
        dataRepository.createWallet(userId, name)
    }

    fun createCategory(
        userId: Int,
        walletId: Int,
        name: String,
    ) {
        dataRepository.createCategory(userId, walletId, name)
    }

    fun createOperation(
        userId: Int,
        categoryId: Int,
        amount: Int,
        type: OperationType,
    ) {
        dataRepository.createOperation(userId, categoryId, amount, type)
    }

    fun changeCategoryLimit(
        userId: Int,
        categoryId: Int,
        limit: Int,
    ) {
        dataRepository.changeCategoryLimit(userId, categoryId, limit)
    }

    fun changeWalletName(
        userId: Int,
        walletId: Int,
        newName: String,
    ) {
        dataRepository.changeWalletName(userId, walletId, newName)
    }
}