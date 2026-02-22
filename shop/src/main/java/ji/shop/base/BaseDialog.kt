package ji.shop.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog(layoutRes: Int) : DialogFragment(layoutRes) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setWindowAnimations(0)
        }
    }
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, this::class.java.name)
    }
}