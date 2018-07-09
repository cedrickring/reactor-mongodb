package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.InsertManyOptions
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class InsertManyMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                        private val documents: List<T>,
                        private val insertManyOptions: InsertManyOptions) : Mono<Void>() {

    override fun subscribe(actual: CoreSubscriber<in Void>?) {
        reactiveCollection.nativeCollection.insertMany(documents, insertManyOptions) { void, throwable ->
            if (throwable == null) {
                actual?.onNext(void)
            } else {
                actual?.onError(throwable)
            }
        }
    }

}