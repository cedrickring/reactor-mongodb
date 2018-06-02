package com.cedrickring.reactormongodb.collection.flux

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class MapReduceFlux<T, Result>(private val reactiveCollection: ReactiveCollection<T>,
                               private val mapFunction: String,
                               private val reduceFunction: String,
                               private val resultClass: Class<Result>) : Flux<Result>() {

    override fun subscribe(actual: CoreSubscriber<in Result>?) {
        reactiveCollection.nativeCollection.mapReduce(mapFunction, reduceFunction, resultClass)
                .forEach(
                        { result -> actual?.onNext(result) },
                        { _, throwable -> if (throwable != null) actual?.onError(throwable) }
                )
    }

}
