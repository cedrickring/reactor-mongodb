# Reactor MongoDB
A wrapper over the mongodb-async api with Reactor (http://projectreactor.io/) written in Kotlin, but fully compatible
with Java (due to `@JvmOverloads`).

## Dependencies

* `org.mongodb::mongodb-driver-async`
* `io.projectreactor::reactor-core`

## Maven

```xml
<dependency>
    <groupId>com.github.cedrickring</groupId>
    <artifactId>reactor-mongodb</artifactId>
    <version>1.7</version>
</dependency>
```

## Example Usage

Java:
```java
ReactiveMongoClient client = ReactiveMongoClient.fromURI("mongodb://<URL>");
ReactiveDatabase database = client.getDatabase("<name>"); //get a reactive database

//To get a collection, use
ReactiveCollection<Document> collection = database.getCollection("<name>");

//or with a specific type
ReactiveCollection<MyType> collection = database.getCollection("<name>");

//you can also use factory methods known from MongoCollection
collection.withWriteConcern(...).withCodecRegistry(...);

//To find documents in a collection, use
Flux<Document> documents = collection.find(Filters.eq("name", "John"));

//then subscribe to it
documents.subscribe(document -> document.getString("name"));

//or use other functions from Flux
documents.map(document -> document.getString("name")).subscribe(...);
```

Kotlin:
```kotlin
val client = ReactiveMongoClient.fromURI("mongodb://<URL>")
val database = client.getDatabase("name")

//in Kotlin we have some useful methods to avoid usage of T::class.java
val collection = database.collection<Type>("<name>")

//find documents of a specific type
val find: Flux<Type> = collection.findWithType<Type>(Filters.eq("my", "filter"))

//or without a specified type
val find: Flux<Document> = collection.find(...)

//then just subscribe to it
find.subscribe { println(it) }
```