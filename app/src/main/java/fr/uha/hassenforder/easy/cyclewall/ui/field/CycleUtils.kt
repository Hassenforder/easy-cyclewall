package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.ui.Converter

@Composable
fun CycleTiming (
    cycle : Cycle,
    color : Color = Color.White,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringArrayResource(R.array.rhythms)[cycle.rhythm.ordinal],
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            ":",
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            Converter.convert(cycle.begin, cycle.rhythm),
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
        cycle.end?.let {
            Text(
                Converter.convert(it, cycle.rhythm),
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        cycle.duration?.let {
            Text(
                pluralStringResource(R.plurals.duration, it, it),
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun CycleOnScreen (
    cycle : Cycle,
    color : Color = Color.White,
) {
    Text(
        stringResource(R.string.on_screen, stringArrayResource(R.array.on_screen)[cycle.screen.ordinal]),
        modifier = Modifier.fillMaxWidth(),
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )
}
