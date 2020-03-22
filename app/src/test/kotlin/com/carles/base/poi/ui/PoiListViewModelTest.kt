package com.carles.base.poi.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.carles.base.core.ui.Resource
import com.carles.base.poi.domain.Poi
import com.carles.base.poi.domain.FetchPoiListUsecase
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PoiListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val observer: Observer<Resource<List<Poi>>> = mockk()
    val fetchPoiListUsecase: FetchPoiListUsecase = mockk(relaxed = true)
    lateinit var viewModel: PoiListViewModel

    @Before
    fun setup() {
        viewModel = PoiListViewModel(fetchPoiListUsecase)
        viewModel.observablePoiList.observeForever(observer)
    }

    @Test
    fun init_getPoiList() {
        verify { fetchPoiListUsecase.invoke() }
    }

    @Test
    fun retry_getPoiList() {
        clearMocks(fetchPoiListUsecase)
        viewModel.retry()
        verify { fetchPoiListUsecase.invoke() }
    }
}