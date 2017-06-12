package com.wraitho.rxstate

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.jakewharton.rxbinding2.view.clicks
import com.wraitho.rxstate.uimodel.UiModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject lateinit var factory: MyPresenter.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()

        val presenter = ViewModelProviders.of(this, factory).get(MyPresenter::class.java)

        val uiEvents: Observable<UiEvent> = Observable.merge(
                login.clicks().map { LoginClick(user.text.toString(), pass.text.toString()) }.doOnNext { login.hideKeyboard() },
                forgot.clicks().map { ForgotPassClick(user.text.toString()) })
        uiEvents.subscribe(presenter.uiEvents)

        presenter.uiModelObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(this::render)
    }

    private fun render(uiModel: UiModel) {
        progress.visibility = if (uiModel.inProgress) VISIBLE else GONE

        if (uiModel.userName != null) {
            toast("Logged in dear " + uiModel.userName)
            loggedinusername.text = uiModel.userName
            loggedinusername.visibility = VISIBLE
        } else {
            loggedinusername.visibility = GONE
        }

        if (uiModel.error != null) {
            toast("error")
        }

    }

    private fun injectDependencies() {
        val injector = ViewModelProviders.of(this).get(DependencyInjector::class.java)
        injector.initComponent(this::createAndInjectComponent)
        injector.component?.inject(this)
    }

    private fun createAndInjectComponent(): MyComponent {
        return DaggerMyComponent.builder().build()
    }
}