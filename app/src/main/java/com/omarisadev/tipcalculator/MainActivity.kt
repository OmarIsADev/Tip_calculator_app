package com.omarisadev.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omarisadev.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme(darkTheme = false) {
                TipApp()
            }
        }
    }
}

@Composable
fun TipApp(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var percentInput by remember { mutableStateOf("") }
    var roundUpSwitch by remember { mutableStateOf(false) }

    val amount: Double = amountInput.toDoubleOrNull() ?: 0.0
    val percent: Double = percentInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount = amount, tipPercent = percent, roundUp = roundUpSwitch)

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start),
        )

        EditNumberField(
            label = stringResource(R.string.bill_amount),
            value = amountInput,
            onChange = { amountInput = it },
            action = ImeAction.Next,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        EditNumberField(
            label = stringResource(R.string.tip_percent),
            value = percentInput,
            onChange = { percentInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        RoundSwitch(
            roundUp = roundUpSwitch,
            onRoundUpChange = { roundUpSwitch = it }
        )

        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun RoundSwitch(
    modifier: Modifier = Modifier,
    roundUp: Boolean,
    onRoundUpChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(R.string.round_up)
        )

        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
        )
    }
}

@Composable
fun EditNumberField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: String,
    action: ImeAction = ImeAction.Done,
    onChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = action
        ),
        label = {
            Text(
                text = label
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTipApp() {
    TipApp()
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount

    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }

    return NumberFormat.getCurrencyInstance().format(tip)
}
