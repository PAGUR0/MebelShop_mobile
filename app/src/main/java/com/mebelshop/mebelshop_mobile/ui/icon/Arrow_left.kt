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

public val Arrow_left: ImageVector
	get() {
		if (_Arrow_left != null) {
			return _Arrow_left!!
		}
		_Arrow_left = ImageVector.Builder(
            name = "Arrow_left",
            defaultWidth = 11.dp,
            defaultHeight = 16.dp,
            viewportWidth = 11f,
            viewportHeight = 16f
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
				moveTo(8.66675f, 16f)
				lineTo(0.666748f, 8f)
				lineTo(8.66675f, 0f)
				lineTo(10.5334f, 1.86667f)
				lineTo(4.40008f, 8f)
				lineTo(10.5334f, 14.1333f)
				lineTo(8.66675f, 16f)
				close()
			}
		}.build()
		return _Arrow_left!!
	}

private var _Arrow_left: ImageVector? = null
