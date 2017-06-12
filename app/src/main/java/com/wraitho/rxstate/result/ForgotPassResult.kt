package com.wraitho.rxstate.result

/**
 * This class is a result from the model layer
 */
data class ForgotPassResult(val secretQuestion: String? = null,
                            val inProgress: Boolean = false,
                            val error: Throwable? = null): Result()