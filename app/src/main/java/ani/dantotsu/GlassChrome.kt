package ani.dantotsu

import android.view.View
import com.example.liquidglass.LiquidGlassView

/** Wire a glass host to the content it should sample. Press gel stays off so child controls keep the touch. */
fun LiquidGlassView.bindGlassChrome(backdrop: View?) {
    enableDynamicBackground = true
    enablePressEffect = false
    if (backdrop != null) {
        backdropSource = backdrop
    }
}
