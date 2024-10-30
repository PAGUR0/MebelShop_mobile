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

public val Trashcan: ImageVector
	get() {
		if (_Trashcan != null) {
			return _Trashcan!!
		}
		_Trashcan = ImageVector.Builder(
            name = "Trashcan",
            defaultWidth = 22.dp,
            defaultHeight = 24.dp,
            viewportWidth = 22f,
            viewportHeight = 24f
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
				moveTo(4.33325f, 24f)
				curveTo(3.59990f, 240f, 2.97210f, 23.73890f, 2.44990f, 23.21670f)
				curveTo(1.92770f, 22.69440f, 1.66660f, 22.06670f, 1.66660f, 21.33330f)
				verticalLineTo(4f)
				horizontalLineTo(0.333252f)
				verticalLineTo(1.33333f)
				horizontalLineTo(6.99992f)
				verticalLineTo(0f)
				horizontalLineTo(14.9999f)
				verticalLineTo(1.33333f)
				horizontalLineTo(21.6666f)
				verticalLineTo(4f)
				horizontalLineTo(20.3333f)
				verticalLineTo(21.3333f)
				curveTo(20.33330f, 22.06670f, 20.07210f, 22.69440f, 19.54990f, 23.21670f)
				curveTo(19.02770f, 23.73890f, 18.39990f, 240f, 17.66660f, 240f)
				horizontalLineTo(4.33325f)
				close()
				moveTo(17.6666f, 4f)
				horizontalLineTo(4.33325f)
				verticalLineTo(21.3333f)
				horizontalLineTo(17.6666f)
				verticalLineTo(4f)
				close()
				moveTo(6.99992f, 18.6667f)
				horizontalLineTo(9.66658f)
				verticalLineTo(6.66667f)
				horizontalLineTo(6.99992f)
				verticalLineTo(18.6667f)
				close()
				moveTo(12.3333f, 18.6667f)
				horizontalLineTo(14.9999f)
				verticalLineTo(6.66667f)
				horizontalLineTo(12.3333f)
				verticalLineTo(18.6667f)
				close()
			}
		}.build()
		return _Trashcan!!
	}

private var _Trashcan: ImageVector? = null
