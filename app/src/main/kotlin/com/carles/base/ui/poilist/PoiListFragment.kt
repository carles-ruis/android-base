package com.carles.base.ui.poilist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.carles.base.R
import com.carles.base.domain.Poi
import com.carles.base.ui.*
import com.carles.base.ui.poidetail.PoiDetailFragment
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_poi_list.poilist_recyclerview
import kotlinx.android.synthetic.main.fragment_poi_list.poilist_toolbar
import kotlinx.android.synthetic.main.view_progress.progress
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PoiListFragment : Fragment(R.layout.fragment_poi_list) {

    private val viewModel by sharedViewModel<PoiListViewModel>()
    private lateinit var adapter: PoiListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = poilist_toolbar as MaterialToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.poilist_title)
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { activity?.finish() }

        adapter = PoiListAdapter { poi -> navigateToPoiDetail(poi.id) }
        poilist_recyclerview.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        poilist_recyclerview.adapter = adapter

        viewModel.observablePoiList.observe(viewLifecycleOwner, Observer {
            it.let { handlePoiList(it.state, it.data, it.message) }
        })
    }

    private fun handlePoiList(state: ResourceState, poiList: List<Poi>?, errorMessage: String?) {
        when (state) {
            SUCCESS -> {
                hideProgress()
                adapter.setItems(poiList ?: emptyList())
            }
            ERROR -> showError(errorMessage)
            LOADING -> showProgress()
        }
    }

    private fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress?.visibility = View.GONE
    }

    private fun showError(errorMessage:String?) {
        hideProgress()
        safeNavigate(
            R.id.poiListFragment,
            R.id.action_poiListFragment_to_errorDialogFragment,
            ErrorDialogFragment.getBundle(errorMessage, true)
        )
    }

    private fun navigateToPoiDetail(id: String) {
        findNavController().navigate(R.id.action_poiListFragment_to_poiDetailFragment, PoiDetailFragment.getBundle(id))
    }
}
