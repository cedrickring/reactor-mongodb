package com.github.cedrickring.reactormongodb.client

import com.github.cedrickring.reactormongodb.database.ReactiveDatabase
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.async.client.MongoClient
import com.mongodb.async.client.MongoClients
import reactor.core.publisher.Flux

class ReactiveMongoClient private constructor(val mongoClient: MongoClient) {

    companion object {

        @JvmStatic
        fun fromURI(uri: String) = ReactiveMongoClient(MongoClients.create(uri))

        @JvmStatic
        fun fromConnectionString(connectionString: ConnectionString) = ReactiveMongoClient(MongoClients.create(connectionString))

        @JvmStatic
        fun fromSettings(settings: MongoClientSettings) = ReactiveMongoClient(MongoClients.create(settings))

    }

    val mongoClientSettings
        get() = mongoClient.settings

    fun listDatabaseNames(): Flux<String> = ListDatabaseFlux(this)

    fun getDatabase(name: String) = ReactiveDatabase(mongoClient.getDatabase(name))

}