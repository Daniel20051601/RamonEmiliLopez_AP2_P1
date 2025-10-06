package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import androidx.compose.material3.LocalContentColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Row

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntradaScreen(
    navController: NavController,
    viewModel: EditEntradaViewModel = hiltViewModel(),
    entradaId: Int? = null
) {
    LaunchedEffect(entradaId) {
        viewModel.onEvent(EditJugadorEvent.Load(entradaId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    EditEntradaBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntradaBody(
    state: EditEntradasUiState,
    onEvent: (EditJugadorEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    var entradaParaEliminar by remember { mutableStateOf<Entrada?>(null) }
    val scrollState = rememberScrollState()
    val titulo = if (state.canBeDeleted) "Editar Entrada" else "Nueva Entrada"

    val importe = remember(state.cantidad, state.precio) {
        val cantidad = state.cantidad.toIntOrNull() ?: 0
        val precio = state.precio.toDoubleOrNull() ?: 0.0
        cantidad * precio
    }

    entradaParaEliminar?.let { entrada ->
        AlertDialog(
            onDismissRequest = { entradaParaEliminar = null },
            title = { Text("Eliminar Entrada") },
            text = { Text("¿Estás seguro de que quieres eliminar esta entrada de ${entrada.nombreCliente}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(EditJugadorEvent.Delete)
                        entradaParaEliminar = null
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { entradaParaEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(titulo) },
                windowInsets = WindowInsets(top = 0.dp),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Fecha (YYYY-MM-DD)", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = state.fecha,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                enabled = true,
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

            Text("Nombre del Cliente", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = state.nombreCliente,
                onValueChange = { onEvent(EditJugadorEvent.NombreChanged(it)) },
                isError = state.nombreClienteError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nombreClienteError != null) {
                Text(
                    text = state.nombreClienteError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text("Cantidad", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = state.cantidad,
                onValueChange = {
                    val cantidad = it.toIntOrNull() ?: return@OutlinedTextField
                    onEvent(EditJugadorEvent.CantidadChanged(cantidad))
                },
                isError = state.cantidadError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.cantidadError != null) {
                Text(
                    text = state.cantidadError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text("Precio", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = state.precio,
                onValueChange = {
                    val precio = it.toDoubleOrNull() ?: return@OutlinedTextField
                    onEvent(EditJugadorEvent.PrecioChanged(precio))
                },
                isError = state.precioError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            if (state.precioError != null) {
                Text(
                    text = state.precioError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text("Importe", style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(
                value = importe.toString(),
                onValueChange = { },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = LocalContentColor.current,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(EditJugadorEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = {
                            entradaParaEliminar = Entrada(
                                entradaId = state.entradaId,
                                fecha = state.fecha,
                                nombreCliente = state.nombreCliente,
                                cantidad = state.cantidad.toIntOrNull() ?: 0,
                                precio = state.precio.toDoubleOrNull() ?: 0.0
                            )
                        },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

class EditEntradaUiStatePreviewProvider : PreviewParameterProvider<EditEntradasUiState> {
    override val values = sequenceOf(

        EditEntradasUiState(
            fecha = "2025-10-01",
            nombreCliente = "",
            cantidad = "",
            precio = "",
            canBeDeleted = false
        ),

        EditEntradasUiState(
            entradaId = 1,
            fecha = "2025-10-01",
            nombreCliente = "Gabriel Miguel",
            cantidad = "12",
            precio = "100.0",
            canBeDeleted = true
        ),

        EditEntradasUiState(
            fecha = "2025-10-01",
            nombreCliente = "Ra",
            cantidad = "-5",
            precio = "0",
            nombreClienteError = "El nombre es demasiado corto.",
            cantidadError = "Debe ser un valor positivo.",
            precioError = "El precio debe ser mayor que 0.",
            canBeDeleted = false
        ),

        EditEntradasUiState(
            entradaId = 1,
            fecha = "2025-10-01",
            nombreCliente = "Ramón Almonte",
            cantidad = "12",
            precio = "100.0",
            isSaving = true,
            canBeDeleted = true
        ),

        EditEntradasUiState(
            entradaId = 1,
            fecha = "2025-10-01",
            nombreCliente = "Ramón Almonte",
            cantidad = "12",
            precio = "100.0",
            isDeleting = true,
            canBeDeleted = true
        )
    )
}

@Preview(showSystemUi = true, showBackground = true, widthDp = 320)
@Composable
fun EditEntradaBodyPreview(
    @PreviewParameter(EditEntradaUiStatePreviewProvider::class) state: EditEntradasUiState
) {
    MaterialTheme {
        EditEntradaBody(
            state = state,
            onEvent = { event ->
                println("Preview Event: $event")
            },
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun EditEntradaNuevaEntradaPreview() {
    MaterialTheme {
        EditEntradaBody(
            state = EditEntradasUiState(
                fecha = "2025-10-01",
                nombreCliente = "",
                cantidad = "",
                precio = "",
                canBeDeleted = false,
                isSaving = false,
                isDeleting = false
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun EditEntradaExistentePreview() {
    MaterialTheme {
        EditEntradaBody(
            state = EditEntradasUiState(
                entradaId = 1,
                fecha = "2025-10-01",
                nombreCliente = "Ramón Almonte",
                cantidad = "12",
                precio = "100.0",
                canBeDeleted = true
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}