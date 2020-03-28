package com.carles.base.domain

import com.carles.base.AppSchedulers
import com.carles.base.data.PoiRepository
import com.carles.base.domain.usecase.FetchPoiListUsecase
import com.carles.base.poiList
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