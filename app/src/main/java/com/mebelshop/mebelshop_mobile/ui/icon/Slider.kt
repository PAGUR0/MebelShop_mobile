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

public val Slider: ImageVector
	get() {
		if (_Slider != null) {
			return _Slider!!
		}
		_Slider = ImageVector.Builder(
            name = "Slider",
            defaultWidth = 352.dp,
            defaultHeight = 10.dp,
            viewportWidth = 352f,
            viewportHeight = 10f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFFFFFFFF)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(5f, 0f)
				horizontalLineTo(347f)
				arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 352f, 5f)
				verticalLineTo(5f)
				arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 347f, 10f)
				horizontalLineTo(5f)
				arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 5f)
				verticalLineTo(5f)
				arcTo(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 0f)
				close()
			}
		}.build()
		return _Slider!!
	}

private var _Slider: ImageVector? = null
