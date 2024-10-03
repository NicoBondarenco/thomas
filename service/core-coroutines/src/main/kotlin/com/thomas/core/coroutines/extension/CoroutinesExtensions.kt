package com.thomas.core.coroutines.extension

import com.thomas.core.context.SessionContextHolder
import com.thomas.core.coroutines.context.CoroutineSessionContext
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

val Dispatchers.VT
    get() =  Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()

suspend fun <T> withSessionContext(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T = withContext(context = (context + CoroutineSessionContext(context = SessionContextHolder.context)), block)

suspend fun <T> withSessionContextIO(
    block: suspend CoroutineScope.() -> T
): T = withSessionContext(Dispatchers.IO, block)

suspend fun <T> withSessionContextVT(
    block: suspend CoroutineScope.() -> T
): T = withSessionContext(Dispatchers.VT, block)
