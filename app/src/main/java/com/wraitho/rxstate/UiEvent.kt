package com.wraitho.rxstate

sealed class UiEvent
data class LoginClick(val user: String, val pass: String) : UiEvent()
data class ForgotPassClick(val user: String) : UiEvent()
