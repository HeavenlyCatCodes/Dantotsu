package ani.dantotsu

import android.view.View
import com.example.liquidglass.GlassAccessibilityMode
import com.example.liquidglass.LiquidGlassView

/**
 * Chrome-only liquid glass. Host opacity stays 1.
 * Press gel stays off so child controls keep the touch.
 * AUTO falls back to an opaque tint when Reduce Transparency / high contrast is on.
 */
fun LiquidGlassView.bindGlassChrome(backdrop: View?) {
    enableDynamicBackground = true
    enablePressEffect = false
    accessibilityMode = GlassAccessibilityMode.AUTO
    if (backdrop != null) {
        backdropSource = backdrop
    }
}
