package com.carles.base.ui.poidetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.carles.base.R
import com.carles.base.domain.PoiDetail
import com.carles.base.ui.ERROR
import com.carles.base.ui.LOADING
import com.carles.base.ui.ResourceState
import com.carles.base.ui.SUCCESS
import com.carles.base.ui.showError
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_address_textview
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_contentview
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_description_textview
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_mail_textview
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_phone_textview
import kotlinx.android.synthetic.main.activity_poi_detail.poidetail_transport_textview
import kotlinx.android.synthetic.main.view_progress.progress
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PoiDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<PoiDetailViewModel> { parametersOf(intent.getStringExtra(EXTRA_ID)) }
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_detail)
        initViews()
        initObservers()
    }

    private fun initViews() {
        toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewModel.observablePoiDetail.observe(this, Observer {
            it?.let {
                handlePoiDetail(it.state, it.data, it.message)
            }
        })
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

    private fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress?.visibility = View.GONE
    }

    companion object {
        private const val EXTRA_ID = "poi_detail_extra_id"
        fun newIntent(context: Context, id: String) =
            Intent(context, PoiDetailActivity::class.java).apply { putExtra(
                EXTRA_ID, id) }
    }
}