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

public val SearchIcon: ImageVector
	get() {
		if (_Search != null) {
			return _Search!!
		}
		_Search = ImageVector.Builder(
            name = "Search",
            defaultWidth = 44.dp,
            defaultHeight = 44.dp,
            viewportWidth = 44f,
            viewportHeight = 44f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFFFFFFFF)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 5f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(41.5f, 41.5f)
				lineTo(32.075f, 32.075f)
				moveTo(37.1667f, 19.8333f)
				curveTo(37.16670f, 29.40630f, 29.40630f, 37.16670f, 19.83330f, 37.16670f)
				curveTo(10.26040f, 37.16670f, 2.50f, 29.40630f, 2.50f, 19.83330f)
				curveTo(2.50f, 10.26040f, 10.26040f, 2.50f, 19.83330f, 2.50f)
				curveTo(29.40630f, 2.50f, 37.16670f, 10.26040f, 37.16670f, 19.83330f)
				close()
			}
		}.build()
		return _Search!!
	}

private var _Search: ImageVector? = null
