package com.carles.kotlin.poi.ui

import com.carles.kotlin.poi.domain.GetPoiDetaiUsecase
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PoiDetailViewModelTest {

    val getPoiDetailUsecase : GetPoiDetaiUsecase = mockk(relaxed = true)

    @Before
    fun setup() {
        PoiDetailViewModel("1", getPoiDetailUsecase)
    }

    @Test
    fun init_getPoiDetail() {
        verify { getPoiDetailUsecase.invoke("1") }
    }
}