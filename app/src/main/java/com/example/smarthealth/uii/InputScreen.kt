package com.example.smarthealth.uii

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarthealth.R
import com.example.smarthealth.data.CalculationResult
import com.example.smarthealth.data.HealthCalculator
import com.example.smarthealth.ui.theme.AppThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    currentThemeColor: AppThemeColor,
    onColorSelect: (AppThemeColor) -> Unit,
    onLanguageToggle: () -> Unit,
    useMetric: Boolean,
    onUnitToggle: () -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var heightFt by remember { mutableStateOf("") }
    var heightIn by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf<CalculationResult?>(null) }
    var showColorMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                },
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = onLanguageToggle,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.lang_toggle),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        TextButton(
                            onClick = onUnitToggle,
                            modifier = Modifier.padding(start = 0.dp)
                        ) {
                            Text(
                                text = if (useMetric) stringResource(R.string.unit_metric) else stringResource(R.string.unit_imperial),
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showColorMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Palette, 
                                contentDescription = stringResource(R.string.theme_color),
                                tint = currentThemeColor.seed
                            )
                        }
                        DropdownMenu(
                            expanded = showColorMenu,
                            onDismissRequest = { showColorMenu = false }
                        ) {
                            AppThemeColor.entries.forEach { colorOption ->
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(colorOption.seed)
                                        )
                                    },
                                    text = { Text(stringResource(colorOption.labelRes)) },
                                    onClick = {
                                        onColorSelect(colorOption)
                                        showColorMenu = false
                                    }
                                )
                            }
                        }
                    }
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = stringResource(if (isDarkMode) R.string.theme_light else R.string.theme_dark)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Gender Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = isMale,
                    onClick = { isMale = true },
                    label = { Text(stringResource(R.string.male), modifier = Modifier.padding(vertical = 8.dp)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = if (isMale) { { Icon(Icons.Default.Check, null) } } else null
                )
                FilterChip(
                    selected = !isMale,
                    onClick = { isMale = false },
                    label = { Text(stringResource(R.string.female), modifier = Modifier.padding(vertical = 8.dp)) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = if (!notIsMale(isMale)) { { Icon(Icons.Default.Check, null) } } else null
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields
            ModernTextField(
                value = weight,
                onValueChange = { if (it.length <= 6) weight = it },
                label = if (useMetric) stringResource(R.string.weight_label) else stringResource(R.string.weight_lb),
                icon = Icons.Default.Edit,
                keyboardType = KeyboardType.Decimal
            )

            if (useMetric) {
                ModernTextField(
                    value = heightCm,
                    onValueChange = { if (it.length <= 3) heightCm = it },
                    label = stringResource(R.string.height_label),
                    icon = Icons.Default.Info,
                    keyboardType = KeyboardType.Number
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ModernTextField(
                        value = heightFt,
                        onValueChange = { if (it.length <= 1) heightFt = it },
                        label = stringResource(R.string.height_ft),
                        icon = Icons.Default.Height,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    ModernTextField(
                        value = heightIn,
                        onValueChange = { if (it.length <= 2) heightIn = it },
                        label = stringResource(R.string.height_in),
                        icon = Icons.Default.Straighten,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            ModernTextField(
                value = age,
                onValueChange = { if (it.length <= 3) age = it },
                label = stringResource(R.string.age_label),
                icon = Icons.Default.Person,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val wInput = weight.replace(",", ".").toFloatOrNull() ?: 0f
                    val a = age.toIntOrNull() ?: 0
                    
                    val wKg = if (useMetric) wInput else wInput / 2.20462f
                    val hCm = if (useMetric) {
                        heightCm.toIntOrNull() ?: 0
                    } else {
                        val ft = heightFt.toIntOrNull() ?: 0
                        val inch = heightIn.toIntOrNull() ?: 0
                        ((ft * 30.48) + (inch * 2.54)).toInt()
                    }

                    if (wKg > 0 && hCm > 0 && a > 0) {
                        val bmi = HealthCalculator.calculateBMI(wKg, hCm)
                        result = HealthCalculator.getResult(bmi, wKg, hCm, a, isMale)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(stringResource(R.string.calculate), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(visible = result != null) {
                result?.let { res ->
                    ResultView(res)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun notIsMale(isMale: Boolean): Boolean = isMale

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
fun ResultView(result: CalculationResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.bmi_result, result.bmi),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(result.statusResId),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            if (result.recommendationResId != 0) {
                Text(
                    text = stringResource(result.recommendationResId),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 24.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Text(
                text = stringResource(R.string.calories_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            EnergyRow(
                label = stringResource(R.string.maintenance_desc),
                value = result.maintenanceCalories
            )

            EnergyRow(
                label = stringResource(R.string.target_desc),
                value = result.targetCalories,
                goalLabel = stringResource(result.goalResId),
                isTarget = true
            )
        }
    }
}

@Composable
fun EnergyRow(label: String, value: Int, goalLabel: String? = null, isTarget: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.kcal_format, value),
                style = if (isTarget) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isTarget) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            if (goalLabel != null) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = goalLabel,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
