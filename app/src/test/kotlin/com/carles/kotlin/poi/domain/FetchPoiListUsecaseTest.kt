package com.carles.kotlin.poi.domain

import com.carles.kotlin.core.domain.AppSchedulers
import com.carles.kotlin.poi.data.PoiRepository
import com.carles.kotlin.poi.domain.FetchPoiListUsecase
import com.carles.kotlin.poiList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

class FetchPoiListUsecaseTest {

    val repository : PoiRepository = mockk(relaxed = true)
    val scheduler = TestScheduler()
    val schedulers = AppSchedulers(scheduler, scheduler, scheduler)
    val usecase = FetchPoiListUsecase(repository, schedulers)

    @Test
    fun invoke_fetchRepository() {
        every { repository.getPoiList(any()) } returns Single.just(poiList)

        val result = usecase.invoke().test()
        scheduler.triggerActions()

        verify { repository.getPoiList(true) }
        result.assertValue(poiList)
    }
}