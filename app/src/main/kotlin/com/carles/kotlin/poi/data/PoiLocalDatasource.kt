package com.carles.kotlin.poi.data

import com.carles.kotlin.core.data.Cache
import com.carles.kotlin.core.data.CacheItems
import com.carles.kotlin.core.data.ItemNotCachedException
import com.carles.kotlin.poi.domain.Poi
import com.carles.kotlin.poi.domain.PoiDetail
import io.reactivex.Single

class PoiLocalDatasource(private val dao: PoiDao, private val cache: Cache) {

    fun getPoiList(): Single<List<Poi>> =
        if (cache.isCached(CacheItems.POI_LIST)) {
            dao.loadPois()
        } else {
            Single.error(ItemNotCachedException)
        }

    fun persist(pois: List<Poi>) = with(pois) {
        dao.deletePois()
        dao.insertPois(pois)
        cache.set(CacheItems.POI_LIST)
        this
    }

    fun getPoiDetail(itemId: String): Single<PoiDetail> =
        if (cache.isCached(CacheItems.POI_DETAIL, itemId)) {
            dao.loadPoiById(itemId)
        } else {
            Single.error<PoiDetail>(ItemNotCachedException)
        }

    fun persist(poi: PoiDetail) = with(poi) {
        dao.insertPoi(poi)
        cache.set(CacheItems.POI_DETAIL, poi.id)
        this
    }
}