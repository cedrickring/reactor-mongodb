package com.cedrickring.reactormongodb.collection.flux

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.changestream.ChangeStreamDocument
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class WatchFlux<T, Result>(private val reactiveCollection: ReactiveCollection<T>,
                           private val pipeline: List<Bson>,
                           private val resultClass: Class<Result>) : Flux<ChangeStreamDocument<Result>>() {

    override fun subscribe(actual: CoreSubscriber<in ChangeStreamDocument<Result>>?) {
        reactiveCollection.nativeCollection.watch(pipeline, resultClass).forEach(
                { document -> actual?.onNext(document) },
                { _, throwable -> if (throwable != null) actual?.onError(throwable) }
        )
    }

}