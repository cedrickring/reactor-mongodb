package com.github.cedrickring.reactormongodb.collection.mono

import com.github.cedrickring.reactormongodb.collection.ReactiveCollection
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.UpdateResult
import org.bson.conversions.Bson
import reactor.core.CoreSubscriber
import reactor.core.publisher.Mono

class UpdateMono<T>(private val reactiveCollection: ReactiveCollection<T>,
                    private val filter: Bson,
                    private val update: Bson,
                    private val updateOptions: UpdateOptions,
                    private val many: Boolean = false) : Mono<UpdateResult>() {

    override fun subscribe(actual: CoreSubscriber<in UpdateResult>?) {
        val callback: (updateResult: UpdateResult, throwable: Throwable?) -> Unit = { updateResult, throwable ->
            if (throwable == null) {
                actual?.onNext(updateResult)
            } else {
                actual?.onError(throwable)
            }
        }

        if (many) {
            reactiveCollection.nativeCollection.updateMany(filter, update, updateOptions, callback)
        } else {
            reactiveCollection.nativeCollection.updateOne(filter, update, updateOptions, callback)
        }
    }

}