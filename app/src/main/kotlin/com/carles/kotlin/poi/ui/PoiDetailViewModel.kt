package com.carles.kotlin.poi.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.carles.kotlin.core.ui.*
import com.carles.kotlin.poi.domain.PoiDetail
import com.carles.kotlin.poi.domain.GetPoiDetaiUsecase
import io.reactivex.disposables.CompositeDisposable

class PoiDetailViewModel(private val id: String, private val getPoiDetailUsecase: GetPoiDetaiUsecase) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _observablePoiDetail = MutableResourceLiveData<PoiDetail>()
    val observablePoiDetail: ResourceLiveData<PoiDetail> = _observablePoiDetail

    init {
        getPoiDetail()
    }

    private fun getPoiDetail() {
        getPoiDetailUsecase(id).doOnSubscribe { _observablePoiDetail.setLoading() }
            .subscribe(::onGetPoiDetailSuccess, ::onGetPoiDetailError)
            .addTo(disposables)
    }

    @VisibleForTesting
    fun onGetPoiDetailSuccess(data: PoiDetail) {
        _observablePoiDetail.setSuccess(data)
    }

    @VisibleForTesting
    fun onGetPoiDetailError(throwable: Throwable) {
        _observablePoiDetail.setError(throwable.message)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
