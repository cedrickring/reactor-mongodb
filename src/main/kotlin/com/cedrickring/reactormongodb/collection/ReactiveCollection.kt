package com.cedrickring.reactormongodb.collection

import com.cedrickring.reactormongodb.collection.flux.*
import com.cedrickring.reactormongodb.collection.mono.*
import com.mongodb.MongoNamespace
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.WriteConcern
import com.mongodb.async.client.MongoCollection
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.model.*
import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

class ReactiveCollection<T>(var nativeCollection: MongoCollection<T>) {

    val namespace: MongoNamespace
        get() = nativeCollection.namespace

    val documentClass: Class<T>
        get() = nativeCollection.documentClass

    val codecRegistry: CodecRegistry
        get() = nativeCollection.codecRegistry

    val writeConcern: WriteConcern
        get() = nativeCollection.writeConcern

    val readConcern: ReadConcern
        get() = nativeCollection.readConcern

    val readPreference: ReadPreference
        get() = nativeCollection.readPreference

    @JvmOverloads
    fun count(filter: Bson? = null, countOptions: CountOptions? = null): Mono<Long> =
            CountMono(this, filter, countOptions)

    @JvmOverloads
    fun <Result> distinct(fieldName: String, resultClass: Class<Result>, filter: Bson? = null): Flux<Result> =
            DistinctFlux(this, fieldName, filter, resultClass)

    @JvmOverloads
    fun <Result> find(resultClass: Class<Result>, filter: Bson? = null): Flux<Result> =
            FindFlux(this, filter, resultClass)

    @JvmOverloads
    fun find(filter: Bson? = null): Flux<T> =
            FindFlux(this, filter, documentClass)

    fun aggregate(pipeline: List<Bson>): Flux<T> =
            AggregateFlux(this, pipeline, documentClass)

    fun <Result> aggregate(pipeline: List<Bson>, resultClass: Class<Result>): Flux<Result> =
            AggregateFlux(this, pipeline, resultClass)

    fun watch(pipeline: List<Bson> = Collections.emptyList()): Flux<ChangeStreamDocument<T>> =
            WatchFlux(this, pipeline, documentClass)

    @JvmOverloads
    fun <Result> watch(pipeline: List<Bson> = Collections.emptyList(), resultClass: Class<Result>): Flux<ChangeStreamDocument<Result>> =
            WatchFlux(this, pipeline, resultClass)

    fun mapReduce(mapFunction: String, reduceFunction: String): Flux<T> =
            MapReduceFlux(this, mapFunction, reduceFunction, documentClass)

    fun <Result> mapReduce(mapFunction: String, reduceFunction: String, resultClass: Class<Result>): Flux<Result> =
            MapReduceFlux(this, mapFunction, reduceFunction, resultClass)

    @JvmOverloads
    fun bulkWrite(requests: List<WriteModel<out T>>, options: BulkWriteOptions = BulkWriteOptions()): Mono<BulkWriteResult> =
            BulkWriteMono(this, requests, options)

    @JvmOverloads
    fun insertOne(document: T, insertOneOptions: InsertOneOptions = InsertOneOptions()): Mono<Void> =
            InsertOneMono(this, document, insertOneOptions)

    @JvmOverloads
    fun insertMany(documents: List<T>, insertManyOptions: InsertManyOptions = InsertManyOptions()): Mono<Void> =
            InsertManyMono(this, documents, insertManyOptions)

    @JvmOverloads
    fun deleteOne(filter: Bson, deleteOptions: DeleteOptions = DeleteOptions()): Mono<DeleteResult> =
            DeleteMono(this, filter, deleteOptions)

    @JvmOverloads
    fun deleteMany(filter: Bson, deleteOptions: DeleteOptions = DeleteOptions()): Mono<DeleteResult> =
            DeleteMono(this, filter, deleteOptions, true)

    @JvmOverloads
    fun replaceOne(filter: Bson, replacement: T, replaceOptions: ReplaceOptions = ReplaceOptions()): Mono<UpdateResult> =
            ReplaceOneMono(this, filter, replacement, replaceOptions)

    @JvmOverloads
    fun updateOne(filter: Bson, update: Bson, updateOptions: UpdateOptions = UpdateOptions()): Mono<UpdateResult> =
            UpdateMono(this, filter, update, updateOptions)

    @JvmOverloads
    fun updateMany(filter: Bson, update: Bson, updateOptions: UpdateOptions = UpdateOptions()): Mono<UpdateResult> =
            UpdateMono(this, filter, update, updateOptions, true)

    @JvmOverloads
    fun findOneAndDelete(filter: Bson, findOneAndDeleteOptions: FindOneAndDeleteOptions = FindOneAndDeleteOptions()): Mono<T> =
            FindOneAndDeleteMono(this, filter, findOneAndDeleteOptions)

    @JvmOverloads
    fun findOneAndReplace(filter: Bson, replacement: T, findOneAndReplaceOptions: FindOneAndReplaceOptions = FindOneAndReplaceOptions()): Mono<T> =
            FindOneAndReplaceMono(this, filter, replacement, findOneAndReplaceOptions)

    @JvmOverloads
    fun findOneAndUpdate(filter: Bson, update: Bson, findOneAndUpdateOptions: FindOneAndUpdateOptions = FindOneAndUpdateOptions()): Mono<T> =
            FindOneAndUpdateMono(this, filter, update, findOneAndUpdateOptions)

    fun drop(): Mono<Void> {
        return Mono.from {
            nativeCollection.drop { void, throwable -> if (throwable != null) it.onError(throwable) else it.onNext(void) }
        }
    }


    fun <NewT> withDocumentClass(clazz: Class<NewT>): ReactiveCollection<NewT> {
        return ReactiveCollection(nativeCollection.withDocumentClass(clazz))
    }

    fun withCodecRegistry(codecRegistry: CodecRegistry): ReactiveCollection<T> {
        nativeCollection = nativeCollection.withCodecRegistry(codecRegistry)
        return this
    }

    fun withReadPreference(readPreference: ReadPreference): ReactiveCollection<T> {
        nativeCollection = nativeCollection.withReadPreference(readPreference)
        return this
    }

    fun withWriteConcern(writeConcern: WriteConcern): ReactiveCollection<T> {
        nativeCollection = nativeCollection.withWriteConcern(writeConcern)
        return this
    }

    fun withReadConcern(readConcern: ReadConcern): ReactiveCollection<T> {
        nativeCollection = nativeCollection.withReadConcern(readConcern)
        return this
    }

}

