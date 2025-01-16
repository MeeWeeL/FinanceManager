package presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Category
import model.OperationType
import model.Wallet
import presentation.Contract

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CabinetScreen(
    state: Contract.State.Authorized,
    onCreateWallet: (userId: Int, name: String) -> Unit,
    onCreateCategory: (userId: Int, walletId: Int, name: String) -> Unit,
    onCreateOperation: (userId: Int, categoryId: Int, amount: Int, type: OperationType) -> Unit,
    onChangeWalletName: (userId: Int, walletId: Int, newName: String) -> Unit,
    onChangeCategoryLimit: (userId: Int, categoryId: Int, limit: Int) -> Unit,
    onLogOut: () -> Unit,
) {
    var actualWallet by remember { mutableStateOf<Wallet?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Hello, ${state.user.login}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Button(onClick = onLogOut) {
                Text(
                    text = "Exit",
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Wallets",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        var newWalletName by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newWalletName,
                onValueChange = {
                    newWalletName = it
                },
                placeholder = {
                    Text(text = "New wallet name")
                },
            )
            Button(onClick = {
                onCreateWallet(state.user.id, newWalletName)
                newWalletName = ""
            }) {
                Text("Create")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.user.wallets.isEmpty()) {
                Text(text = "Empty")
            } else {
                state.user.wallets.forEach { wallet ->
                    WalletBox(
                        onClick = { actualWallet = wallet },
                        wallet = wallet,
                    )
                }
            }
        }

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Actual wallet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        if (actualWallet == null) {
            Text(text = "Empty")
        } else {
            Text(text = "Wallet name: ${actualWallet?.name}")
            Text(text = "Wallet balance: ${actualWallet?.balance}")

            val walletExpenses = actualWallet?.categories?.sumOf { category ->
                category.operations
                    .filter { it.type == OperationType.EXPENSE }
                    .sumOf { it.amount }
            }

            Text(text = "Wallet expenses: $walletExpenses")

            val walletIncome = actualWallet?.categories?.sumOf { category ->
                category.operations
                    .filter { it.type == OperationType.INCOME }
                    .sumOf { it.amount }
            }

            Text(text = "Wallet income: $walletIncome")

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Categories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            var newCategoryName by remember { mutableStateOf("") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextField(
                    value = newCategoryName,
                    onValueChange = {
                        newCategoryName = it
                    },
                    placeholder = {
                        Text(text = "New Category name")
                    },
                )
                Button(onClick = {
                    onCreateCategory(state.user.id, actualWallet!!.id, newCategoryName)
                    newCategoryName = ""
                }) {
                    Text("Create")
                }
            }

            actualWallet?.categories?.forEach { category ->
                Category(
                    category = category,
                    onCreateOperation = { operationAmount, type ->
                        onCreateOperation(
                            state.user.id,
                            category.id,
                            operationAmount,
                            type,
                        )
                    },
                    onChangeCategoryLimit = { newLimit ->
                        onChangeCategoryLimit(
                            state.user.id,
                            category.id,
                            newLimit,
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletBox(
    onClick: () -> Unit,
    wallet: Wallet,
) {
    Text(
        modifier = Modifier
            .size(200.dp)
            .background(color = Color.Blue.copy(alpha = 0.3f))
            .onClick(onClick = onClick)
            .padding(16.dp),
        text = wallet.name,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ColumnScope.Category(
    category: Category,
    onCreateOperation: (operationAmount: Int, type: OperationType) -> Unit,
    onChangeCategoryLimit: (newLimit: Int) -> Unit,
) {
    val name = "Category: ${category.name}"
    val expenses = "Expenses: " + category.operations
        .filter { it.type == OperationType.EXPENSE && it.categoryId == category.id }
        .sumOf { it.amount }
    val incomes = "Incomes: " + category.operations
        .filter { it.type == OperationType.INCOME && it.categoryId == category.id }
        .sumOf { it.amount }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = if (category.limit != null && category.limit!! < 0) Color.Red.copy(alpha = 0.4f)
                    else Color.Green.copy(alpha = 0.3f)
                )
                .padding(8.dp),
            text = "Category: $name\n" +
                    "Expenses: $expenses\n" +
                    "Incomes: $incomes\n" +
                    "Limit: ${category.limit}\n",
        )
        var operationAmount by remember { mutableStateOf("") }
        TextField(
            value = operationAmount,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                    operationAmount = newValue
                }
            },
            placeholder = {
                Text(text = "amount")
            },
        )
        Button(onClick = {
            if (operationAmount.isNotEmpty()) {
                onCreateOperation(
                    operationAmount.toInt(),
                    OperationType.EXPENSE,
                )
                operationAmount = ""
            }
        }) {
            Text(text = "EXPOSE")
        }
        Button(
            onClick = {
                if (operationAmount.isNotEmpty()) {
                    onCreateOperation(
                        operationAmount.toInt(),
                        OperationType.INCOME,
                    )
                    operationAmount = ""
                }
            }
        ) {
            Text(text = "INCOME")
        }
        Button(
            onClick = {
                if (operationAmount.isNotEmpty()) {
                    onChangeCategoryLimit(operationAmount.toInt())
                    operationAmount = ""
                }
            }
        ) {
            Text(text = "Set limit")
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

    }
}