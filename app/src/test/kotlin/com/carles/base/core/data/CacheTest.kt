package com.carles.base.core.data

import com.carles.base.data.local.Cache
import com.carles.base.data.local.CacheItems
import com.carles.base.data.local.CacheKey
import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.Test
import java.util.*

class CacheTest {

    val cache = Cache()
    val calendar : Calendar = mockk()
    val cacheKey = CacheKey(CacheItems.POI_LIST)

    @Test
    fun isCached_itemNotCached() {
        Assertions.assertThat(cache.isCached(cacheKey)).isFalse()
    }

    @Test
    fun isCached_itemExpired() {
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        every { calendar.timeInMillis } returns 0L
        cache.set(cacheKey)

        clearStaticMockk(Calendar::class)
        Assertions.assertThat(cache.isCached(cacheKey)).isFalse()
    }

    @Test
    fun isCached_itemExpired_Spy() {
        val spy : Cache = spyk()
        every { spy.now() } returns 0L
        spy.set(cacheKey)

        clearAllMocks()
        Assertions.assertThat(spy.isCached(cacheKey)).isFalse()
    }

    @Test
    fun isCached_success() {
        cache.set(cacheKey)
        Assertions.assertThat(cache.isCached(cacheKey)).isTrue()
    }
}