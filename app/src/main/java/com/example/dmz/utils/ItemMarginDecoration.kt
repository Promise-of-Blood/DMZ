import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemMarginDecoration(
    context: Context,
    topMarginDp: Int,
    bottomMarginDp: Int,
    startMarginDp: Int,
    endMarginDp: Int
) : RecyclerView.ItemDecoration() {

    private val topMarginPx = dpToPx(context, topMarginDp)
    private val bottomMarginPx = dpToPx(context, bottomMarginDp)
    private val startMarginPx = dpToPx(context, startMarginDp)
    private val endMarginPx = dpToPx(context, endMarginDp)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = topMarginPx
        outRect.bottom = bottomMarginPx
        outRect.left = startMarginPx
        outRect.right = endMarginPx
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}