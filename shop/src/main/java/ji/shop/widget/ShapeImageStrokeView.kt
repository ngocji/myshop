package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.imageview.ShapeableImageView
import ji.shop.R
import ji.shop.exts.load

class ShapeImageStrokeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
        setBackgroundResource(R.drawable.bg_secondary_rounded_with_stroke)
        val padding = context.resources.getDimensionPixelSize(R.dimen._6dp)
        setPadding(padding, padding, padding, padding)
        addView(ShapeableImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(context.resources.getDimension(R.dimen.default_radius))
                .build()
        }, LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }

    fun load(path: Any?) {
        (getChildAt(0) as? ShapeableImageView)?.load(path, error = R.drawable.ic_product)
    }
}