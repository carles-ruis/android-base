package com.carles.base.domain.usecase

import com.carles.base.AppSchedulers
import com.carles.base.data.PoiRepo
import com.carles.base.domain.Poi
import io.reactivex.Single

class FetchPoiListUsecase(private val repository: PoiRepo, private val schedulers: AppSchedulers) {

    operator fun invoke(): Single<List<Poi>> = repository.getPoiList(true).subscribeOn(schedulers.io).observeOn(schedulers.ui)
}