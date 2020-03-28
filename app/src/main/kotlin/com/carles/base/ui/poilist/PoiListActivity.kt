package com.carles.base.ui.poilist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.carles.base.R
import com.carles.base.domain.Poi
import com.carles.base.ui.ERROR
import com.carles.base.ui.LOADING
import com.carles.base.ui.ResourceState
import com.carles.base.ui.SUCCESS
import com.carles.base.ui.poidetail.PoiDetailActivity
import com.carles.base.ui.showError
import kotlinx.android.synthetic.main.activity_poi_list.poilist_recyclerview
import kotlinx.android.synthetic.main.view_progress.progress
import kotlinx.android.synthetic.main.view_toolbar.toolbar
import org.koin.android.viewmodel.ext.android.viewModel

class PoiListActivity : AppCompatActivity() {

    private val viewModel by viewModel<PoiListViewModel>()
    private lateinit var adapter: PoiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_from_right_to_left, R.anim.slide_out_from_right_to_left)
        setContentView(R.layout.activity_poi_list)
        initViews()
        initObservers()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_left_to_right, R.anim.slide_out_from_left_to_right)
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.poilist_title)
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = PoiListAdapter { poi -> navigateToPoiDetail(poi.id) }
        poilist_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        poilist_recyclerview.adapter = adapter
    }

    private fun initObservers() {
        viewModel.observablePoiList.observe(this, Observer {
            it.let { handlePoiList(it.state, it.data, it.message) }
        })
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

    private fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress?.visibility = View.GONE
    }

    private fun navigateToPoiDetail(id: String) = startActivity(PoiDetailActivity.newIntent(this, id))
}