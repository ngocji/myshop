package ji.shop.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog(layoutRes: Int) : DialogFragment(layoutRes) {
    override fun onStart() {
        super.onStart()
        dialog?.window?.also { doOnWindow(it) }
    }

    open fun doOnWindow(window: Window) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setWindowAnimations(0)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, this::class.java.name)
    }
}