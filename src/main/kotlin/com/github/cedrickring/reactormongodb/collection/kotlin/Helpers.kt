package com.github.cedrickring.reactormongodb.collection.kotlin

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.changestream.ChangeStreamDocument
import org.bson.conversions.Bson
import reactor.core.publisher.Flux

inline fun <reified T : Any, R> ReactiveCollection<R>.distinct(fieldName: String, filter: Bson? = null): Flux<T> {
    return distinct(fieldName, T::class.java, filter)
}

inline fun <reified T : Any, R> ReactiveCollection<R>.findWithType(filter: Bson? = null): Flux<T> {
    return this.find(T::class.java, filter)
}

inline fun <reified T : Any, R> ReactiveCollection<R>.watchWithType(pipeline: List<Bson>): Flux<ChangeStreamDocument<T>> {
    return this.watch(pipeline, T::class.java)
}

inline fun <reified T : Any, R> ReactiveCollection<R>.mapReduceWithType(mapFunction: String, reduceFunction: String): Flux<T> {
    return this.mapReduce(mapFunction, reduceFunction, T::class.java)
}