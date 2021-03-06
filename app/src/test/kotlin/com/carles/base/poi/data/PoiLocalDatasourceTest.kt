package com.carles.base.poi.data

import com.carles.base.data.local.Cache
import com.carles.base.data.local.CacheItems
import com.carles.base.data.local.CacheKey
import com.carles.base.data.local.ItemNotCachedException
import com.carles.base.data.local.PoiDao
import com.carles.base.data.local.PoiLocalDatasource
import com.carles.base.poiDetail
import com.carles.base.poiList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.Single
import org.junit.Test

class PoiLocalDatasourceTest {

    val cache: Cache = mockk(relaxed = true)
    val dao: PoiDao = mockk(relaxed = true)
    val datasource = PoiLocalDatasource(dao, cache)
    val poiListKey =
        CacheKey(CacheItems.POI_LIST)
    val poiDetailKey =
        CacheKey(CacheItems.POI_DETAIL, "1")

    @Test
    fun getPoiList_cacheHit() {
        every { cache.isCached(poiListKey) } returns true
        every { dao.loadPois() } returns Single.just(poiList)
        datasource.getPoiList().test().assertValue(poiList)
    }

    @Test
    fun getPoiList_cacheMiss() {
        every { cache.isCached(poiListKey) } returns false
        datasource.getPoiList().test().assertError(ItemNotCachedException)
    }

    @Test
    fun persist_poiList() {
        datasource.persist(poiList)
        verifyAll {
            dao.deletePois()
            dao.insertPois(poiList)
            cache.set(poiListKey)
        }
    }

    @Test
    fun getPoiDetail_cacheHit() {
        every { cache.isCached(poiDetailKey) } returns true
        every { dao.loadPoiById("1") } returns Single.just(poiDetail)
        datasource.getPoiDetail("1").test().assertValue(poiDetail)
    }

    @Test
    fun getPoiDetail_cacheMiss() {
        every { cache.isCached(poiDetailKey) } returns false
        datasource.getPoiDetail("1").test().assertError(ItemNotCachedException)
    }

    @Test
    fun persist_poiDetail() {
        datasource.persist(poiDetail)
        verifyAll {
            dao.insertPoi(poiDetail)
            cache.set(poiDetailKey)
        }
    }
}