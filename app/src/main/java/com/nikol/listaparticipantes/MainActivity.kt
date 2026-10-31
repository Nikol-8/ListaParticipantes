package com.nikol.listaparticipantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikol.listaparticipantes.ui.theme.ListaParticipantesTheme

data class Participante(
    val id: Int,
    val nombre: String,
    val alias: String,
    val profesion: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold { padding ->
                    PantallaTorneo(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun PantallaTorneo(modifier: Modifier = Modifier) {
    //Estado del formulario
    var nombre by rememberSaveable { mutableStateOf("") }
    var alias by rememberSaveable { mutableStateOf("") }
    var profesion by rememberSaveable { mutableStateOf("") }

    //Lista observable de participantes
    val participantes = remember { mutableStateListOf<Participante>() }

    //Contador para dar dorsales (ids) únicos
    var siguienteId by remember { mutableStateOf(1) }

    val formularioValido =
        nombre.isNotBlank() && alias.isNotBlank() && profesion.isNotBlank()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Inscripción al torneo",
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = alias,
            onValueChange = { alias = it },
            label = { Text("Alias") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = profesion,
            onValueChange = { profesion = it },
            label = { Text("Profesión") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )

        Button(
            onClick = {
                if (formularioValido) {
                    val nuevo = Participante(
                        id = siguienteId,
                        nombre = nombre.trim(),
                        alias = alias.trim(),
                        profesion = profesion.trim()
                    )
                    participantes.add(nuevo)
                    siguienteId++

                    nombre = ""
                    alias = ""
                    profesion = ""
                }
            },
            enabled = formularioValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir participante")
        }

        Text("Participantes inscritos: ${participantes.size}")

        if (participantes.isEmpty()) {
            Text("Todavía no hay participantes")
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = participantes,
                key = { participante -> participante.id }
            ) { participante ->
                TarjetaParticipante(
                    participante = participante,
                    onEliminar = {
                        participantes.removeAll { it.id == participante.id }
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaParticipante(
    participante: Participante,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "#${participante.id} · ${participante.nombre}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Alias: ${participante.alias}")
            Text(text = "Profesión: ${participante.profesion}")

            Button(onClick = onEliminar) {
                Text("Eliminar")
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PantallaTorneoPreview() {
    MaterialTheme { PantallaTorneo() }
}
