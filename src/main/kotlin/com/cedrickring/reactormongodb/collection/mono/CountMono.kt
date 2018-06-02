package com.cedrickring.reactormongodb.collection.mono

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.CountOptions
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class CountMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                   private val filter: Bson?,
                   private val countOptions: CountOptions?) : Mono<Long>() {

    override fun subscribe(actual: CoreSubscriber<in Long>?) {
        reactiveCollection.nativeCollection.count(filter ?: Document(), countOptions
                ?: CountOptions(), { count, throwable ->
            if (throwable != null) {
                actual?.onError(throwable)
            } else {
                actual?.onNext(count)
            }
        })
    }

}