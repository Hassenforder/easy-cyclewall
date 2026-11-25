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
public val Icons.Outlined.MobileLock: ImageVector
  get() {
    if (_mobile_lock != null) {
      return _mobile_lock!!
    }
    _mobile_lock =
      ImageVector.Builder(
          name = "mobile_lock",
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
            moveTo(7f, 23f)
            quadTo(6.18f, 23f, 5.59f, 22.41f)
            reflectiveQuadTo(5f, 21f)
            verticalLineTo(3f)
            quadTo(5f, 2.17f, 5.59f, 1.59f)
            reflectiveQuadTo(7f, 1f)
            horizontalLineTo(17f)
            quadToRelative(0.82f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(19f, 3f)
            verticalLineTo(6.1f)
            quadToRelative(0.45f, 0.18f, 0.73f, 0.55f)
            reflectiveQuadTo(20f, 7.5f)
            verticalLineToRelative(2f)
            quadToRelative(0f, 0.47f, -0.27f, 0.85f)
            reflectiveQuadTo(19f, 10.9f)
            verticalLineTo(21f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(17f, 23f)
            horizontalLineTo(7f)
            close()
            moveTo(7f, 21f)
            horizontalLineTo(17f)
            verticalLineTo(3f)
            horizontalLineTo(7f)
            verticalLineTo(21f)
            close()
            moveTo(8f, 15f)
            horizontalLineToRelative(8f)
            lineTo(13.38f, 11.5f)
            lineTo(11.5f, 14f)
            lineTo(10.13f, 12.18f)
            lineTo(8f, 15f)
            close()
            moveTo(15.71f, 9.71f)
            quadTo(16f, 9.42f, 16f, 9f)
            quadTo(16f, 8.57f, 15.71f, 8.29f)
            reflectiveQuadTo(15f, 8f)
            reflectiveQuadTo(14.29f, 8.29f)
            reflectiveQuadTo(14f, 9f)
            quadToRelative(0f, 0.42f, 0.29f, 0.71f)
            reflectiveQuadTo(15f, 10f)
            reflectiveQuadTo(15.71f, 9.71f)
            close()
            moveTo(7f, 21f)
            verticalLineTo(3f)
            verticalLineTo(21f)
            close()
          }
        }
        .build()
    return _mobile_lock!!
  }

private var _mobile_lock: ImageVector? = null
