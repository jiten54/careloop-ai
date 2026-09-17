package com.example.careloop.data.local

import com.example.careloop.data.model.*

object SampleData {
    val samplePatients = listOf(
        Patient(
            id = "PT-1042",
            name = "Rajesh Sharma",
            age = 54,
            gender = "Male",
            primaryCondition = "Type 2 Diabetes Mellitus & Stage 2 Hypertension",
            allergies = "Penicillin, Sulfa drugs",
            missedAppointmentRisk = "High (74%)",
            riskScorePercent = 74,
            riskFactors = "Transport transit time > 75 mins, 2 previous missed follow-ups, daily wage shift worker, polypharmacy burden (6 Rx)",
            supportiveOutreachStatus = "Active: SMS + Hindi automated voice call dispatched; Teleconsult alternative offered",
            contactNumber = "+91 98102 34567",
            preferredLanguage = "Hindi"
        ),
        Patient(
            id = "PT-1043",
            name = "Priya Mukherjee",
            age = 34,
            gender = "Female",
            primaryCondition = "Gestational Diabetes (28 weeks) & Mild Hypothyroidism",
            allergies = "No Known Drug Allergies (NKDA)",
            missedAppointmentRisk = "Low (16%)",
            riskScorePercent = 16,
            riskFactors = "Good adherence history, family caregiver support, living within 5 km of clinic",
            supportiveOutreachStatus = "Routine: Standard WhatsApp care plan summary sent",
            contactNumber = "+91 98451 88920",
            preferredLanguage = "English"
        ),
        Patient(
            id = "PT-1044",
            name = "Sunil Varma",
            age = 68,
            gender = "Male",
            primaryCondition = "Post-PCI / Stent Placement (CAD) & Chronic Knee Osteoarthritis",
            allergies = "Aspirin induced dyspepsia, Ceftriaxone",
            missedAppointmentRisk = "Moderate (48%)",
            riskScorePercent = 48,
            riskFactors = "Caregiver availability uncertain on weekdays, mobility limitation from osteoarthritis",
            supportiveOutreachStatus = "Scheduled: Home phlebotomist visit booked for pre-visit blood draw",
            contactNumber = "+91 97110 54321",
            preferredLanguage = "Hindi"
        ),
        Patient(
            id = "PT-1045",
            name = "Kavita Patel",
            age = 49,
            gender = "Female",
            primaryCondition = "Seropositive Rheumatoid Arthritis on Methotrexate",
            allergies = "Codeine, NSAID gastro-intolerance",
            missedAppointmentRisk = "Low (22%)",
            riskScorePercent = 22,
            riskFactors = "Occasional nausea post-weekly dose, high health literacy",
            supportiveOutreachStatus = "Routine: Digital symptom diary active",
            contactNumber = "+91 99203 11445",
            preferredLanguage = "Hindi"
        )
    )

    val sampleCarePlans = listOf(
        CarePlan(
            id = "CP-8801",
            patientId = "PT-1042",
            patientName = "Rajesh Sharma",
            title = "Cardiometabolic & Renal Protection Protocol",
            doctorName = "Dr. Ananya Roy, MD (Cardiometabolic)",
            status = "In Review",
            completenessScore = 78,
            aiAuditWarnings = "⚠️ Missing deadline for 3-month HbA1c repeat; ⚠️ Metformin dosage conflict detected between Discharge Card and OPD prescription; ⚠️ Missing recommended Nephrology referral for eGFR < 60 mL/min.",
            createdAt = "Sep 12, 2026"
        ),
        CarePlan(
            id = "CP-8802",
            patientId = "PT-1043",
            patientName = "Priya Mukherjee",
            title = "Gestational Glycemic & Fetal Monitoring Plan",
            doctorName = "Dr. Sameer Joshi, MD (Obstetrics & Gyn)",
            status = "Active",
            completenessScore = 95,
            aiAuditWarnings = "All critical milestones present. Next ultrasound scheduled at 32 weeks.",
            createdAt = "Sep 10, 2026"
        ),
        CarePlan(
            id = "CP-8803",
            patientId = "PT-1044",
            patientName = "Sunil Varma",
            title = "Post-PCI Secondary Prevention & Anticoagulation",
            doctorName = "Dr. Vikram Sethi, DM (Interventional Cardiology)",
            status = "Active",
            completenessScore = 88,
            aiAuditWarnings = "⚠️ NSAID safety conflict: Patient inquired about Ibuprofen for joint pain while on Dual Antiplatelet Therapy (DAPT: Ticagrelor + Aspirin). Gastrointestinal bleeding risk flagged.",
            createdAt = "Sep 08, 2026"
        )
    )

    val sampleCareTasks = listOf(
        CareTask(
            id = "TK-301",
            carePlanId = "CP-8801",
            patientId = "PT-1042",
            title = "Fasting Lipid Profile & Serum Creatinine",
            category = "Lab Test",
            dueDate = "Sep 22, 2026",
            status = "Scheduled",
            assignedProvider = "Apex Diagnostic Lab (Home Phlebotomy)",
            aiInsight = "AI turnaround prediction: 18 hours. Buffer of 4 days before next clinician review."
        ),
        CareTask(
            id = "TK-302",
            carePlanId = "CP-8801",
            patientId = "PT-1042",
            title = "Nephrology Consultation for Stage 3a CKD evaluation",
            category = "Referral",
            dueDate = "Sep 28, 2026",
            status = "Pending",
            assignedProvider = "Metro Kidney Care (Dr. S. Kulkarni)",
            aiInsight = "Referral routed based on proximity to patient's transit line and morning clinic availability."
        ),
        CareTask(
            id = "TK-303",
            carePlanId = "CP-8801",
            patientId = "PT-1042",
            title = "Dilated Fundoscopy / Diabetic Retinopathy Screen",
            category = "Lab Test",
            dueDate = "Oct 05, 2026",
            status = "Pending",
            assignedProvider = "Vision Eye Hospital",
            aiInsight = "Care gap detected: Overdue by 4 months according to standard clinical diabetes guidelines."
        ),
        CareTask(
            id = "TK-304",
            carePlanId = "CP-8801",
            patientId = "PT-1042",
            title = "Clinician Review & Medication Reconciliation",
            category = "Follow-up",
            dueDate = "Oct 08, 2026",
            status = "Pending",
            assignedProvider = "Dr. Ananya Roy Clinic",
            aiInsight = "High risk of no-show (74%). Pre-appointment transport verification recommended 48h prior."
        ),
        CareTask(
            id = "TK-305",
            carePlanId = "CP-8802",
            patientId = "PT-1043",
            title = "Home Blood Glucose Log (4-point daily)",
            category = "Medication",
            dueDate = "Sep 18, 2026",
            status = "Completed",
            assignedProvider = "CareLoop Companion App",
            aiInsight = "Fasting values within target: average 89 mg/dL. 2-hr post-prandial average 128 mg/dL."
        ),
        CareTask(
            id = "TK-306",
            carePlanId = "CP-8803",
            patientId = "PT-1044",
            title = "Echocardiogram LVEF Assessment (3-month post-stent)",
            category = "Lab Test",
            dueDate = "Sep 25, 2026",
            status = "Scheduled",
            assignedProvider = "City Heart Institute",
            aiInsight = "Patient requires ground floor access or wheelchair support."
        )
    )

    val sampleDocuments = listOf(
        MedicalDocument(
            id = "DOC-901",
            patientId = "PT-1042",
            title = "Comprehensive Metabolic & Lipid Panel",
            docType = "Lab Report",
            uploadDate = "Sep 14, 2026",
            rawText = """
                APEX DIAGNOSTIC SERVICES
                Patient: Rajesh Sharma | Age: 54 | Sex: M
                Referring Clinician: Dr. Ananya Roy

                TEST RESULT REFERENCE UNITS
                ------------------------------------------------------------------
                Fasting Blood Glucose: 168 mg/dL (70 - 99) [HIGH]
                HbA1c: 8.6 % (< 5.7 Good, > 8.0 Poor Control) [HIGH]
                Serum Creatinine: 1.42 mg/dL (0.7 - 1.2) [HIGH]
                eGFR (CKD-EPI): 56 mL/min/1.73m2 (> 60 Normal) [LOW - Stage 3a]
                Blood Urea Nitrogen (BUN): 28 mg/dL (7 - 20) [HIGH]
                Total Cholesterol: 224 mg/dL (< 200) [HIGH]
                LDL Cholesterol: 142 mg/dL (< 100) [HIGH]
                HDL Cholesterol: 38 mg/dL (> 40) [LOW]
                Triglycerides: 220 mg/dL (< 150) [HIGH]
                Serum Potassium: 4.6 mEq/L (3.5 - 5.0) [NORMAL]
                Urine Albumin/Creatinine Ratio (UACR): 180 mg/g (< 30) [MODERATE MICROALBUMINURIA]
            """.trimIndent(),
            plainSummary = "Your blood sugar and 3-month sugar average (HbA1c 8.6%) are higher than recommended targets. Your kidney function test (eGFR 56) indicates mild reduction (Stage 3a kidney strain), and your cholesterol levels show elevated LDL. Potassium levels are normal.",
            hindiSummary = "आपकी 3 महीने की शुगर (HbA1c 8.6%) और खाली पेट शुगर सामान्य से अधिक है। गुर्दे (Kidney) की कार्यक्षमता (eGFR 56) में हल्का दबाव दिखाई दे रहा है, और खराब कोलेस्ट्रॉल (LDL 142) बढ़ा हुआ है। डॉक्टर से दवा बदलने और खान-पान में बदलाव पर चर्चा करें।",
            extractedEntities = "Entities: HbA1c=8.6% (Target <7%), Fasting Glucose=168 mg/dL, eGFR=56 mL/min, Creatinine=1.42 mg/dL, LDL=142 mg/dL, UACR=180 mg/g. Diagnosis markers: Uncontrolled Diabetes, CKD Stage 3a, Dyslipidemia.",
            conflictAlerts = "⚠️ Renal Dose Precaution: Patient is on Metformin. Note that eGFR is 56 mL/min (metformin requires dose adjustment when eGFR < 45 mL/min, monitor closely).",
            questionsForDoctor = "1. Should my diabetes medication dose be adjusted for my kidney numbers?\n2. Do I need a statin adjustment for cholesterol?\n3. When should I repeat the kidney and urine test?"
        ),
        MedicalDocument(
            id = "DOC-902",
            patientId = "PT-1042",
            title = "Hospital Discharge Medication Summary",
            docType = "Discharge Summary",
            uploadDate = "Sep 02, 2026",
            rawText = """
                CITY MULTISPECIALTY HOSPITAL - DISCHARGE SUMMARY
                Patient: Rajesh Sharma | IPD No: 99421
                Admit Date: Aug 28, 2026 | Discharge Date: Sep 02, 2026
                Discharge Medications:
                1. Tab Metformin 500 mg - 1 tablet twice daily after meals (BID)
                2. Tab Telmisartan 40 mg - 1 tablet once daily morning (OD)
                3. Tab Atorvastatin 20 mg - 1 tablet at bedtime (HS)
                4. Tab Amlodipine 5 mg - 1 tablet once daily morning (OD)
                Follow-up with primary physician in 10-14 days.
            """.trimIndent(),
            plainSummary = "Discharge instructions from your hospital stay. Prescribed Metformin 500mg twice daily, Telmisartan 40mg once daily, Amlodipine 5mg once daily, and Atorvastatin 20mg at bedtime.",
            hindiSummary = "अस्पताल से छुट्टी के बाद की दवाएं: मेटफॉर्मिन 500mg दिन में 2 बार भोजन के बाद, टेल्मिसर्टन 40mg सुबह 1 बार, एमलोडिपिन 5mg सुबह 1 बार, और एटोरवास्टेटिन 20mg रात को सोते समय।",
            extractedEntities = "Medications: Metformin 500mg BID, Telmisartan 40mg OD, Atorvastatin 20mg HS, Amlodipine 5mg OD. Condition: Hypertensive urgency resolved.",
            conflictAlerts = "⚠️ CONFLICT DETECTED: Prescription dated Sep 10 lists Metformin 1000 mg sustained release once daily, but this Discharge Summary lists Metformin 500 mg twice daily. Clinician confirmation required to avoid accidental double dosing.",
            questionsForDoctor = "1. What is the correct Metformin dose: 500mg twice daily or 1000mg once daily?\n2. Are Amlodipine and Telmisartan taken together in the morning?"
        ),
        MedicalDocument(
            id = "DOC-903",
            patientId = "PT-1044",
            title = "Cardiology Referral & Stent Placement Record",
            docType = "Referral Letter",
            uploadDate = "Aug 20, 2026",
            rawText = """
                HEART & VASCULAR INSTITUTE
                Patient: Sunil Varma | Age: 68 | Sex: M
                Procedure: Percutaneous Coronary Intervention (PCI) with Drug-Eluting Stent (DES) to LAD.
                Post-procedure Medications:
                - Ticagrelor 90 mg BID (Mandatory DAPT for 12 months)
                - Aspirin 75 mg OD
                - Atorvastatin 80 mg OD
                - Metoprolol Succinate 25 mg OD
                WARNING: Strict adherence to dual antiplatelet therapy. DO NOT DISCONTINUE without consulting cardiologist. Avoid NSAIDs due to bleeding hazard.
            """.trimIndent(),
            plainSummary = "Detailed record of your heart stent placement in the LAD artery. You must take both blood thinners (Ticagrelor and Aspirin) daily for 12 months without missing doses. Do not take NSAID pain medicines like Ibuprofen.",
            hindiSummary = "हार्ट स्टेंट (LAD) का विवरण। आपको दोनों खून पतला करने वाली दवाएं (Ticagrelor और Aspirin) बिना नागा पूरे 1 वर्ष तक लेनी हैं। ब्रूफेन या अन्य दर्द निवारक (NSAID) दवाएं न लें क्योंकि इससे पेट में रक्तस्राव का गंभीर खतरा हो सकता है।",
            extractedEntities = "Procedure: PCI with DES to LAD. Medications: Ticagrelor 90mg BID, Aspirin 75mg OD, Atorvastatin 80mg OD, Metoprolol 25mg OD. Critical contraindication: NSAIDs.",
            conflictAlerts = "⚠️ DAPT SAFETY ALERT: Active Dual Antiplatelet Therapy in place. Avoid all non-selective NSAIDs (Ibuprofen, Naproxen, Diclofenac). Use Paracetamol for joint pain after physician consent.",
            questionsForDoctor = "1. What safe pain relief can I take for severe knee pain instead of Ibuprofen?\n2. What signs of bruising or bleeding should I watch for?"
        )
    )

    val sampleReminders = listOf(
        MedicationReminder(
            id = 1,
            patientId = "PT-1042",
            medicationName = "Telmisartan 40 mg",
            dosage = "1 tablet with water",
            frequency = "Daily Morning",
            timeSlot = "08:00 AM",
            isTaken = true,
            instructionsHindi = "सुबह नाश्ते के बाद 1 गोली पानी के साथ लें (ब्लड प्रेशर नियंत्रण के लिए)"
        ),
        MedicationReminder(
            id = 2,
            patientId = "PT-1042",
            medicationName = "Amlodipine 5 mg",
            dosage = "1 tablet",
            frequency = "Daily Morning",
            timeSlot = "08:30 AM",
            isTaken = true,
            instructionsHindi = "सुबह 1 गोली (ब्लड प्रेशर के लिए)"
        ),
        MedicationReminder(
            id = 3,
            patientId = "PT-1042",
            medicationName = "Metformin 500 mg",
            dosage = "1 tablet immediately after dinner",
            frequency = "Daily Night",
            timeSlot = "08:30 PM",
            isTaken = false,
            instructionsHindi = "रात के भोजन के तुरंत बाद 1 गोली लें (शुगर नियंत्रण के लिए)"
        ),
        MedicationReminder(
            id = 4,
            patientId = "PT-1042",
            medicationName = "Atorvastatin 20 mg",
            dosage = "1 tablet at bedtime",
            frequency = "Daily Bedtime",
            timeSlot = "10:00 PM",
            isTaken = false,
            instructionsHindi = "रात को सोने से पहले 1 गोली लें (कोलेस्ट्रॉल के लिए)"
        )
    )

    val sampleMetrics = listOf(
        OperationalMetric(
            id = 1,
            clinicName = "CareLoop Partner Clinic (North Branch)",
            department = "Cardiometabolic OPD",
            expectedDemand = 48,
            predictedNoShowRate = "18.4% (9 patients at high risk)",
            labTurnaroundDelayRisk = "Elevated on Wednesdays (HbA1c batching delay +4 hrs)",
            recommendedStaffing = "+1 Nurse Coordinator from 09:00 to 13:00 for pre-visit vitals & phlebotomy triage",
            bottleneckInsight = "Peak registration wait time observed between 09:30 AM - 10:45 AM due to manual insurance documentation."
        ),
        OperationalMetric(
            id = 2,
            clinicName = "CareLoop Partner Clinic (North Branch)",
            department = "Diagnostic & Phlebotomy Hub",
            expectedDemand = 65,
            predictedNoShowRate = "8.2%",
            labTurnaroundDelayRisk = "Low (Average processing time: 3.2 hours)",
            recommendedStaffing = "Standard staffing adequate (2 phlebotomists, 1 lab tech)",
            bottleneckInsight = "Fasting glucose patients arriving simultaneously before 08:30 AM; staggered slot recommendation suggested."
        )
    )
}
