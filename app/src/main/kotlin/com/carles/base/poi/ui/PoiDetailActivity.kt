package com.carles.base.poi.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import com.carles.base.R
import com.carles.base.core.ui.BaseActivity
import com.carles.base.core.ui.initDefaultToolbar
import com.carles.base.core.ui.ERROR
import com.carles.base.core.ui.LOADING
import com.carles.base.core.ui.ResourceState
import com.carles.base.core.ui.SUCCESS
import com.carles.base.poi.domain.PoiDetail
import kotlinx.android.synthetic.main.activity_poi_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PoiDetailActivity : BaseActivity() {

    override val layoutResourceId = R.layout.activity_poi_detail
    private val viewModel by viewModel<PoiDetailViewModel> { parametersOf(intent.getStringExtra(EXTRA_ID)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observablePoiDetail.observe(this, Observer {
            it?.let {
                handlePoiDetail(it.state, it.data, it.message)
            }
        })
    }

    override fun initViews() {
        initDefaultToolbar()
    }

    private fun handlePoiDetail(state: ResourceState, poiDetail: PoiDetail?, errorMessage: String?) {
        when (state) {
            SUCCESS -> {
                hideProgress()
                if (poiDetail != null) displayPoiDetail(poiDetail)
            }
            ERROR -> {
                hideProgress()
                showError(errorMessage)
            }
            LOADING -> showProgress()
        }
    }

    private fun displayPoiDetail(poi: PoiDetail) {
        supportActionBar!!.setTitle(poi.title)
        poidetail_contentview.visibility = VISIBLE
        poidetail_address_textview.text = poi.address
        poidetail_description_textview.text = poi.description

        poidetail_transport_textview.text = poi.transport ?: ""
        poidetail_transport_textview.visibility = if (poi.transport == null) GONE else VISIBLE
        poidetail_mail_textview.text = poi.email ?: ""
        poidetail_mail_textview.visibility = if (poi.email == null) GONE else VISIBLE
        poidetail_phone_textview.text = poi.phone ?: ""
        poidetail_phone_textview.visibility = if (poi.phone == null) GONE else VISIBLE
    }

    companion object {
        private const val EXTRA_ID = "poi_detail_extra_id"
        fun newIntent(context: Context, id: String) =
            Intent(context, PoiDetailActivity::class.java).apply { putExtra(
                EXTRA_ID, id) }
    }
}