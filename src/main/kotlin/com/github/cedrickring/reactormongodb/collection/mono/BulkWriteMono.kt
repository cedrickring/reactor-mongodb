package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.WriteModel
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class BulkWriteMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                       private val documents: List<WriteModel<out T>>,
                       private val bulkWriteOptions: BulkWriteOptions) : Mono<BulkWriteResult>() {

    override fun subscribe(actual: CoreSubscriber<in BulkWriteResult>?) {
        reactiveCollection.nativeCollection.bulkWrite(documents, bulkWriteOptions, { result, throwable ->
            if (throwable == null) {
                actual?.onNext(result)
            } else {
                actual?.onError(throwable)
            }
        })
    }

}