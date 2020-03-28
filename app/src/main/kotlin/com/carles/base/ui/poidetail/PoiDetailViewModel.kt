package com.carles.base.ui.poidetail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.carles.base.domain.PoiDetail
import com.carles.base.domain.usecase.GetPoiDetaiUsecase
import com.carles.base.ui.MutableResourceLiveData
import com.carles.base.ui.ResourceLiveData
import com.carles.base.ui.addTo
import com.carles.base.ui.setError
import com.carles.base.ui.setLoading
import com.carles.base.ui.setSuccess
import io.reactivex.disposables.CompositeDisposable

class PoiDetailViewModel(private val id: String, private val getPoiDetailUsecase: GetPoiDetaiUsecase) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val _observablePoiDetail = MutableResourceLiveData<PoiDetail>()
    val observablePoiDetail: ResourceLiveData<PoiDetail> = _observablePoiDetail

    init {
        getPoiDetail()
    }

    private fun getPoiDetail() {
        getPoiDetailUsecase(id)
            .doOnSubscribe { _observablePoiDetail.setLoading() }
            .subscribe(::onGetPoiDetailSuccess, ::onGetPoiDetailError)
            .addTo(disposables)
    }

    private fun onGetPoiDetailSuccess(data: PoiDetail) {
        _observablePoiDetail.setSuccess(data)
    }

    private fun onGetPoiDetailError(throwable: Throwable) {
        _observablePoiDetail.setError(throwable.message)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
