package com.ob.rfi

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


class GlobalProgressDialogFragment() : DialogFragment() {
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        // Create a simple dialog with no title and no cancel option
        val dialog = Dialog(requireContext())
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)

        if (progressMessage.isNotEmpty())
            v.findViewById<TextView>(R.id.progressMessage).text = progressMessage

        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {

        var progressMessage: String = ""
        const val TAG: String = "GlobalProgressDialog"
        private var instance: GlobalProgressDialogFragment? = null

        fun getInstance(): GlobalProgressDialogFragment {
            if (instance == null) {
                instance = GlobalProgressDialogFragment()
            }
            return instance!!
        }
        // Show the dialog safely
        fun showDialog(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) == null) {
                getInstance().show(fragmentManager, TAG)
            }
        }

        // Hide the dialog safely
        fun hideDialog(fragmentManager: FragmentManager) {
            val dialog = fragmentManager.findFragmentByTag(TAG) as GlobalProgressDialogFragment
            dialog.dismissAllowingStateLoss()
        }
    }
}