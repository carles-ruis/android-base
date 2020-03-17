package com.carles.kotlin.poi.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.carles.kotlin.core.ui.*
import com.carles.kotlin.poi.domain.Poi
import com.carles.kotlin.poi.domain.FetchPoiListUsecase
import io.reactivex.disposables.CompositeDisposable

class PoiListViewModel(private val fetchPoiListUsecase: FetchPoiListUsecase) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _observablePoiList = MutableResourceLiveData<List<Poi>>()
    val observablePoiList: ResourceLiveData<List<Poi>> = _observablePoiList

    init {
        fetchPoiList()
    }

    private fun fetchPoiList() {
        fetchPoiListUsecase()
            .doOnSubscribe { _observablePoiList.setLoading() }
            .subscribe(::onGetPoiSuccess, ::onGetPoiError)
            .addTo(disposables)
    }

    @VisibleForTesting
    fun onGetPoiSuccess(data: List<Poi>) {
        _observablePoiList.setSuccess(data)
    }

    @VisibleForTesting
    fun onGetPoiError(throwable: Throwable) {
        _observablePoiList.setError(throwable.message)
    }

    fun retry() {
        fetchPoiList()
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
