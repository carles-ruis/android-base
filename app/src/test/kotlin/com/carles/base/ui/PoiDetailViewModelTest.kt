package com.carles.base.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carles.base.domain.usecase.GetPoiDetaiUsecase
import com.carles.base.poiDetail
import com.carles.base.ui.poidetail.PoiDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

class PoiDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val getPoiDetailUsecase : GetPoiDetaiUsecase = mockk(relaxed = true)
    lateinit var viewModel: PoiDetailViewModel

    @Test
    fun init_getPoiDetail_success() {
        every { getPoiDetailUsecase.invoke(any()) } returns Single.just(poiDetail)

        viewModel = PoiDetailViewModel("1", getPoiDetailUsecase)

        Assertions.assertThat(viewModel.observablePoiDetail.value!!.state).isEqualTo(SUCCESS)
        Assertions.assertThat(viewModel.observablePoiDetail.value!!.data).isEqualTo(poiDetail)
    }

    @Test
    fun init_getPoiDetail_error() {
        val error = Throwable("some error")
        every { getPoiDetailUsecase.invoke(any()) } returns Single.error(error)

        viewModel = PoiDetailViewModel("1", getPoiDetailUsecase)

        Assertions.assertThat(viewModel.observablePoiDetail.value!!.state).isEqualTo(ERROR)
        Assertions.assertThat(viewModel.observablePoiDetail.value!!.message).isEqualTo("some error")
    }
}