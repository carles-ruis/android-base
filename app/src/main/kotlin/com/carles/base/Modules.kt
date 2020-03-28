package com.carles.base

import androidx.room.Room
import com.carles.base.data.local.Cache
import com.carles.base.data.remote.PoiApi
import com.carles.base.data.local.PoiDatabase
import com.carles.base.data.local.PoiLocalDatasource
import com.carles.base.data.remote.PoiRemoteDatasource
import com.carles.base.data.PoiRepo
import com.carles.base.data.PoiRepository
import com.carles.base.domain.usecase.FetchPoiListUsecase
import com.carles.base.domain.usecase.GetPoiDetaiUsecase
import com.carles.base.ui.poidetail.PoiDetailViewModel
import com.carles.base.ui.poilist.PoiListViewModel
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

val appModule = module {
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
            .baseUrl(BASE_URL)
            .build()
    }

    single { (get() as PoiDatabase).poiDao() }
    single { (get() as Retrofit).create(PoiApi::class.java) }
    single { PoiLocalDatasource(dao = get(), cache = get()) }
    single { PoiRemoteDatasource(api = get()) }
    single() { PoiRepository(
        localDatasource = get(),
        remoteDatasource = get()
    ) as PoiRepo
    }

    factory { FetchPoiListUsecase(repository = get(), schedulers = get()) }
    factory { GetPoiDetaiUsecase(repository = get(), schedulers = get()) }

    viewModel { PoiListViewModel(fetchPoiListUsecase = get()) }
    viewModel { (id: String) ->
        PoiDetailViewModel(
            id = id,
            getPoiDetailUsecase = get()
        )
    }
}
