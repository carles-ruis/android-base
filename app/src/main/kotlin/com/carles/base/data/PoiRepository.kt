package com.carles.base.data

import com.carles.base.data.local.PoiLocalDatasource
import com.carles.base.data.remote.PoiRemoteDatasource
import com.carles.base.domain.Poi
import com.carles.base.domain.PoiDetail
import io.reactivex.Single

interface PoiRepo {

    fun getPoiList(refresh: Boolean = false): Single<List<Poi>>

    fun getPoiDetail(itemId: String, refresh: Boolean = false): Single<PoiDetail>

}

class PoiRepository(private val localDatasource: PoiLocalDatasource, private val remoteDatasource: PoiRemoteDatasource) :
    PoiRepo {

    override fun getPoiList(refresh: Boolean): Single<List<Poi>> = when (refresh) {
        true -> remoteDatasource.getPoiList().map { localDatasource.persist(it) }
        false -> localDatasource.getPoiList().onErrorResumeNext { getPoiList(true) }
    }

    override fun getPoiDetail(itemId: String, refresh: Boolean): Single<PoiDetail> = when (refresh) {
        true -> remoteDatasource.getPoiDetail(itemId).map { localDatasource.persist(it) }
        false -> localDatasource.getPoiDetail(itemId).onErrorResumeNext { getPoiDetail(itemId, true) }
    }
}