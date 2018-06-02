package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.FindOneAndDeleteOptions
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class FindOneAndDeleteMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                              private val filter: Bson,
                              private val findOneAndDeleteOptions: FindOneAndDeleteOptions) : Mono<T>() {

    override fun subscribe(actual: CoreSubscriber<in T>?) {
        reactiveCollection.nativeCollection.findOneAndDelete(filter, findOneAndDeleteOptions, { document, throwable ->
            if (throwable == null) {
                actual?.onNext(document)
            } else {
                actual?.onError(throwable)
            }
        })
    }

}