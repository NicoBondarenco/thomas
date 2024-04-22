package com.thomas.jwt.auth0.util

import com.mongodb.ServerAddress
import com.mongodb.ServerCursor
import com.mongodb.client.MongoCursor
import org.bson.Document

internal class TestMongoCursor(
    documents: List<Document>,
) : MongoCursor<Document> {

    val iterator = documents.iterator()

    override fun remove() {}

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): Document = iterator.next()

    override fun close() {}

    override fun available(): Int = 1

    override fun tryNext(): Document? = if (hasNext()) iterator.next() else null

    override fun getServerCursor(): ServerCursor? = null

    override fun getServerAddress(): ServerAddress = ServerAddress("")

}