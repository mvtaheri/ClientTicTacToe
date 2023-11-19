package com.vahid.clienttictactoe.data

import com.vahid.clienttictactoe.models.GameState
import com.vahid.clienttictactoe.models.MakeTurn
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRealTimeMessagingClient(
    private val client: HttpClient
) : RealTimeMessagingClient {
    private var session: WebSocketSession? = null
    override fun getGameStateStream(): Flow<GameState> {
        return flow {
            val session = client.webSocketSession {
                url("ws://")
            }
            val gameState = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull {
                    Json.decodeFromString<GameState>(it.readText())
                }
            emitAll(gameState)
        }
    }

    override suspend fun sendAction(action: MakeTurn) {
        session?.outgoing?.send(
            Frame.Text("make_turn${Json.encodeToString(action)}")
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

}