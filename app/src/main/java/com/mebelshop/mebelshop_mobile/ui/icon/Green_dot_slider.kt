/*
* Converted using https://composables.com/svgtocompose
*/

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Green_dot_slider: ImageVector
	get() {
		if (_Green_dot_slider != null) {
			return _Green_dot_slider!!
		}
		_Green_dot_slider = ImageVector.Builder(
            name = "Green_dot_slider",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFF006B5F)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(12f, 0f)
				horizontalLineTo(12f)
				arcTo(12f, 12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 24f, 12f)
				verticalLineTo(12f)
				arcTo(12f, 12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 24f)
				horizontalLineTo(12f)
				arcTo(12f, 12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 12f)
				verticalLineTo(12f)
				arcTo(12f, 12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 0f)
				close()
			}
		}.build()
		return _Green_dot_slider!!
	}

private var _Green_dot_slider: ImageVector? = null
