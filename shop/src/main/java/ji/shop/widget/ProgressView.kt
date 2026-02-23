package ji.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.progressindicator.LinearProgressIndicator

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : LinearLayout(context, attrs, def) {

    private val progressBar: LinearProgressIndicator? = null
    private val tvPercent: TextView? = null

    init {

    }
}