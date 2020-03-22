package com.carles.base.api

import com.carles.base.poi.data.PoiApi
import com.carles.base.poi.data.PoiDetailResponseDto
import com.carles.base.poi.data.PoiListResponseDto
import com.google.gson.Gson
import io.reactivex.Single

class TestApi : PoiApi {

    override fun getPoiList() = Single.just(Gson().fromJson(POI_LIST_RESPONSE, PoiListResponseDto::class.java))

    override fun getPoiDetail(id: String): Single<PoiDetailResponseDto> {
        val response = when (id) {
            "1" -> POI_DETAIL_1_RESPONSE
            "52" -> POI_DETAIL_52_RESPONSE
            else -> throw IllegalArgumentException()
        }
        return Single.just(Gson().fromJson(response, PoiDetailResponseDto::class.java))
    }
}