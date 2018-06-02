package com.cedrickring.reactormongodb.database.flux

import com.cedrickring.reactormongodb.database.ReactiveDatabase
import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class ListCollectionFlux(private val reactiveDatabase: ReactiveDatabase) : Flux<String>() {

    override fun subscribe(actual: CoreSubscriber<in String>?) {
        reactiveDatabase.nativeDatabase.listCollectionNames()
                .forEach(
                        { name -> actual?.onNext(name) },
                        { _, throwable -> if (throwable != null) actual?.onError(throwable) }
                )
    }

}