package com.carles.base.poi.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.carles.base.R
import com.carles.base.core.ui.BaseActivity
import com.carles.base.core.ui.ERROR
import com.carles.base.core.ui.LOADING
import com.carles.base.core.ui.ResourceState
import com.carles.base.core.ui.SUCCESS
import com.carles.base.poi.domain.Poi
import kotlinx.android.synthetic.main.activity_poi_list.poilist_recyclerview
import kotlinx.android.synthetic.main.view_toolbar.toolbar
import org.koin.android.viewmodel.ext.android.viewModel

class PoiListActivity : BaseActivity() {

    override val layoutResourceId = R.layout.activity_poi_list
    private val viewModel by viewModel<PoiListViewModel>()
    private lateinit var adapter: PoiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observablePoiList.observe(this, Observer {
            it.let { handlePoiList(it.state, it.data, it.message) }
        })
    }

    override fun initViews() {
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.poilist_title)
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = PoiListAdapter { poi -> navigateToPoiDetail(poi.id) }
        poilist_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        poilist_recyclerview.adapter = adapter
    }

    private fun handlePoiList(state: ResourceState, poiList: List<Poi>?, errorMessage: String?) {
        when (state) {
            SUCCESS -> {
                hideProgress()
                adapter.setItems(poiList ?: emptyList())
            }
            ERROR -> {
                hideProgress()
                showError(errorMessage) { viewModel.retry() }
            }
            LOADING -> showProgress()
        }
    }

    private fun navigateToPoiDetail(id: String) = startActivity(PoiDetailActivity.newIntent(this, id))
}