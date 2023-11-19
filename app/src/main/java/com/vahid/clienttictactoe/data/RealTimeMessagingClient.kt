package com.vahid.clienttictactoe.data

import com.vahid.clienttictactoe.models.GameState
import com.vahid.clienttictactoe.models.MakeTurn
import kotlinx.coroutines.flow.Flow

interface RealTimeMessagingClient {
    fun getGameStateStream(): Flow<GameState>
    suspend fun sendAction(action: MakeTurn)
    suspend fun close()
}