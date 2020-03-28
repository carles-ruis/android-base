package com.carles.base.ui.poilist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.carles.base.domain.Poi
import com.carles.base.domain.usecase.FetchPoiListUsecase
import com.carles.base.ui.MutableResourceLiveData
import com.carles.base.ui.ResourceLiveData
import com.carles.base.ui.addTo
import com.carles.base.ui.setError
import com.carles.base.ui.setLoading
import com.carles.base.ui.setSuccess
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
    private fun onGetPoiSuccess(data: List<Poi>) {
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
