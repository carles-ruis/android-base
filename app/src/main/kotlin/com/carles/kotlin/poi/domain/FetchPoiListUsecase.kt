package com.carles.kotlin.poi.domain

import com.carles.kotlin.core.domain.AppSchedulers
import com.carles.kotlin.poi.data.PoiRepository
import io.reactivex.Single

class FetchPoiListUsecase(private val repository: PoiRepository, private val schedulers: AppSchedulers) {

    operator fun invoke(): Single<List<Poi>> = repository.getPoiList(true).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}