package ji.shop.insets

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.graphics.Insets
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import ji.shop.R
import kotlin.math.max

class InsetContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.insetContainerStyle,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var systemBarsInsets: Insets = Insets.NONE
    private var imeInsets: Insets = Insets.NONE
    private var displayCutoutInsets: Insets = Insets.NONE
    private val paint = Paint()

    private var forceStatusBar = false
    private var statusBarColor: Int = 0
    private var forceNavigationBar = false
    private var navigationBarColor: Int = 0

    private var enableLightStatusBar: Boolean = false
    private val transWithWhiteColor by lazy {
        "#00ffffff".toColorInt()
    }

    init {
        // Retrieve attributes from XML
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.InsetContainer,
                defStyleAttr,
                defStyleRes
            )
            forceStatusBar =
                typedArray.getBoolean(R.styleable.InsetContainer_ins_force_statusbar, false)
            statusBarColor = typedArray.getColor(
                R.styleable.InsetContainer_ins_statusbar_color,
                Color.WHITE
            )
            forceNavigationBar =
                typedArray.getBoolean(R.styleable.InsetContainer_ins_force_navigationbar, false)
            navigationBarColor = typedArray.getColor(
                R.styleable.InsetContainer_ins_navigationbar_color,
                Color.BLACK
            )
            enableLightStatusBar = typedArray.getBoolean(
                R.styleable.InsetContainer_ins_enable_light_status_bar,
                false
            )
            typedArray.recycle()
        }

        log("Init: initTransWhiteColor=$transWithWhiteColor, stsBar=$statusBarColor, naviBar=${navigationBarColor}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Apply initial status bar appearance based on attributes
        updateStatusBarAppearance()

        // Apply initial window colors for older Android versions
        applyInitialWindowColors()

        // Set up listener to observe inset changes (padding)
        setupWindowInsetsListener()
    }

    /**
     * Sets up a listener to receive insets and adjust the view's padding.
     */
    private fun setupWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            // Get insets for system bars, IME (keyboard), and display cutout
            systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            displayCutoutInsets = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

            // Set padding for the view. Only add padding if a color is specified for drawing.
            val bottomPadding =
                if (needToPaddingNavigationBarInset()) systemBarsInsets.bottom else 0

            // Adjust padding to account for both IME and navigation bar
            val finalTopPadding = if (needToPaddingStatusBarInset())
                max(systemBarsInsets.top, displayCutoutInsets.top) else 0

            val finalBottomPadding = max(bottomPadding, imeInsets.bottom)

            log("finalTopPadding: $finalTopPadding, finalBottomPadding: $finalBottomPadding, statusbarColor: $statusBarColor, navigationbarColor: ${navigationBarColor}, force: status ($forceStatusBar), nav($forceNavigationBar), imeInset: ${imeInsets.bottom}")

            v.setPadding(
                v.paddingLeft + displayCutoutInsets.left,
                finalTopPadding,
                v.paddingRight + displayCutoutInsets.right,
                finalBottomPadding
            )

            // Request the view to be redrawn to update the overlay colors
            invalidate()

            // Return the insets so child views can use them.
            insets
        }
    }

    /**
     * Sets the color of the status bar overlay at runtime.
     * @param color The new color for the status bar. Use 0 to disable.
     */
    fun setStatusBarColor(color: Int) {
        if (statusBarColor != color) {
            statusBarColor = color
            // For API 21+, we can set the status bar color directly on the Window.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                findActivity()?.window?.statusBarColor = color
            }

            // Update the layout and redraw the view.
            ViewCompat.requestApplyInsets(this)
            invalidate()
        }
    }

    /**
     * Sets the color of the navigation bar overlay at runtime.
     * @param color The new color for the navigation bar. Use 0 to disable.
     */
    fun setNavigationBarColor(color: Int) {
        if (navigationBarColor != color) {
            navigationBarColor = color
            // For API 21+, we can set the navigation bar color directly on the Window.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                findActivity()?.window?.navigationBarColor = color
            }

            // Update the layout and redraw the view.
            ViewCompat.requestApplyInsets(this)
            invalidate()
        }
    }

    /**
     * Sets whether to use light status bar appearance (dark icons).
     * @param enable True to use light appearance, false for dark.
     */
    fun setEnableLightStatusBar(enable: Boolean) {
        if (enableLightStatusBar != enable) {
            enableLightStatusBar = enable
            updateStatusBarAppearance()
        }
    }

    /**
     * This function is called to draw additional content on the view after child views are drawn.
     * We use it to draw the colored overlay bars on top of the content.
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawProtections(canvas)
    }

    /**
     * Draws the colored bars on the Canvas to cover the system bars.
     */
    private fun drawProtections(canvas: Canvas) {
        // Draw the status bar if a color is specified
        if (statusBarColor.isNotTransparentColor()) {
            log("Draw statusbar color: $statusBarColor")
            paint.color = statusBarColor
            canvas.drawRect(
                0f, 0f, width.toFloat(), max(
                    systemBarsInsets.top.toFloat(),
                    displayCutoutInsets.top.toFloat()
                ), paint
            )
        }

        // Draw the navigation bar if a color is specified
        if (navigationBarColor.isNotTransparentColor()) {
            log("Draw navigation color: $statusBarColor")
            paint.color = navigationBarColor
            canvas.drawRect(
                0f,
                (height - systemBarsInsets.bottom).toFloat(),
                width.toFloat(),
                height.toFloat(),
                paint
            )
        }
    }

    /**
     * Applies the initial status bar and navigation bar colors from XML attributes
     * for older Android versions where drawProtections is not effective.
     * This is only needed if the app is not in edge-to-edge mode.
     */
    private fun applyInitialWindowColors() {
        val window = findActivity()?.window ?: return
        if (statusBarColor != 0) {
            window.statusBarColor = statusBarColor
        }
        if (navigationBarColor != 0) {
            window.navigationBarColor = navigationBarColor
        }
    }

    /**
     * Updates the status bar icon and text color based on the `enableLightStatusBar` flag.
     * This is supported from Android M (API 23) onwards.
     */
    private fun updateStatusBarAppearance() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = findActivity()?.window ?: return
            val controller = WindowCompat.getInsetsController(window, this)
            controller.isAppearanceLightStatusBars = enableLightStatusBar
            log("Update light statusbar: $enableLightStatusBar")
        }
    }

    /**
     * Finds the Activity from the current Context.
     * @return The Activity or null if not found.
     */
    private fun findActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun needToPaddingStatusBarInset(): Boolean {
        return forceStatusBar || statusBarColor.isNotTransparentColor()
    }

    private fun needToPaddingNavigationBarInset(): Boolean {
        return forceNavigationBar || navigationBarColor.isNotTransparentColor()
    }

    private fun log(message: String) {
        Log.i("Insets", message)
    }

    private fun Int.isNotTransparentColor() =
        this != Color.TRANSPARENT && this != transWithWhiteColor
}