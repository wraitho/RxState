package com.wraitho.rxstate.result

import com.wraitho.rxstate.fromapi.User

/**
 * This class is a result from the model layer
 */
data class AccountResult(val inProgress: Boolean = false,
                         val error: Throwable? = null,
                         val user: User? = null) : Result()