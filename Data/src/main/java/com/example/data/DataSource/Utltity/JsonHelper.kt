package com.example.data.DataSource.Utltity

import com.example.domain.GameEvents.Event
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
                    subclass(Event.GameEvent::class,Event.GameEvent.serializer())
                    subclass(Event.RoomStateEvent::class,Event.RoomStateEvent.serializer())
                    subclass(Event.JoinedGameEvent::class,Event.JoinedGameEvent.serializer())
                    subclass(Event.CardPlayEvent::class,Event.CardPlayEvent.serializer())
                    subclass(Event.LiarCallEvent::class,Event.LiarCallEvent.serializer())
                    subclass(Event.PlayerShotEvent::class,Event.PlayerShotEvent.serializer())

                }
            }
        }
}