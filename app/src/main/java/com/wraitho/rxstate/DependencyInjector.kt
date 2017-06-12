package com.wraitho.rxstate

import android.arch.lifecycle.ViewModel

/**
 * This class simplifies the DI, so we can have true singletons on a feature level.
 * Basically this is the viewmodel of the feature holder activity. so until the activity lives, the component also.
 */
class DependencyInjector : ViewModel() {
    var component : MyComponent? = null

    internal fun initComponent(initializer: () -> MyComponent) {
        if (component == null) {
            component = initializer.invoke()
        }
    }
}