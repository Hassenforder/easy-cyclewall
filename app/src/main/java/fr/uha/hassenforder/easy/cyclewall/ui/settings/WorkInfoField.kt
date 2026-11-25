package fr.uha.hassenforder.easy.cyclewall.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.work.WorkInfo
import fr.uha.hassenforder.easy.cyclewall.ui.Converter
import fr.uha.hassenforder.easy.cyclewall.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun WorkInfosField(infos : List<WorkInfo>) {
    LazyColumn (
        modifier = Modifier.fillMaxWidth()
    ) {
        if (infos.isEmpty()) item {
            Text(stringResource(R.string.workinfo_no_value))
        }
        items(infos, key = { it.id }) {
            WorkInfoField(it)
        }
    }
}

@Composable
private fun WorkInfoField(info : WorkInfo) {
    Row (
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringArrayResource(R.array.workinfo_stateValues)[info.state.ordinal])
        if (info.nextScheduleTimeMillis != Long.MAX_VALUE) {
            Spacer(Modifier.weight(1f))
            val day = LocalDateTime.ofInstant(Instant.ofEpochMilli(info.nextScheduleTimeMillis), ZoneId.systemDefault())
            Text(Converter.convert(day))
        }
    }
}
