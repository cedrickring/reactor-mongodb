package com.cedrickring.reactormongodb.collection.flux

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class AggregateFlux<T, Result>(private val reactiveCollection: ReactiveCollection<T>,
                               private val pipeline: List<Bson>,
                               private val resultClass: Class<Result>) : Flux<Result>() {

    override fun subscribe(actual: CoreSubscriber<in Result>?) {
        reactiveCollection.nativeCollection.aggregate(pipeline, resultClass).forEach(
                { result -> actual?.onNext(result) },
                { _, throwable -> actual?.onError(throwable) }
        )
    }

}