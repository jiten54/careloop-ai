package com.example.careloop.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.careloop.data.ai.GeminiAiService
import com.example.careloop.data.local.CareLoopDatabase
import com.example.careloop.data.model.*
import com.example.careloop.data.repository.CareLoopRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

enum class AppRole(val title: String) {
    PATIENT_COMPANION("Patient Companion"),
    CLINICAL_COORDINATION("Care Coordination"),
    CARE_INTELLIGENCE("Care Intelligence"),
    MEDICAL_DOCS("Medical Documents")
}

data class AiAnalysisState(
    val isRunning: Boolean = false,
    val title: String = "",
    val outputText: String = "",
    val modelUsed: String = GeminiAiService.MODEL_FLASH,
    val isApiKeyActive: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class CareLoopViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CareLoopRepository

    val activeRole = MutableStateFlow(AppRole.PATIENT_COMPANION)
    val selectedPatientId = MutableStateFlow("PT-1042")

    val aiState = MutableStateFlow(AiAnalysisState())
    val voiceDialogActive = MutableStateFlow(false)
    val voiceTranscript = MutableStateFlow("")

    val allPatients: StateFlow<List<Patient>>
    val selectedPatient: StateFlow<Patient?>
    val activeCarePlans: StateFlow<List<CarePlan>>
    val activeTasks: StateFlow<List<CareTask>>
    val activeDocuments: StateFlow<List<MedicalDocument>>
    val activeReminders: StateFlow<List<MedicationReminder>>
    val operationalMetrics: StateFlow<List<OperationalMetric>>

    init {
        val db = CareLoopDatabase.getDatabase(application, viewModelScope)
        repository = CareLoopRepository(db.careLoopDao())

        allPatients = repository.allPatients.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )

        selectedPatient = selectedPatientId.flatMapLatest { id ->
            repository.getPatientById(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        activeCarePlans = selectedPatientId.flatMapLatest { id ->
            repository.getCarePlansForPatient(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        activeTasks = selectedPatientId.flatMapLatest { id ->
            repository.getTasksForPatient(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        activeDocuments = selectedPatientId.flatMapLatest { id ->
            repository.getDocumentsForPatient(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        activeReminders = selectedPatientId.flatMapLatest { id ->
            repository.getRemindersForPatient(id)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        operationalMetrics = repository.allOperationalMetrics.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )

        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    fun setRole(role: AppRole) {
        activeRole.value = role
    }

    fun selectPatient(patientId: String) {
        selectedPatientId.value = patientId
    }

    fun toggleReminder(reminder: MedicationReminder) {
        viewModelScope.launch {
            repository.toggleReminderTaken(reminder)
        }
    }

    fun updateTaskStatus(task: CareTask, newStatus: String) {
        viewModelScope.launch {
            repository.updateTaskStatus(task, newStatus)
        }
    }

    fun clearAiState() {
        aiState.value = AiAnalysisState()
    }

    // 1. Explain Medical Report (English & Hindi)
    fun explainReportWithAi(document: MedicalDocument, language: String = "Hindi & English") {
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Analyzing ${document.title}",
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val prompt = """
                You are CareLoop Companion's Medical Report Explainer.
                Analyze the following medical report:
                Document Title: ${document.title}
                Report Content:
                ${document.rawText}

                Provide a patient-friendly explanation in $language:
                1. Clear plain-language explanation of key findings without medical jargon.
                2. सरल हिंदी व्याख्या (Easy-to-understand explanation in Hindi).
                3. Key questions the patient should ask their attending doctor.
                4. Essential safety advice and reminder that doctors make final clinical determinations.
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are a warm, patient, and medically responsible healthcare explainer. Translate medical complexity into accessible language.",
                model = GeminiAiService.MODEL_FLASH
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Medical Report Explanation (${document.title})",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }

    // 2. Audit Care Plan Completeness
    fun auditCarePlanCompleteness(plan: CarePlan) {
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Auditing Care Plan Completeness",
                modelUsed = GeminiAiService.MODEL_PRO,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val patient = selectedPatient.value
            val tasks = activeTasks.value.filter { it.carePlanId == plan.id }
            val tasksSummary = tasks.joinToString("\n") { "- ${it.category}: ${it.title} [Due: ${it.dueDate}, Status: ${it.status}]" }

            val prompt = """
                Evaluate the completeness and safety of this care plan:
                Patient: ${patient?.name} (Age ${patient?.age}, Condition: ${patient?.primaryCondition}, Allergies: ${patient?.allergies})
                Care Plan: ${plan.title} (Doctor: ${plan.doctorName})
                Tasks:
                $tasksSummary

                Analyze for:
                1. Missing critical milestones or overdue clinical guideline tests.
                2. Duplicate tasks or conflicting timelines.
                3. Medication or follow-up gaps.
                4. Assign an updated Completeness Score (0-100%) with actionable next steps.
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are a clinical quality and care coordination audit intelligence engine. Adhere strictly to evidence-based medical standards.",
                model = GeminiAiService.MODEL_PRO
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Care Plan Completeness & Safety Audit",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_PRO,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }

    // 3. Medication Safety Checker
    fun checkMedicationSafety(newDrugName: String) {
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Evaluating Medication Safety for $newDrugName",
                modelUsed = GeminiAiService.MODEL_PRO,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val patient = selectedPatient.value
            val currentReminders = activeReminders.value.joinToString(", ") { "${it.medicationName} (${it.dosage})" }

            val prompt = """
                Perform a clinical medication safety evaluation:
                Patient: ${patient?.name}, Age ${patient?.age}, Primary Condition: ${patient?.primaryCondition}
                Documented Allergies: ${patient?.allergies}
                Current Active Medications: $currentReminders
                Candidate / Requested Medication: $newDrugName

                Screen for:
                1. Drug-Drug Interactions (especially bleeding risk, QT prolongation, sedation, potassium shifts).
                2. Allergy Cross-Reactivity.
                3. Duplicate therapeutic agents.
                4. Clear clinical verdict: Approved, Caution, or Contraindicated with evidence for clinician review.
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are CareLoop's Clinical Safety Checker. Flag dangerous combinations and explain pharmacokinetic/pharmacodynamic rationale.",
                model = GeminiAiService.MODEL_PRO
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Medication Safety & Interaction Check ($newDrugName)",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_PRO,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }

    // 4. Predict Missed Appointment Risk & Trigger Supportive Outreach
    fun runMissedAppointmentPrediction() {
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Calculating Attendance Prediction & Outreach Plan",
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val patient = selectedPatient.value
            val prompt = """
                Evaluate missed appointment risk based on consented patient indicators:
                Patient: ${patient?.name}
                Condition: ${patient?.primaryCondition}
                Known Risk Factors: ${patient?.riskFactors}
                Current Risk Assessment: ${patient?.missedAppointmentRisk}

                Requirements:
                1. Explain the underlying socio-demographic, transport, and clinical determinants.
                2. Adhere strictly to the CareLoop fairness principle: Missed-appointment predictions must trigger supportive outreach, NEVER denial of care or penalization.
                3. Design an automated, compassionate supportive outreach plan (e.g., multilingual voice check-in, flexible transit assistance, home lab collection, tele-health conversion).
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are CareLoop's Predictive Care Intelligence engine. Ensure predictive fairness and patient-centric supportive interventions.",
                model = GeminiAiService.MODEL_FLASH
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Predictive Attendance & Supportive Outreach",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }

    // 5. Clinical Note Assistant (Rough consultation notes to structured SOAP)
    fun convertDoctorNotesToSoap(roughNotes: String) {
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Structuring Consultation Notes",
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val patient = selectedPatient.value
            val prompt = """
                Convert the following clinician rough consultation note into a standardized, polished SOAP note:
                Patient: ${patient?.name} | Age: ${patient?.age} | Known Conditions: ${patient?.primaryCondition}
                Rough Doctor Notes:
                $roughNotes

                Format:
                - SUBJECTIVE
                - OBJECTIVE
                - ASSESSMENT
                - PLAN (with coordinated next steps for lab tests and patient reminders)
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are CareLoop's Clinical Documentation Assistant. Convert unstructured clinician dictated notes into precise, compliant SOAP documentation.",
                model = GeminiAiService.MODEL_FLASH
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Structured Clinical Note (SOAP)",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_FLASH,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }

    // 6. Voice Assistant Query
    fun processVoiceQuery(userQuery: String) {
        voiceTranscript.value = userQuery
        viewModelScope.launch {
            aiState.value = AiAnalysisState(
                isRunning = true,
                title = "Listening & Processing: '$userQuery'",
                modelUsed = GeminiAiService.MODEL_LITE,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )

            val patient = selectedPatient.value
            val prompt = """
                The patient ${patient?.name} asked via voice assistant:
                "$userQuery"

                Context:
                - Primary condition: ${patient?.primaryCondition}
                - Active medications: ${activeReminders.value.joinToString { it.medicationName }}
                - Preferred language: ${patient?.preferredLanguage ?: "Hindi / English"}

                Provide a direct, spoken-style answer:
                1. Short, conversational, caring response in simple English.
                2. सरल हिंदी में उत्तर (Short Hindi spoken response).
                3. Gentle medical disclaimer when symptoms warrant contacting their doctor.
            """.trimIndent()

            val result = GeminiAiService.generateContent(
                prompt = prompt,
                systemInstruction = "You are CareLoop Companion's voice assistant. Speak with clarity, empathy, and simplicity for individuals with varying literacy.",
                model = GeminiAiService.MODEL_LITE
            )

            aiState.value = AiAnalysisState(
                isRunning = false,
                title = "Voice Assistant Response",
                outputText = result,
                modelUsed = GeminiAiService.MODEL_LITE,
                isApiKeyActive = GeminiAiService.isApiKeyConfigured()
            )
        }
    }
}
