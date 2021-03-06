package com.carles.base

import com.carles.base.domain.Poi
import com.carles.base.domain.PoiDetail
import com.carles.base.data.remote.PoiDetailResponseDto
import com.carles.base.data.remote.PoiListResponseDto
import com.carles.base.data.remote.PoiResponseDto

private const val DEFAULT_ID = "1"

val poiList = listOf(createPoi(), createPoi("2"))
val anotherPoiList = listOf(createPoi("3"), createPoi("4"))
val poiDetail = createPoiDetail()
val anotherPoiDetail = createPoiDetail().copy(title = "another_title")

val emptyPoiListResponseDto = PoiListResponseDto(listOf())
val poiListResponseDto =
    PoiListResponseDto(listOf(createPoiResponseDto(), createPoiResponseDto("2")))
val poiResponseDto = createPoiResponseDto()
val poiDetailResponseDto = createPoiDetailResponseDto()

private fun createPoi(id: String = DEFAULT_ID) = Poi(id, "the title", "the geocoordinates")
private fun createPoiDetail(id: String = DEFAULT_ID) = PoiDetail(
    id, "the_title", transport = "the_transport", email = "the_email", phone =
    "the_phone"
)

private fun createPoiResponseDto(id: String = DEFAULT_ID) =
    PoiResponseDto(id, "the title", "the geocoordinates")

private fun createPoiDetailResponseDto(id: String = DEFAULT_ID) =
    PoiDetailResponseDto(
        id = DEFAULT_ID,
        title = "the_title",
        transport = "the_transport",
        email = "the_email",
        phone = "the_phone"
    )


