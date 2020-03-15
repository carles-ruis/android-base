package com.carles.kotlin.poi.data

import com.carles.kotlin.poi.domain.Poi
import com.carles.kotlin.poi.domain.PoiDetail
import io.reactivex.Single

class PoiRepository(private val localDatasource: PoiLocalDatasource, private val remoteDatasource: PoiRemoteDatasource) {

    fun getPoiList(refresh: Boolean = false): Single<List<Poi>> = when (refresh) {
        true -> remoteDatasource.getPoiList().map { localDatasource.persist(it) }
        false -> localDatasource.getPoiList().onErrorResumeNext { getPoiList(true) }
    }

    fun getPoiDetail(itemId: String, refresh: Boolean = false): Single<PoiDetail> = when (refresh) {
        true -> remoteDatasource.getPoiDetail(itemId).map { localDatasource.persist(it) }
        false -> localDatasource.getPoiDetail(itemId).onErrorResumeNext { getPoiDetail(itemId, true) }
    }
}