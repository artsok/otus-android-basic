package artsok.github.io.movie4k.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import artsok.github.io.movie4k.R
import kotlinx.android.synthetic.main.exit_dialog.*


class CustomDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exit_dialog)
        val okButton = buttonOk
        val noButton = buttonNo
        okButton.setOnClickListener {
            this@CustomDialog.dismiss()
            handleExit(context)
        }
        noButton.setOnClickListener { this@CustomDialog.cancel() }
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun handleExit(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}