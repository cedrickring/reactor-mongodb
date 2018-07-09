package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.FindOneAndUpdateOptions
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class FindOneAndUpdateMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                              private val filter: Bson,
                              private val update: Bson,
                              private val findOneAndUpdateOptions: FindOneAndUpdateOptions) : Mono<T>() {

    override fun subscribe(actual: CoreSubscriber<in T>?) {
        reactiveCollection.nativeCollection.findOneAndUpdate(filter, update, findOneAndUpdateOptions) { document, throwable ->
            if (throwable == null) {
                actual?.onNext(document)
            } else {
                actual?.onError(throwable)
            }
        }
    }

}