package com.carles.base.ui.poidetail

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.carles.base.R
import com.carles.base.domain.PoiDetail
import com.carles.base.ui.*
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_poi_detail.*
import kotlinx.android.synthetic.main.view_progress.progress
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PoiDetailFragment : Fragment(R.layout.fragment_poi_detail) {

    private val viewModel by viewModel<PoiDetailViewModel> { parametersOf(requireArguments().getString(EXTRA_ID)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = poidetail_toolbar as MaterialToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        viewModel.observablePoiDetail.observe(viewLifecycleOwner, Observer {
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
        (activity as AppCompatActivity).supportActionBar?.setTitle(poi.title)
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

    private fun showError(errorMessage: String?) {
        safeNavigate(
            R.id.poiDetailFragment,
            R.id.action_poiDetailFragment_to_errorDialogFragment,
            ErrorDialogFragment.getBundle(errorMessage)
        )
    }

    companion object {
        private const val EXTRA_ID = "id"
        fun getBundle(id: String) = bundleOf(EXTRA_ID to id)
    }
}
