package com.wraitho.rxstate.fromapi

/**
 * Simple class to represent an API response.
 */
data class User(val name: String, val pass: String, val error: Throwable? = null)