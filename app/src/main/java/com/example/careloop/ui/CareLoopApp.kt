package com.example.careloop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.careloop.data.ai.GeminiAiService
import com.example.careloop.ui.components.AiResultDialog
import com.example.careloop.ui.components.VoiceAssistantModal
import com.example.careloop.ui.screens.*
import com.example.careloop.ui.viewmodel.AppRole
import com.example.careloop.ui.viewmodel.CareLoopViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareLoopApp(
    viewModel: CareLoopViewModel = viewModel()
) {
    val activeRole by viewModel.activeRole.collectAsState()
    val allPatients by viewModel.allPatients.collectAsState()
    val selectedPatientId by viewModel.selectedPatientId.collectAsState()
    val selectedPatient by viewModel.selectedPatient.collectAsState()
    val aiState by viewModel.aiState.collectAsState()
    val voiceDialogActive by viewModel.voiceDialogActive.collectAsState()

    var patientDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = "CareLoop",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CareLoop",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = MedicalNavy
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = PrimaryTealContainer
                                ) {
                                    Text(
                                        text = "AI",
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OnPrimaryTealContainer
                                    )
                                }
                            }
                            Text(
                                text = "Intelligent Healthcare Platform",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    // Patient Selector Dropdown
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SlateSurfaceVariant,
                            modifier = Modifier
                                .clickable { patientDropdownExpanded = true }
                                .padding(end = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Patient",
                                    tint = PrimaryTeal,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedPatient?.name?.split(" ")?.firstOrNull() ?: "Patient",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = SlateTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = patientDropdownExpanded,
                            onDismissRequest = { patientDropdownExpanded = false }
                        ) {
                            allPatients.forEach { p ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = "${p.name} (${p.age}y)",
                                                fontWeight = if (p.id == selectedPatientId) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                text = p.primaryCondition,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SlateTextSecondary
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.selectPatient(p.id)
                                        patientDropdownExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = if (p.id == selectedPatientId) Icons.Default.CheckCircle else Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = if (p.id == selectedPatientId) PrimaryTeal else SlateTextMuted
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = activeRole == AppRole.PATIENT_COMPANION,
                    onClick = { viewModel.setRole(AppRole.PATIENT_COMPANION) },
                    icon = {
                        Icon(
                            imageVector = if (activeRole == AppRole.PATIENT_COMPANION) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Companion"
                        )
                    },
                    label = { Text("Companion", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryTealContainer,
                        selectedIconColor = OnPrimaryTealContainer,
                        selectedTextColor = PrimaryTeal
                    )
                )

                NavigationBarItem(
                    selected = activeRole == AppRole.CLINICAL_COORDINATION,
                    onClick = { viewModel.setRole(AppRole.CLINICAL_COORDINATION) },
                    icon = {
                        Icon(
                            imageVector = if (activeRole == AppRole.CLINICAL_COORDINATION) Icons.AutoMirrored.Filled.Assignment else Icons.AutoMirrored.Outlined.Assignment,
                            contentDescription = "Care Plan"
                        )
                    },
                    label = { Text("Care Plan", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryTealContainer,
                        selectedIconColor = OnPrimaryTealContainer,
                        selectedTextColor = PrimaryTeal
                    )
                )

                NavigationBarItem(
                    selected = activeRole == AppRole.CARE_INTELLIGENCE,
                    onClick = { viewModel.setRole(AppRole.CARE_INTELLIGENCE) },
                    icon = {
                        Icon(
                            imageVector = if (activeRole == AppRole.CARE_INTELLIGENCE) Icons.Filled.Insights else Icons.Outlined.Insights,
                            contentDescription = "Intelligence"
                        )
                    },
                    label = { Text("Intelligence", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryTealContainer,
                        selectedIconColor = OnPrimaryTealContainer,
                        selectedTextColor = PrimaryTeal
                    )
                )

                NavigationBarItem(
                    selected = activeRole == AppRole.MEDICAL_DOCS,
                    onClick = { viewModel.setRole(AppRole.MEDICAL_DOCS) },
                    icon = {
                        Icon(
                            imageVector = if (activeRole == AppRole.MEDICAL_DOCS) Icons.Filled.Description else Icons.Outlined.Description,
                            contentDescription = "Documents"
                        )
                    },
                    label = { Text("Documents", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryTealContainer,
                        selectedIconColor = OnPrimaryTealContainer,
                        selectedTextColor = PrimaryTeal
                    )
                )
            }
        },
        floatingActionButton = {
            if (activeRole == AppRole.PATIENT_COMPANION) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.voiceDialogActive.value = true },
                    containerColor = PrimaryTeal,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Voice") },
                    text = { Text("Voice Assistant", fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SlateBackground)
        ) {
            when (activeRole) {
                AppRole.PATIENT_COMPANION -> PatientCompanionScreen(viewModel = viewModel)
                AppRole.CLINICAL_COORDINATION -> ClinicalCoordinationScreen(viewModel = viewModel)
                AppRole.CARE_INTELLIGENCE -> CareIntelligenceScreen(viewModel = viewModel)
                AppRole.MEDICAL_DOCS -> MedicalDocumentsScreen(viewModel = viewModel)
            }
        }

        // Global AI Result Modal
        AiResultDialog(
            state = aiState,
            onDismiss = { viewModel.clearAiState() }
        )

        // Voice Assistant Query Modal
        VoiceAssistantModal(
            isOpen = voiceDialogActive,
            onDismiss = { viewModel.voiceDialogActive.value = false },
            onQuerySubmit = { query ->
                viewModel.processVoiceQuery(query)
            }
        )
    }
}
