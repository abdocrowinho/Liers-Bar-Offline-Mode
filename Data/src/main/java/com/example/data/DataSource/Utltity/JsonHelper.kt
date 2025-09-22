package com.example.data.DataSource.Utltity

import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.WarningEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

object JsonHelper {
    val Json =
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true

            classDiscriminator="type"

            serializersModule = SerializersModule {
                polymorphic(Event::class){
                    subclass(RoomStateEvent::class,RoomStateEvent.serializer())
                    subclass(JoinToGameEvent::class ,JoinToGameEvent.serializer())
                    subclass(CardPlayEvent::class,CardPlayEvent.serializer())
                    subclass(LiarCallEvent::class,LiarCallEvent.serializer())
                    subclass(PlayerShotEvent::class,PlayerShotEvent.serializer())
                    subclass(WarningEvent::class,WarningEvent.serializer())
                }
            }
        }
}