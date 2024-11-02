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

public val Arrow_right: ImageVector
	get() {
		if (_Arrow_right != null) {
			return _Arrow_right!!
		}
		_Arrow_right = ImageVector.Builder(
            name = "Arrow_right",
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
				moveTo(6.80008f, 8f)
				lineTo(0.666748f, 1.86667f)
				lineTo(2.53341f, 0f)
				lineTo(10.5334f, 8f)
				lineTo(2.53341f, 16f)
				lineTo(0.666748f, 14.1333f)
				lineTo(6.80008f, 8f)
				close()
			}
		}.build()
		return _Arrow_right!!
	}

private var _Arrow_right: ImageVector? = null
