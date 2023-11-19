package com.vahid.clienttictactoe.di

import com.vahid.clienttictactoe.data.KtorRealTimeMessagingClient
import com.vahid.clienttictactoe.data.RealTimeMessagingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
        }
    }

    @Singleton
    @Provides
    fun provideRealTimeMessageingClient(httpClient: HttpClient): RealTimeMessagingClient {
        return KtorRealTimeMessagingClient(httpClient)
    }
}