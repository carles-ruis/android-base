package com.carles.base.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import com.carles.base.R
import com.carles.base.ui.poilist.PoiListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ErrorDialogFragment : AppCompatDialogFragment() {

    private val viewModel by sharedViewModel<PoiListViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val errorMessage = arguments.getString(EXTRA_MESSAGE, getString(R.string.error_server_response))

        val alertDialogBuilder = MaterialAlertDialogBuilder(context).setMessage(errorMessage)
        if (arguments.getBoolean(EXTRA_RETRY)) {
            setCancelable(false)
            alertDialogBuilder.setCancelable(false).setPositiveButton(R.string.error_retry) { _, _ ->
                dismiss()
                viewModel.retry()
            }
        }
        return alertDialogBuilder.create()
    }

    companion object {
        private const val EXTRA_RETRY = "retry"
        private const val EXTRA_MESSAGE = "message"
        fun getBundle(message: String?, retry: Boolean = false) = bundleOf(EXTRA_RETRY to retry, EXTRA_MESSAGE to message)
    }
}
