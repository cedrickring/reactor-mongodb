package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.FindOneAndReplaceOptions
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class FindOneAndReplaceMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                               private val filter: Bson,
                               private val replacement: T,
                               private val findOneAndReplaceOptions: FindOneAndReplaceOptions) : Mono<T>() {

    override fun subscribe(actual: CoreSubscriber<in T>?) {
        reactiveCollection.nativeCollection.findOneAndReplace(filter, replacement, findOneAndReplaceOptions, { document, throwable ->
            if (throwable == null) {
                actual?.onNext(document)
            } else {
                actual?.onError(throwable)
            }
        })
    }

}