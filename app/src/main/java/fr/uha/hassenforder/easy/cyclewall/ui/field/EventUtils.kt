package fr.uha.hassenforder.easy.cyclewall.ui.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.ui.Converter
import java.time.LocalDateTime


@Composable
fun EventDecoratedTitle(
    cycle : Cycle,
    isCurrent : Boolean,
    color : Color = Color.White,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(cycle.title,
            color = color,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        if (isCurrent) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = Color.Magenta,
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        Text(
            stringArrayResource(R.array.rhythms)[cycle.rhythm.ordinal],
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun EventDayRange (
    begin : LocalDateTime,
    end : LocalDateTime,
    color : Color = Color.White,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            Converter.convert(begin),
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            "..",
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            Converter.convert(end),
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
