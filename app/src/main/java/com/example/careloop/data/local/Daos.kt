package com.example.careloop.data.local

import androidx.room.*
import com.example.careloop.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CareLoopDao {
    // Patients
    @Query("SELECT * FROM patients ORDER BY name ASC")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE id = :id")
    fun getPatientById(id: String): Flow<Patient?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<Patient>)

    @Update
    suspend fun updatePatient(patient: Patient)

    // Care Plans
    @Query("SELECT * FROM care_plans ORDER BY createdAt DESC")
    fun getAllCarePlans(): Flow<List<CarePlan>>

    @Query("SELECT * FROM care_plans WHERE patientId = :patientId")
    fun getCarePlansForPatient(patientId: String): Flow<List<CarePlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarePlans(plans: List<CarePlan>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarePlan(plan: CarePlan)

    @Update
    suspend fun updateCarePlan(plan: CarePlan)

    // Care Tasks
    @Query("SELECT * FROM care_tasks ORDER BY dueDate ASC")
    fun getAllCareTasks(): Flow<List<CareTask>>

    @Query("SELECT * FROM care_tasks WHERE patientId = :patientId")
    fun getTasksForPatient(patientId: String): Flow<List<CareTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareTasks(tasks: List<CareTask>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareTask(task: CareTask)

    @Update
    suspend fun updateCareTask(task: CareTask)

    // Medical Documents
    @Query("SELECT * FROM medical_documents ORDER BY uploadDate DESC")
    fun getAllDocuments(): Flow<List<MedicalDocument>>

    @Query("SELECT * FROM medical_documents WHERE patientId = :patientId")
    fun getDocumentsForPatient(patientId: String): Flow<List<MedicalDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocuments(docs: List<MedicalDocument>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: MedicalDocument)

    // Medication Reminders
    @Query("SELECT * FROM medication_reminders WHERE patientId = :patientId")
    fun getRemindersForPatient(patientId: String): Flow<List<MedicationReminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<MedicationReminder>)

    @Update
    suspend fun updateReminder(reminder: MedicationReminder)

    // Operational Metrics
    @Query("SELECT * FROM operational_metrics")
    fun getAllOperationalMetrics(): Flow<List<OperationalMetric>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperationalMetrics(metrics: List<OperationalMetric>)
}
