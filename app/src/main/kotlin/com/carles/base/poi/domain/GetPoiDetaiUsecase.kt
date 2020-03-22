package com.carles.base.poi.domain

import com.carles.base.core.domain.AppSchedulers
import com.carles.base.poi.data.PoiRepo
import io.reactivex.Single

class GetPoiDetaiUsecase(private val repository: PoiRepo, private val schedulers: AppSchedulers) {

    operator fun invoke(itemId: String): Single<PoiDetail> =
        repository.getPoiDetail(itemId, false).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}