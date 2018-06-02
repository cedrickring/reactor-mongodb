package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.result.UpdateResult
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class ReplaceOneMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                        private val filter: Bson,
                        private val replacement: T,
                        private val replaceOptions: ReplaceOptions) : Mono<UpdateResult>() {

    override fun subscribe(actual: CoreSubscriber<in UpdateResult>?) {
        reactiveCollection.nativeCollection.replaceOne(filter, replacement, replaceOptions, { updateResult, throwable ->
            if (throwable == null) {
                actual?.onNext(updateResult)
            } else {
                actual?.onError(throwable)
            }
        })
    }

}