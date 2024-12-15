package com.ob.rfi.service

import android.app.ProgressDialog
import android.content.Context
import com.ob.rfi.R


object GlobalProgressDialog {
    private var progressDialog: ProgressDialog? = null

    fun showWithMessage(context: Context, message: String ="Loading, please wait...") {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context, R.style.MyProgressBarStyle).apply {
                setCancelable(false)
                setProgressStyle(ProgressDialog.STYLE_SPINNER)
                setCanceledOnTouchOutside(false)
                setMessage(message)
            }
        }

        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    fun dismiss() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialog = null
        }
    }

    fun show(context: Context) {
        showWithMessage(context)
        /*if (progressDialog == null) {
            progressDialog = ProgressDialog(context).apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setMessage("Loading, please wait...")
            }
        }

        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }*/
    }

}