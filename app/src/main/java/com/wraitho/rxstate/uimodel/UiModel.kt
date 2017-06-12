package com.wraitho.rxstate.uimodel

/**
 * This class represents the whole ui state. this is the "model" that is always mirrored to the view.
 */
class UiModel(val userName: String?, val inProgress: Boolean, val error: Throwable?) {
    fun  copy(userName: String?, inProgress: Boolean, error: Throwable?): UiModel {
        return UiModel(userName, inProgress, error)
    }
}
