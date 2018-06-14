package com.wraitho.rxstate

import android.util.Log
import com.wraitho.rxstate.fromapi.ForgotPass
import com.wraitho.rxstate.fromapi.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MyRepository @Inject constructor() {
    fun getAccount(user: String, pass: String): Observable<User> {
        Log.d("RxState", "getting the account")
        return if ("bla".equals(user, ignoreCase = true) && "bla".equals(pass, ignoreCase = true)) {
            Log.d("RxState", "COOL")
            Observable.fromArray(User("bla", "bla")).delay(3, TimeUnit.SECONDS)
        } else {
            Log.d("RxState", "NOT SO COOL")
            Observable.error(WrongCredentials())
        }
    }

    fun forgotPass(user: String): Observable<ForgotPass> {
        return Observable.just(ForgotPass( secretQuestion = "what is your favourite name?"))
    }
}
