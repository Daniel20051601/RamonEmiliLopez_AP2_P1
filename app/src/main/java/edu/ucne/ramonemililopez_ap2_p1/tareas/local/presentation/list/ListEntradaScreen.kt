package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEntradaScreen(
    navController: NavController,
    viewModel: ListEntradaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var filterText by remember { mutableStateOf("") }
    var filterFecha by remember { mutableStateOf("") }
    var isFilterExpanded by remember { mutableStateOf(false) }
    var selectedFilterType by remember { mutableStateOf(FilterType.NOMBRE_CLIENTE) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        filterFecha = localDate.format(formatter)
                        viewModel.onFilterChange(
                            filterType = selectedFilterType,
                            filterValue = filterText,
                            filterDate = filterFecha
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Entradas de Huacales") },
                windowInsets = WindowInsets(top = 0.dp),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("edit_entrada_screen/0")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "${state.entradas.size}",
                        fontSize = 18.sp
                    )
                    val total = state.entradas.sumOf { it.cantidad * it.precio }
                    Text(
                        "$${"%,.2f".format(total)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isFilterExpanded = !isFilterExpanded }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("Filtros", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtros"
                    )
                }

                AnimatedVisibility(
                    visible = isFilterExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isDropdownExpanded,
                            onExpandedChange = { isDropdownExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = when (selectedFilterType) {
                                    FilterType.ENTRADA_ID -> "ID de Entrada"
                                    FilterType.NOMBRE_CLIENTE -> "Nombre de Cliente"
                                    FilterType.CANTIDAD -> "Cantidad"
                                    FilterType.PRECIO -> "Precio"
                                },
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                label = { Text("Filtrar por") }
                            )

                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier.exposedDropdownSize()
                            ) {
                                DropdownMenuItem(
                                    text = { Text("ID de Entrada") },
                                    onClick = {
                                        selectedFilterType = FilterType.ENTRADA_ID
                                        isDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Nombre de Cliente") },
                                    onClick = {
                                        selectedFilterType = FilterType.NOMBRE_CLIENTE
                                        isDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cantidad") },
                                    onClick = {
                                        selectedFilterType = FilterType.CANTIDAD
                                        isDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Precio") },
                                    onClick = {
                                        selectedFilterType = FilterType.PRECIO
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = filterText,
                            onValueChange = {
                                filterText = it
                                viewModel.onFilterChange(
                                    filterType = selectedFilterType,
                                    filterValue = it,
                                    filterDate = filterFecha
                                )
                            },
                            label = {
                                Text(
                                    when (selectedFilterType) {
                                        FilterType.ENTRADA_ID -> "Buscar por ID"
                                        FilterType.NOMBRE_CLIENTE -> "Buscar por nombre"
                                        FilterType.CANTIDAD -> "Buscar por cantidad"
                                        FilterType.PRECIO -> "Buscar por precio"
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = when (selectedFilterType) {
                                FilterType.ENTRADA_ID, FilterType.CANTIDAD ->
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    )
                                FilterType.PRECIO ->
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal
                                    )
                                else ->
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Text
                                    )
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = filterFecha,
                            onValueChange = {},
                            label = { Text("Fecha") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showDatePicker = true
                                    focusManager.clearFocus()
                                },
                            readOnly = true,
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = LocalContentColor.current,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        )
                    }
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (state.entradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        "No hay entradas. ¡Añade una!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(state.entradas) { entrada ->
                        EntradaItem(
                            entrada = entrada,
                            onEntradaClick = {
                                navController.navigate("edit_entrada_screen/${entrada.entradaId}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EntradaItem(
    entrada: Entrada,
    onEntradaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onEntradaClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entrada.nombreCliente,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = entrada.fecha,
                    fontSize = 14.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "${entrada.cantidad} x", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$${"%,.2f".format(entrada.precio)}", fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "= $${"%,.2f".format(entrada.cantidad * entrada.precio)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntradaItemPreview() {
    MaterialTheme {
        EntradaItem(
            entrada = Entrada(
                entradaId = 1,
                fecha = "2025-10-01",
                nombreCliente = "Ramón Almonte",
                cantidad = 12,
                precio = 100.0
            ),
            onEntradaClick = {}
        )
    }
}
