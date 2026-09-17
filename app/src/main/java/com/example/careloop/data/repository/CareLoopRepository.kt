package com.example.careloop.data.repository

import com.example.careloop.data.local.CareLoopDao
import com.example.careloop.data.local.SampleData
import com.example.careloop.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CareLoopRepository(private val dao: CareLoopDao) {

    val allPatients: Flow<List<Patient>> = dao.getAllPatients()
    val allCarePlans: Flow<List<CarePlan>> = dao.getAllCarePlans()
    val allCareTasks: Flow<List<CareTask>> = dao.getAllCareTasks()
    val allDocuments: Flow<List<MedicalDocument>> = dao.getAllDocuments()
    val allOperationalMetrics: Flow<List<OperationalMetric>> = dao.getAllOperationalMetrics()

    fun getPatientById(id: String): Flow<Patient?> = dao.getPatientById(id)
    fun getCarePlansForPatient(patientId: String): Flow<List<CarePlan>> = dao.getCarePlansForPatient(patientId)
    fun getTasksForPatient(patientId: String): Flow<List<CareTask>> = dao.getTasksForPatient(patientId)
    fun getDocumentsForPatient(patientId: String): Flow<List<MedicalDocument>> = dao.getDocumentsForPatient(patientId)
    fun getRemindersForPatient(patientId: String): Flow<List<MedicationReminder>> = dao.getRemindersForPatient(patientId)

    suspend fun toggleReminderTaken(reminder: MedicationReminder) {
        dao.updateReminder(reminder.copy(isTaken = !reminder.isTaken))
    }

    suspend fun updateTaskStatus(task: CareTask, newStatus: String) {
        dao.updateCareTask(task.copy(status = newStatus))
    }

    suspend fun addCarePlan(plan: CarePlan) {
        dao.insertCarePlan(plan)
    }

    suspend fun addCareTask(task: CareTask) {
        dao.insertCareTask(task)
    }

    suspend fun addDocument(doc: MedicalDocument) {
        dao.insertDocument(doc)
    }

    suspend fun updatePatient(patient: Patient) {
        dao.updatePatient(patient)
    }

    suspend fun initializeIfEmpty() {
        val patients = dao.getAllPatients().firstOrNull()
        if (patients.isNullOrEmpty()) {
            dao.insertPatients(SampleData.samplePatients)
            dao.insertCarePlans(SampleData.sampleCarePlans)
            dao.insertCareTasks(SampleData.sampleCareTasks)
            dao.insertDocuments(SampleData.sampleDocuments)
            dao.insertReminders(SampleData.sampleReminders)
            dao.insertOperationalMetrics(SampleData.sampleMetrics)
        }
    }
}
