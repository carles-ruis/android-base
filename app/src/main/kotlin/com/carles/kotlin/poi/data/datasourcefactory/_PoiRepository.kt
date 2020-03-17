package com.carles.kotlin.poi.data.datasourcefactory

import com.carles.kotlin.core.data.CacheItems
import com.carles.kotlin.core.data.CacheKey
import com.carles.kotlin.poi.data.PoiRepo
import com.carles.kotlin.poi.domain.Poi
import com.carles.kotlin.poi.domain.PoiDetail
import io.reactivex.Single

class _PoiRepository(private val datasourceFactory: _PoiDatasourceFactory) : PoiRepo {

    override fun getPoiList(refresh: Boolean): Single<List<Poi>> =
        datasourceFactory
            .retrieveDatasource(refresh, CacheKey(CacheItems.POI_LIST))
            .getPoiList()
            //     .onErrorResumeNext(datasourceFactory.retrieveRemoteDatasource().getPoiList())
            .flatMap { datasourceFactory.retrieveLocalDatasource().persist(it) }


    override fun getPoiDetail(itemId: String, refresh: Boolean): Single<PoiDetail> =
        datasourceFactory
            .retrieveDatasource(refresh, CacheKey(CacheItems.POI_DETAIL, itemId))
            .getPoiDetail(itemId)
//            .onErrorResumeNext(datasourceFactory.retrieveLocalDatasource().getPoiDetail(itemId))
            .flatMap { datasourceFactory.retrieveLocalDatasource().persist(it) }

}