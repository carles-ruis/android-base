package com.carles.kotlin

import androidx.room.Room
import com.carles.kotlin.core.domain.AppSchedulers
import com.carles.kotlin.core.data.Cache
import com.carles.kotlin.poi.data.*
import com.carles.kotlin.poi.domain.FetchPoiListUsecase
import com.carles.kotlin.poi.domain.GetPoiDetaiUsecase
import com.carles.kotlin.poi.ui.PoiDetailViewModel
import com.carles.kotlin.poi.ui.PoiListViewModel
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://t21services.herokuapp.com"

val coreModule = module {
    single { Cache() }
    single {
        AppSchedulers(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Schedulers.newThread()
        )
    }

    single {
        Room.databaseBuilder(androidContext(), PoiDatabase::class.java, "kotlin_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build())
            .baseUrl(BASE_URL)
            .build()
    }
}

val poiModule = module {
    single { (get() as PoiDatabase).poiDao() }
    single { (get() as Retrofit).create(PoiApi::class.java) }
    single { PoiLocalDatasource(dao = get(), cache = get()) }
    single { PoiRemoteDatasource(api = get()) }
    single { PoiRepository(localDatasource = get(), remoteDatasource = get()) }

    factory { FetchPoiListUsecase(repository = get(), schedulers = get()) }
    factory { GetPoiDetaiUsecase(repository = get(), schedulers = get()) }

    viewModel { PoiListViewModel(fetchPoiListUsecase = get()) }
    viewModel { (id: String) -> PoiDetailViewModel(id = id, getPoiDetailUsecase = get()) }
}
