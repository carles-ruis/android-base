package com.carles.kotlin.poi.data.datasourcefactory

import com.carles.kotlin.core.data.Cache
import com.carles.kotlin.core.data.CacheItems
import com.carles.kotlin.core.data.CacheKey
import com.carles.kotlin.poi.data.PoiApi
import com.carles.kotlin.poi.data.PoiDao
import com.carles.kotlin.poi.data.toModel
import com.carles.kotlin.poi.domain.Poi
import com.carles.kotlin.poi.domain.PoiDetail
import io.reactivex.Single

class _PoiDatasourceFactory(
    private val localDatasource: _PoiLocalDatasource,
    private val remoteDatasource: _PoiRemoteDatasource,
    private val cache: Cache
) {

    fun retrieveDatasource(refresh: Boolean, key: CacheKey): _PoiDatasource = when {
        refresh -> remoteDatasource
        cache.isCached(key) -> localDatasource
        else -> remoteDatasource
    }

    fun retrieveLocalDatasource() = localDatasource

    fun retrieveRemoteDatasource() = remoteDatasource
}

interface _PoiDatasource {

    fun getPoiList(): Single<List<Poi>>

    fun getPoiDetail(itemId: String): Single<PoiDetail>
}

class _PoiLocalDatasource(private val cache: Cache, private val dao: PoiDao) : _PoiDatasource {

    override fun getPoiList() = dao.loadPois()

    fun persist(pois: List<Poi>): Single<List<Poi>> {
        dao.deletePois()
        dao.insertPois(pois)
        cache.set(CacheKey(CacheItems.POI_LIST))
        return Single.just(pois)
    }

    override fun getPoiDetail(itemId: String) = dao.loadPoiById(itemId)

    fun persist(poi: PoiDetail): Single<PoiDetail> {
        dao.insertPoi(poi)
        cache.set(CacheKey(CacheItems.POI_DETAIL, poi.id))
        return Single.just(poi)
    }
}

class _PoiRemoteDatasource(private val api: PoiApi) : _PoiDatasource {

    override fun getPoiList() = api.getPoiList().map { it.toModel() }

    override fun getPoiDetail(itemId: String) = api.getPoiDetail(itemId).map { it.toModel() }
}