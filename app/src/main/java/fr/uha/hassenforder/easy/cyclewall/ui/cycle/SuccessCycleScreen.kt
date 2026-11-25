package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import fr.uha.hassenforder.easy.android.ui.DecoratorProperties
import fr.uha.hassenforder.easy.android.ui.app.AppTopBar
import fr.uha.hassenforder.easy.android.ui.dialog.OutlinedDialog
import fr.uha.hassenforder.easy.android.ui.field.group.Orientation
import fr.uha.hassenforder.easy.android.ui.field.group.OutlinedEnumRadioGroup
import fr.uha.hassenforder.easy.android.ui.field.group.RadioProperties
import fr.uha.hassenforder.easy.android.ui.field.picture.DecoratedButton
import fr.uha.hassenforder.easy.android.ui.field.picture.Eraser
import fr.uha.hassenforder.easy.android.ui.field.picture.ExternalCameraApp
import fr.uha.hassenforder.easy.android.ui.field.picture.ExternalCameraAppProperties
import fr.uha.hassenforder.easy.android.ui.field.picture.GalleryPicker
import fr.uha.hassenforder.easy.android.ui.field.picture.GalleryPickerProperties
import fr.uha.hassenforder.easy.android.ui.field.picture.OutlinedPictureField
import fr.uha.hassenforder.easy.android.ui.field.picture.PictureFieldActionsScope
import fr.uha.hassenforder.easy.android.ui.field.spinner.OutlinedEnumSpinnerField
import fr.uha.hassenforder.easy.android.ui.field.spinner.OutlinedSpinnerField
import fr.uha.hassenforder.easy.android.ui.field.spinner.SpinnerProperties
import fr.uha.hassenforder.easy.android.ui.field.text.OutlinedIntField
import fr.uha.hassenforder.easy.android.ui.field.text.OutlinedTextField
import fr.uha.hassenforder.easy.android.ui.field.text.TextProperties
import fr.uha.hassenforder.easy.cyclewall.provider.AppFileProvider
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import fr.uha.hassenforder.easy.cyclewall.ui.Converter
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneDayLess
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneDayMore
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneWeekLess
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneWeekMore
import fr.uha.hassenforder.easy.android.ui.field.date.DateTimeFieldProperties
import fr.uha.hassenforder.easy.android.ui.field.date.OutlinedLocalDateTimeField
import java.lang.reflect.Field
import kotlin.reflect.KClass

@Composable
fun SuccessCycleScreen(
    uiState: CycleViewModel.UIState,
    send : (CycleViewModel.UIEvent) -> Unit,
) {
    Scaffold(
        topBar = { AppTopBar(uiState.uiTitle, { send(CycleViewModel.UIEvent.OnBack) }) },
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.cycle.title,
                onValueChange = { send(CycleViewModel.UIEvent.TitleChanged(it)) },
                properties = TextProperties(),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.title,
                    errorId = uiState.validation.title,
                ),
            )
            OutlinedEnumRadioGroup(
                value = uiState.cycle.screen,
                onValueChange = { send(CycleViewModel.UIEvent.ScreenChanged(CycleScreen.valueOf(it))) },
                properties = RadioProperties(
                    itemValues = CycleScreen.entries.toTypedArray(),
                    orientation = Orientation.HorizontalList,
                    itemLabels = stringArrayResource(R.array.on_screen)
                ),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.screen,
                    errorId = uiState.validation.screen,
                ),
            )
            OutlinedEnumSpinnerField(
                value = uiState.cycle.rhythm,
                onValueChange = { send(CycleViewModel.UIEvent.RhythmChanged(it)) },
                properties = SpinnerProperties(
                    converter = { CycleRhythm.valueOf(it) },
                    optionValues = CycleRhythm.entries.toTypedArray(),
                ),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.rhythm,
                    errorId = uiState.validation.rhythm,
                ),
            )
            if (uiState.cycle.rhythm != CycleRhythm.NO) {
                OutlinedLocalDateTimeField(
                    value = uiState.cycle.begin,
                    onValueChange = { it?.let { send(CycleViewModel.UIEvent.BeginChanged(it)) } },
                    properties = DateTimeFieldProperties(
                        converter = { Converter.convert(it, uiState.cycle.rhythm) },
                        isNullable = false,
                        dateOnly = uiState.isDayWithTime
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorator = DecoratorProperties(
                        labelId = if (uiState.cycle.rhythm == CycleRhythm.ONE_DAY) R.string.day else R.string.begin,
                        errorId = uiState.validation.begin,
                    ),
                    leading = {
                        ActionOneWeekLess()
                        ActionOneDayLess()
                    },
                    trailing = {
                        ActionOneDayMore()
                        ActionOneWeekMore()
                    }
                )
            }
            if (uiState.cycle.rhythm != CycleRhythm.NO && uiState.cycle.rhythm != CycleRhythm.ONE_DAY) {
                OutlinedEnumRadioGroup(
                    value = uiState.ending,
                    onValueChange = {
                        send(
                            CycleViewModel.UIEvent.EndingChanged(
                                CycleViewModel.Ending.valueOf(
                                    it
                                )
                            )
                        )
                    },
                    properties = RadioProperties(
                        itemValues = CycleViewModel.Ending.entries.toTypedArray(),
                        orientation = Orientation.HorizontalList,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorator = DecoratorProperties(
                        labelId = R.string.hint_ending,
                        errorId = uiState.validation.ending,
                    ),
                )
                when (uiState.ending) {
                    CycleViewModel.Ending.NO_IDEA -> {}
                    CycleViewModel.Ending.BY_END ->
                        OutlinedLocalDateTimeField(
                            value = uiState.cycle.end,
                            onValueChange = { it?.let { send(CycleViewModel.UIEvent.EndChanged(it)) } },
                            properties = DateTimeFieldProperties(
                                converter = { Converter.convert(it, uiState.cycle.rhythm) },
                                isNullable = false,
                                dateOnly = uiState.isDayWithTime
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            decorator = DecoratorProperties(
                                labelId = R.string.end,
                                errorId = uiState.validation.end,
                            ),
                            leading = {
                                ActionOneWeekLess()
                                ActionOneDayLess()
                            },
                            trailing = {
                                ActionOneDayMore()
                                ActionOneWeekMore()
                            }
                        )

                    CycleViewModel.Ending.BY_DURATION ->
                        OutlinedIntField(
                            value = uiState.cycle.duration,
                            onValueChange = { send(CycleViewModel.UIEvent.DurationChanged(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            decorator = DecoratorProperties(
                                labelId = R.string.duration,
                                errorId = uiState.validation.duration,
                            ),
                        )
                }
            }
            OutlinedPictureField(
                value = uiState.cycle.wallpaper,
                onValueChange = { send(CycleViewModel.UIEvent.WallpaperChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.picture,
                    errorId = uiState.validation.wallpaper,
                ),
                top = @Composable {
                    Eraser()
                },
                middle = @Composable {
                    GalleryPicker(
                        properties = GalleryPickerProperties(
                            galleryFilter = "image/*",
                        ),
                    )
                    val context = LocalContext.current
                    AdvancedResourcePicker(
                        properties = AdvancedResourcePickerProperties(
                            uriProvider = { id ->
                                AppFileProvider.getResourceDocumentUri(context, id)
                            },
                            from = arrayOf(R.mipmap::class),
                            preFilter = { it.startsWith("") },
                            inFilter = listOf<Pair<String, (String) -> Boolean>> (
                                "all" to { true },
                                "drawing" to { it.startsWith("d_") },
                                "picture" to { it.startsWith("p_") },
                            ),
                            preferredCount = 2,
                            titleId = R.string.resource_picker,
                            badgeId = R.string.picker_long_resource,
                        ),
                    )
                },
                bottom = @Composable {
                    val context = LocalContext.current
                    ExternalCameraApp(
                        properties = ExternalCameraAppProperties(
                            uriProvider = { AppFileProvider.createExternalImageUri(context) },
                        ),
                    )
                }
            )
        }
    }
}

@Suppress("ArrayInDataClass")
@Immutable
data class AdvancedResourcePickerProperties (
    val uriProvider : ((Int) -> Uri),
    val from : Array<KClass<*>>,
    val preFilter : (String) -> Boolean = { true },
    val inFilter : List<Pair<String, (String) -> Boolean>> = emptyList(),
    @param:StringRes val titleId : Int,// = R.string.title_resource_picker,
    val preferredCount : Int = 4,
    val icon : ImageVector = Icons.Outlined.ImageSearch,
    @param:StringRes val badgeId : Int,// = R.string.picker_long_resource,
)

private fun getAllResourceFields (
    from : KClass<*>,
    filter : (String) -> Boolean = { true }
) : List<Field> {
    return from.java.fields.filterNot { it.name.contains("launcher") }.filter { filter(it.name) }
}

@Composable
private fun AdvancedResourcePickerDialog (
    onSelected: (Int?) -> Unit,
    properties : AdvancedResourcePickerProperties,
) {
    val preFilteredEntries = remember<List<Field>> (Unit) { properties.from.flatMap { getAllResourceFields(it, properties.preFilter) } }
    val inSelected = remember { mutableStateOf(properties.inFilter.firstOrNull()) }
    val inFilteredEntries = remember<List<Field>> (inSelected.value) {
        inSelected.value?.let { selector -> preFilteredEntries.filter { selector.second(it.name) } } ?: preFilteredEntries
    }
    OutlinedDialog(onDismiss = { onSelected(null) }, properties.titleId) {
        if (inSelected.value != null) {
            OutlinedSpinnerField(
                value = inSelected.value?.first ?: "",
                onValueChange = { neValue -> inSelected.value = properties.inFilter.firstOrNull { it.first == neValue } },
                properties = SpinnerProperties(
                    optionValues = properties.inFilter.map { it.first }.toTypedArray(),
                ),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.hint_ending,
                ),
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(properties.preferredCount),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(inFilteredEntries) { entry ->
                val id : Int? = try { entry.getInt(Int) } catch (_ : Exception) { null }
                id?.let {
                    Image(
                        painterResource(id = id),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clickable(onClick = { onSelected(id) }),
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvancedResourcePickerControl (
    onValueChange: (Uri?) -> Unit,
    properties : AdvancedResourcePickerProperties,
) {
    val showPopup = remember { mutableStateOf(false) }
    if (showPopup.value) {
        AdvancedResourcePickerDialog(
            onSelected = { filename ->
                showPopup.value = false
                filename?.let { onValueChange(properties.uriProvider(filename)) }
            },
            properties,
        )
    }
    DecoratedButton({ showPopup.value = true }, properties.icon, properties.badgeId, "resource gallery")
}

@Composable
fun PictureFieldActionsScope.AdvancedResourcePicker(properties : AdvancedResourcePickerProperties) = AdvancedResourcePickerControl(onValueChange, properties)
