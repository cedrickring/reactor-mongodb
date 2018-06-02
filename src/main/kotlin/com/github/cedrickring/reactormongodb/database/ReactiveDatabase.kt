package com.github.cedrickring.reactormongodb.database

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.github.cedrickring.reactormongodb.database.flux.ListCollectionFlux
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.async.client.MongoDatabase
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistry
import reactor.core.publisher.Flux

class ReactiveDatabase(private var mongoDatabase: MongoDatabase) {

    val nativeDatabase: MongoDatabase
        get() = mongoDatabase

    fun listCollectionNames(): Flux<String> = ListCollectionFlux(this)

    fun createCollection(name: String, errorCallback: (error: Throwable?) -> Unit = {}) {
        mongoDatabase.createCollection(name, { _, throwable -> errorCallback(throwable) })
    }

    fun getCollection(name: String): ReactiveCollection<Document> {
        return ReactiveCollection(mongoDatabase.getCollection(name))
    }

    fun <T> getCollection(name: String, clazz: Class<T>): ReactiveCollection<T> {
        return ReactiveCollection(mongoDatabase.getCollection(name, clazz))
    }

    fun drop(errorCallback: (error: Throwable?) -> Unit = {}) {
        mongoDatabase.drop { _, throwable -> errorCallback(throwable) }
    }

    fun withCodecRegistry(codecRegistry: CodecRegistry): ReactiveDatabase {
        mongoDatabase = mongoDatabase.withCodecRegistry(codecRegistry)
        return this
    }

    fun withReadPreference(readPreference: ReadPreference): ReactiveDatabase {
        mongoDatabase = mongoDatabase.withReadPreference(readPreference)
        return this
    }

    fun withWriteConcern(writeConcern: WriteConcern): ReactiveDatabase {
        mongoDatabase = mongoDatabase.withWriteConcern(writeConcern)
        return this
    }

    fun withReadConcern(readConcern: ReadConcern): ReactiveDatabase {
        mongoDatabase = mongoDatabase.withReadConcern(readConcern)
        return this
    }

}

inline fun <reified T : Any> ReactiveDatabase.collection(name: String): ReactiveCollection<T> {
    return this.getCollection(name, T::class.java)
}