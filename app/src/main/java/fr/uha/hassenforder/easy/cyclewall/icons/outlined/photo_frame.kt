package fr.uha.hassenforder.easy.cyclewall.icons.outlined

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val Icons.Outlined.PhotoFrame: ImageVector
  get() {
    if (_photo_frame != null) {
      return _photo_frame!!
    }
    _photo_frame =
      ImageVector.Builder(
          name = "photo_frame",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(6f, 21f)
            quadTo(5.58f, 21f, 5.29f, 20.71f)
            quadTo(5f, 20.43f, 5f, 20f)
            verticalLineTo(19f)
            horizontalLineTo(3f)
            quadTo(2.18f, 19f, 1.59f, 18.41f)
            reflectiveQuadTo(1f, 17f)
            verticalLineTo(6f)
            quadTo(1f, 5.18f, 1.59f, 4.59f)
            reflectiveQuadTo(3f, 4f)
            horizontalLineTo(21f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(23f, 5.18f, 23f, 6f)
            verticalLineTo(17f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(21f, 19f)
            horizontalLineTo(19f)
            verticalLineToRelative(1f)
            quadToRelative(0f, 0.43f, -0.29f, 0.71f)
            reflectiveQuadTo(18f, 21f)
            horizontalLineTo(6f)
            close()
            moveTo(3f, 17f)
            horizontalLineTo(21f)
            verticalLineTo(6f)
            horizontalLineTo(3f)
            verticalLineTo(17f)
            close()
            moveTo(5f, 15f)
            horizontalLineTo(19f)
            lineTo(14.5f, 9f)
            lineTo(11f, 13.5f)
            lineToRelative(-2.5f, -3f)
            lineTo(5f, 15f)
            close()
            moveTo(3f, 17f)
            verticalLineTo(6f)
            verticalLineTo(17f)
            close()
          }
        }
        .build()
    return _photo_frame!!
  }

private var _photo_frame: ImageVector? = null
