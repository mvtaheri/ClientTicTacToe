package com.vahid.clienttictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vahid.clienttictactoe.presentation.TicTacToeField
import com.vahid.clienttictactoe.presentation.TicTacToeViewModel
import com.vahid.clienttictactoe.ui.theme.ClientTicTacToeTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientTicTacToeTheme {
                val viewModel = hiltViewModel<TicTacToeViewModel>()
                val state by viewModel.state.collectAsState()
                val isconnecting by viewModel.isConnecting.collectAsState()
                val showConnectionError by viewModel.showConnectionerror.collectAsState()
                // A surface container using the 'background' color from the theme
                if (showConnectionError) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Could not connect to server",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    return@ClientTicTacToeTheme
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        if (!state.connectedPlayers.contains('X')) {
                            Text(text = "waiting for player x", fontSize = 32.sp)
                        }
                        if (!state.connectedPlayers.contains('O')) {
                            Text(text = "waiting for player o", fontSize = 32.sp)
                        }
                    }
                    if (state.connectedPlayers.size == 2 && state.winningPlayer == null &&
                        !state.isBoardFull
                    ) {
                        Text(
                            text = if (state.playerAtTurn == 'X') {
                                "X is Next"
                            } else {
                                "O is Turn"
                            },
                            fontSize = 32.sp,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                    TicTacToeField(
                        state = state,
                        onTapInField = viewModel::finishTurn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(16.dp)
                    )
                    if (state.isBoardFull || state.winningPlayer != null) {
                        Text(
                            text = when (state.winningPlayer) {
                                'X' -> "player x is won"
                                'O' -> "player o is won"
                                else -> "it is draw"
                            },
                            fontSize = 32.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 32.dp)

                        )
                    }
                    if (isconnecting) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClientTicTacToeTheme {
        Greeting("Android")
    }
}