package com.carles.kotlin.poi.domain

import com.carles.kotlin.core.domain.AppSchedulers
import com.carles.kotlin.poi.data.PoiRepo
import com.carles.kotlin.poi.data.PoiRepository
import io.reactivex.Single

class GetPoiDetaiUsecase(private val repository: PoiRepo, private val schedulers: AppSchedulers) {

    operator fun invoke(itemId: String): Single<PoiDetail> =
        repository.getPoiDetail(itemId, false).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}