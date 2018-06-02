package com.cedrickring.reactormongodb.collection.mono

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.InsertOneOptions
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class InsertOneMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                       private val document: T,
                       private val insertOneOptions: InsertOneOptions) : Mono<Void>() {

    override fun subscribe(actual: CoreSubscriber<in Void>?) {
        reactiveCollection.nativeCollection.insertOne(document, insertOneOptions, { void, throwable ->
            if (throwable == null) {
                actual?.onNext(void)
            } else {
                actual?.onError(throwable)
            }
        })
    }

}