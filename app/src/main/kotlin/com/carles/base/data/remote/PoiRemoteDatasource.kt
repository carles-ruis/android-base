package com.carles.base.data.remote

import com.carles.base.data.remote.PoiApi
import com.carles.base.data.remote.toModel

class PoiRemoteDatasource(private val api: PoiApi) {

    fun getPoiList() = api.getPoiList().map { it.toModel() }

    fun getPoiDetail(itemId: String) = api.getPoiDetail(itemId).map { it.toModel() }
}