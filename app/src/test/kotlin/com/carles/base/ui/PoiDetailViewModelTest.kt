package com.carles.base.ui

import com.carles.base.domain.usecase.GetPoiDetaiUsecase
import com.carles.base.ui.poidetail.PoiDetailViewModel
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