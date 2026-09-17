package com.example.careloop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val primaryCondition: String,
    val allergies: String,
    val missedAppointmentRisk: String,
    val riskScorePercent: Int,
    val riskFactors: String,
    val supportiveOutreachStatus: String,
    val contactNumber: String,
    val preferredLanguage: String = "Hindi"
)

@Entity(tableName = "care_plans")
data class CarePlan(
    @PrimaryKey val id: String,
    val patientId: String,
    val patientName: String,
    val title: String,
    val doctorName: String,
    val status: String, // Active, In Review, Completed
    val completenessScore: Int, // 0 - 100
    val aiAuditWarnings: String,
    val createdAt: String
)

@Entity(tableName = "care_tasks")
data class CareTask(
    @PrimaryKey val id: String,
    val carePlanId: String,
    val patientId: String,
    val title: String,
    val category: String, // Lab Test, Referral, Medication, Follow-up
    val dueDate: String,
    val status: String, // Pending, Scheduled, Completed, Overdue
    val assignedProvider: String,
    val aiInsight: String
)

@Entity(tableName = "medical_documents")
data class MedicalDocument(
    @PrimaryKey val id: String,
    val patientId: String,
    val title: String,
    val docType: String, // Lab Report, Prescription, Discharge Summary, Referral Letter
    val uploadDate: String,
    val rawText: String,
    val plainSummary: String,
    val hindiSummary: String,
    val extractedEntities: String,
    val conflictAlerts: String,
    val questionsForDoctor: String
)

@Entity(tableName = "medication_reminders")
data class MedicationReminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientId: String,
    val medicationName: String,
    val dosage: String,
    val frequency: String,
    val timeSlot: String,
    val isTaken: Boolean = false,
    val instructionsHindi: String = ""
)

@Entity(tableName = "operational_metrics")
data class OperationalMetric(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clinicName: String,
    val department: String,
    val expectedDemand: Int,
    val predictedNoShowRate: String,
    val labTurnaroundDelayRisk: String,
    val recommendedStaffing: String,
    val bottleneckInsight: String
)
