package com.wraitho.rxstate

import dagger.Component
import javax.inject.Singleton

@Component @Singleton
interface MyComponent {
    fun inject(myactivity : MainActivity)
}