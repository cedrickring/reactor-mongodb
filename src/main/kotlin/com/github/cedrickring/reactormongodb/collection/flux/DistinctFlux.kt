package com.github.cedrickring.reactormongodb.collection.flux

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class DistinctFlux<T, Result>(private val reactiveCollection: ReactiveCollection<T>,
                              private val fieldName: String,
                              private val filter: Bson?,
                              private val resultClass: Class<Result>) : Flux<Result>() {

    override fun subscribe(actual: CoreSubscriber<in Result>?) {
        reactiveCollection.nativeCollection.distinct(fieldName, filter ?: Document(), resultClass)
                .forEach(
                        { result -> actual?.onNext(result) },
                        { _, throwable -> actual?.onError(throwable) }
                )
    }

}