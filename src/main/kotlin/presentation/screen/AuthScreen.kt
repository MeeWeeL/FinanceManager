package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    message: Flow<String>,
    onAuth: (login: String, password: String) -> Unit,
    onCreateUser: (login: String, password: String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var toast by remember { mutableStateOf<String?>(null) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = login,
            onValueChange = { newText ->
                login = newText
            },
            placeholder = {
                Text(text = "login")
            },
        )
        TextField(
            value = password,
            onValueChange = { newText ->
                password = newText
            },
            placeholder = {
                Text(text = "password")
            },
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.weight(2f),
                onClick = { onAuth(login, password) },
            ) {
                Text(text = "LogIn")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.weight(2f),
                onClick = { onCreateUser(login, password) },
            ) {
                Text(text = "New user")
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        toast?.let { message ->
            Text(
                modifier = Modifier
                    .background(color = Color.Red.copy(alpha = 0.5f))
                    .clickable { toast = null }
                    .padding(16.dp),
                text = message,
            )
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            message.collectLatest { message ->
                toast = message
            }
        }
    }
}