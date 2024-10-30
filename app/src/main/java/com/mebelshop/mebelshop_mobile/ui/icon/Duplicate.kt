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

public val Duplicate: ImageVector
	get() {
		if (_Icon != null) {
			return _Icon!!
		}
		_Icon = ImageVector.Builder(
            name = "Icon",
            defaultWidth = 30.dp,
            defaultHeight = 30.dp,
            viewportWidth = 30f,
            viewportHeight = 30f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFFFFFFFF)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 3f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(5.66675f, 19f)
				horizontalLineTo(4.33341f)
				curveTo(3.62620f, 190f, 2.94790f, 18.7190f, 2.44780f, 18.21890f)
				curveTo(1.94770f, 17.71880f, 1.66670f, 17.04060f, 1.66670f, 16.33330f)
				verticalLineTo(4.33332f)
				curveTo(1.66670f, 3.62610f, 1.94770f, 2.94780f, 2.44780f, 2.44770f)
				curveTo(2.94790f, 1.94760f, 3.62620f, 1.66670f, 4.33340f, 1.66670f)
				horizontalLineTo(16.3334f)
				curveTo(17.04070f, 1.66670f, 17.71890f, 1.94760f, 18.2190f, 2.44770f)
				curveTo(18.71910f, 2.94780f, 19.00010f, 3.62610f, 19.00010f, 4.33330f)
				verticalLineTo(5.66666f)
				moveTo(13.6667f, 11f)
				horizontalLineTo(25.6667f)
				curveTo(27.13950f, 110f, 28.33340f, 12.19390f, 28.33340f, 13.66670f)
				verticalLineTo(25.6667f)
				curveTo(28.33340f, 27.13940f, 27.13950f, 28.33330f, 25.66670f, 28.33330f)
				horizontalLineTo(13.6667f)
				curveTo(12.1940f, 28.33330f, 11.00010f, 27.13940f, 11.00010f, 25.66670f)
				verticalLineTo(13.6667f)
				curveTo(11.00010f, 12.19390f, 12.1940f, 110f, 13.66670f, 110f)
				close()
			}
		}.build()
		return _Icon!!
	}

private var _Icon: ImageVector? = null
