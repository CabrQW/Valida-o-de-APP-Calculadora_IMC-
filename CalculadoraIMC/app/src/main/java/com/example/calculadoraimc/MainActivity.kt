package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraIMCTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0F7FA)), 
                    color = Color.Transparent
                ) {
                    CalculadoraIMCScreen()
                }
            }
        }
    }
}

@Composable
fun CalculadoraIMCScreen() {
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    val pesoValido = peso.replace(',', '.').toFloatOrNull() != null || peso.isBlank()
    val alturaValida = altura.replace(',', '.').toFloatOrNull() != null || altura.isBlank()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_imc),
            contentDescription = "Logo IMC",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Calculadora de IMC",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Peso
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            isError = !pesoValido,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Altura
        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            isError = !alturaValida,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão 
        Button(
            onClick = {
                val pesoFormatado = peso.replace(',', '.')
                val alturaFormatada = altura.replace(',', '.')

                val pesoFloat = pesoFormatado.toFloatOrNull()
                val alturaFloat = alturaFormatada.toFloatOrNull()

                resultado = when {
                    peso.isBlank() || altura.isBlank() -> "Preencha todos os campos!"
                    pesoFloat == null || alturaFloat == null -> "Digite valores numéricos válidos (ex: 70.5)"
                    alturaFloat == 0f -> "Altura não pode ser zero!"
                    else -> {
                        val imc = pesoFloat / (alturaFloat * alturaFloat)
                        "Seu IMC: %.2f\n%s".format(imc, classificarIMC(imc))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
        ) {
            Text("Calcular IMC", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = resultado,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}

private fun classificarIMC(imc: Float): String {
    return when {
        imc < 18.5 -> "Abaixo do peso"
        imc < 24.9 -> "Peso normal"
        imc < 29.9 -> "Sobrepeso"
        imc < 34.9 -> "Obesidade Grau I"
        imc < 39.9 -> "Obesidade Grau II"
        else -> "Obesidade Grau III"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculadoraIMC() {
    CalculadoraIMCTheme {
        CalculadoraIMCScreen()
    }
}
