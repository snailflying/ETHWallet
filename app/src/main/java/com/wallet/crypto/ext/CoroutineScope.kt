package com.wallet.crypto.ext

import kotlin.coroutines.experimental.CoroutineContext

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 21/06/2018 3:33 PM
 * @Version
 */
public interface CoroutineScope {
    /**
     * Returns `true` when this coroutine is still active (has not completed yet).
     *
     * Check this property in long-running computation loops to support cancellation:
     * ```
     * while (isActive) {
     *     // do some computation
     * }
     * ```
     *
     * This property is a shortcut for `context[Job]!!.isActive`. See [context] and [Job].
     */
    public val isActive: Boolean

    /**
     * Returns the context of this coroutine.
     */
    public val context: CoroutineContext
}