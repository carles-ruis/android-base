package com.carles.base.domain.usecase

import com.carles.base.AppSchedulers
import com.carles.base.data.PoiRepo
import com.carles.base.domain.PoiDetail
import io.reactivex.Single

class GetPoiDetaiUsecase(private val repository: PoiRepo, private val schedulers: AppSchedulers) {

    operator fun invoke(itemId: String): Single<PoiDetail> =
        repository.getPoiDetail(itemId, false).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}