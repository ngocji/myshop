package ji.shop.dialog

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import ji.shop.R
import ji.shop.base.BaseDialog
import ji.shop.base.viewBinding
import ji.shop.data.domain.CreditInfo
import ji.shop.databinding.DialogEditManualCardBinding
import ji.shop.exts.height
import ji.shop.exts.isTablet
import ji.shop.exts.width
import java.io.File
import kotlin.math.roundToInt
import androidx.core.net.toUri

class EditManualCardDialog : BaseDialog(R.layout.dialog_edit_manual_card) {
    private val binding by viewBinding(DialogEditManualCardBinding::bind)
    private var creditInfo: CreditInfo? = null
    private var action: ((CreditInfo) -> Unit)? = null
    private var imageUri: Uri? = null
    private var imageFile: File? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (!success) {
                imageFile?.takeIf { it.exists() }?.delete()
                imageUri = null
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    override fun doOnWindow(window: Window) {
        super.doOnWindow(window)
        val isTablet = context.isTablet()
        window.setLayout(
            requireActivity().width().let {
                if (isTablet) (it * 0.4).roundToInt() else it
            },
            if (isTablet) requireActivity().height() else WRAP_CONTENT
        )
        window.setGravity(if (isTablet) Gravity.END else Gravity.BOTTOM)
    }

    private fun initViews() {
        with(binding) {
            btnClose.setOnClickListener { dismissAllowingStateLoss() }
            btnPlayNow.setOnClickListener {
                action?.invoke(obtainCreditInfo())
                dismissAllowingStateLoss()
            }
            btnImage.setOnClickListener { pickCamera() }
        }
    }

    private fun initData() {
        creditInfo?.let { (cardNumber, nameOnCard, date, cvv, imageUri) ->
            with(binding) {
                edtCardNumber.setText(cardNumber)
                edtNameOnCard.setText(nameOnCard)
                edtExpiryDate.setText(date)
                edtCvv.setText(cvv)
                imageUri?.let {
                    this@EditManualCardDialog.imageUri = it.toUri()
                }
            }
        }
    }

    private fun obtainCreditInfo(): CreditInfo {
        return CreditInfo(
            cardNumber = binding.edtCardNumber.getText(),
            nameOnCard = binding.edtNameOnCard.getText(),
            date = binding.edtExpiryDate.getText(),
            cvv = binding.edtCvv.getText(),
            imageUri = imageUri?.toString()
        )
    }

    private fun pickCamera() {
        val file = File.createTempFile(
            "camera_",
            ".jpg",
            requireContext().cacheDir
        )

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
        imageFile = file

        takePictureLauncher.launch(imageUri)
    }

    companion object {
        fun newInstance(
            creditInfo: CreditInfo?,
            action: (CreditInfo) -> Unit
        ): EditManualCardDialog {
            return EditManualCardDialog().apply {
                this.creditInfo = creditInfo
                this.action = action
            }
        }
    }
}