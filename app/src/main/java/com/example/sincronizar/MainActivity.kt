package com.example.sincronizar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sincronizar.ui.theme.SincronizarTheme
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    private lateinit var databaseRef: DatabaseReference
    private var contador by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        databaseRef = FirebaseDatabase.getInstance().getReference("contador")

        // Escuchar cambios en Firebase
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contador = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })

        setContent {
            SincronizarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen(
                        contador = contador,
                        onIncrement = { incrementarContador() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun incrementarContador() {
        val nuevoContador = contador + 1
        databaseRef.setValue(nuevoContador)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CounterScreen(
    contador: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) {
        Column {
            Text(text = "Contador: $contador", modifier = Modifier.padding(16.dp))
            Button(onClick = { onIncrement() }) {
                Text(text = "Incrementar contador")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    SincronizarTheme {
        CounterScreen(contador = 0, onIncrement = {})
    }
}
