package presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import presentation.screen.AuthScreen
import presentation.screen.CabinetScreen

@Composable
@Preview
fun App() {
    val presenter = Presenter()
    val state by presenter.state.collectAsState()
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(targetState = state) { actualState ->
                when (actualState) {
                    is Contract.State.Authorized -> {
                        println(actualState)
                        println(actualState.user.wallets)
                        CabinetScreen(
                            state = actualState,
                            onCreateWallet = presenter::createWallet,
                            onCreateCategory = presenter::createCategory,
                            onCreateOperation = presenter::createOperation,
                            onChangeWalletName = presenter::changeWalletName,
                            onChangeCategoryLimit = presenter::changeCategoryLimit,
                            onLogOut = presenter::logout,
                        )
                    }

                    Contract.State.NoAuthorized -> {
                        println(actualState)
                        AuthScreen(
                            message = presenter.messages,
                            onAuth = presenter::auth,
                            onCreateUser = presenter::createUser,
                        )
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
