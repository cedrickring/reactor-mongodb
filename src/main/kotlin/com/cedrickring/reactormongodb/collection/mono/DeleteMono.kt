package com.cedrickring.reactormongodb.collection.mono

import com.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.result.DeleteResult
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class DeleteMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                    private val filter: Bson,
                    private val deleteOptions: DeleteOptions,
                    private val many: Boolean = false) : Mono<DeleteResult>() {

    override fun subscribe(actual: CoreSubscriber<in DeleteResult>?) {
        val callback: (deleteResult: DeleteResult, throwable: Throwable?) -> Unit = { deleteResult, throwable ->
            if (throwable == null) {
                actual?.onNext(deleteResult)
            } else {
                actual?.onError(throwable)
            }
        }

        if (many) {
            reactiveCollection.nativeCollection.deleteMany(filter, deleteOptions, callback)
        } else {
            reactiveCollection.nativeCollection.deleteOne(filter, deleteOptions, callback)
        }
    }

}