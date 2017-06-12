package com.wraitho.rxstate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.wraitho.rxstate.result.AccountResult
import com.wraitho.rxstate.result.ForgotPassResult
import com.wraitho.rxstate.result.Result
import com.wraitho.rxstate.uimodel.UiModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


/**
 * This is a Presenter that deals with connecting the model with the UI
 * It has transformers that makes the connections.
 */
class MyPresenter(repository: MyRepository) : ViewModel() {

    val uiEvents: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    var currentState = UiModel(null, false, null)

    private val transformer: ObservableTransformer<UiEvent, UiModel>
    init {
        transformer = ObservableTransformer { events ->
            events.publish { shared ->
                Observable.merge(
                        shared.ofType(LoginClick::class.java).compose(loginTransformer),
                        shared.ofType(ForgotPassClick::class.java).compose(forgotPasswordTransformer))
                        .scan(currentState, { previous, result -> stateReducer(previous, result) })
            }
        }
    }

    val uiModelObservable: Observable<UiModel>
    init {
        uiModelObservable =
                uiEvents.compose(transformer)
                        .replay(1)
                        .autoConnect()
                        .doOnNext {
                            log(""""username: ${it.userName})
                            error: ${it.error.toString()}
                            inprogress: ${it.inProgress}""")
                        }
    }

    private val loginTransformer: ObservableTransformer<LoginClick, Result> = ObservableTransformer { events ->
        events.flatMap {
            repository.getAccount(it.user, it.pass)
                    //.doOnSubscribe { requested = true }
                    .map {
                        log("logintransformer map, $it")
                        AccountResult(user = it)
                    }
                    .onErrorReturn {
                        log("logintransformer onErrorReturn")
                        AccountResult(error = it)
                    }
                    .startWith(AccountResult(inProgress = true))
        }
    }

    private val forgotPasswordTransformer: ObservableTransformer<ForgotPassClick, Result> = ObservableTransformer { events ->
        events.flatMap {
            repository.forgotPass( it.user )
                    //.doOnSubscribe { requested = true }
                    .map { ForgotPassResult(secretQuestion = it.secretQuestion) }
                    .onErrorReturn { ForgotPassResult(error = it) }
                    .startWith(ForgotPassResult(inProgress = true))
        }
    }

    private fun stateReducer(previousState: UiModel, result: Result): UiModel {
        when (result) {
            is AccountResult -> {
                currentState = previousState.copy(
                        userName = result.user?.name,
                        inProgress = result.inProgress,
                        error = result.error)
            }
        }
        return currentState
    }

    class Factory @Inject constructor(val repository: MyRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>?): T {
            @Suppress("UNCHECKED_CAST")
            return MyPresenter(repository) as T
        }
    }

    fun log(what: String) {
        Log.d("RxState", what)
    }

}