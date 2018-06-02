package com.cedrickring.reactormongodb.client

import reactor.core.CoreSubscriber
import reactor.core.publisher.Flux

class ListDatabaseFlux(private val client: ReactiveMongoClient) : Flux<String>() {

    override fun subscribe(actual: CoreSubscriber<in String>?) {
        client.mongoClient.listDatabaseNames().forEach(
                { name -> actual?.onNext(name) },
                { _, throwable -> if (throwable != null) actual?.onError(throwable) }
        )
    }

}