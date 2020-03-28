package com.carles.base.data.remote

import com.carles.base.domain.Poi
import com.carles.base.domain.PoiDetail

data class PoiListResponseDto(var list: List<PoiResponseDto>? = null)

data class PoiResponseDto(var id: String, var title: String, var geocoordinates: String)

data class PoiDetailResponseDto(
    var id: String,
    var title: String? = null,
    var address: String? = null,
    var transport: String? = null,
    var description: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var geocoordinates: String? = null
)

fun PoiListResponseDto.toModel(): List<Poi> = list?.map { poiResponseDto -> poiResponseDto.toModel() } ?: emptyList()

fun PoiResponseDto.toModel() = Poi(id, title, geocoordinates)

fun PoiDetailResponseDto.toModel() =
    PoiDetail(
        id,
        title,
        address,
        sanitize(transport),
        description,
        sanitize(email),
        sanitize(phone),
        geocoordinates
    )

@Suppress("ComplexCondition")
private fun sanitize(source: String?): String? =
    if (source == null || source.isEmpty() || source == "null" || source == "undefined") null else source