package com.carles.base.poi.domain

import com.carles.base.core.domain.AppSchedulers
import com.carles.base.poi.data.PoiRepo
import io.reactivex.Single

class FetchPoiListUsecase(private val repository: PoiRepo, private val schedulers: AppSchedulers) {

    operator fun invoke(): Single<List<Poi>> = repository.getPoiList(true).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}