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

public val Plane_on: ImageVector
	get() {
		if (_Plane_on != null) {
			return _Plane_on!!
		}
		_Plane_on = ImageVector.Builder(
            name = "Plane_on",
            defaultWidth = 32.dp,
            defaultHeight = 26.dp,
            viewportWidth = 32f,
            viewportHeight = 26f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF006B5F)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 3f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(1.33325f, 13f)
				curveTo(1.33330f, 130f, 6.66660f, 2.33330f, 15.99990f, 2.33330f)
				curveTo(25.33330f, 2.33330f, 30.66660f, 130f, 30.66660f, 130f)
				curveTo(30.66660f, 130f, 25.33330f, 23.66660f, 15.99990f, 23.66660f)
				curveTo(6.66660f, 23.66660f, 1.33330f, 130f, 1.33330f, 130f)
				close()
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF006B5F)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 3f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(15.9999f, 17f)
				curveTo(18.20910f, 170f, 19.99990f, 15.20910f, 19.99990f, 130f)
				curveTo(19.99990f, 10.79080f, 18.20910f, 90f, 15.99990f, 90f)
				curveTo(13.79080f, 90f, 11.99990f, 10.79080f, 11.99990f, 130f)
				curveTo(11.99990f, 15.20910f, 13.79080f, 170f, 15.99990f, 170f)
				close()
			}
		}.build()
		return _Plane_on!!
	}

private var _Plane_on: ImageVector? = null
