import java.util.*;

/**
 * Hospital Emergency Room Triage System
 * Implements the 8-step triage workflow and the 5-level
 * Emergency Severity Index (ESI) acuity classification.
 */
public class ERTriageSystem {

    // ---------- ESI Acuity Levels (Slide 5) ----------
    enum ESILevel {
        LEVEL_1(1, "Resuscitation", "Cardiac arrest, no pulse", "Immediate"),
        LEVEL_2(2, "Emergent", "Chest pain, stroke symptoms", "Within 10 min"),
        LEVEL_3(3, "Urgent", "Moderate abdominal pain, fractures", "Within 30 min"),
        LEVEL_4(4, "Less Urgent", "Sprains, minor infections", "Within 60 min"),
        LEVEL_5(5, "Non-Urgent", "Cold symptoms, prescription refill", "Within 120 min");

        final int level;
        final String category;
        final String example;
        final String targetTime;

        ESILevel(int level, String category, String example, String targetTime) {
            this.level = level;
            this.category = category;
            this.example = example;
            this.targetTime = targetTime;
        }

        @Override
        public String toString() {
            return "ESI " + level + " (" + category + ") - Target: " + targetTime;
        }
    }

    // ---------- Patient / Vitals model ----------
    static class Patient {
        String name;
        int age;
        String chiefComplaint;

        // Visual assessment flags (Step 2)
        boolean unresponsive;
        boolean notBreathing;
        boolean severeBleeding;

        // Vitals (Step 4)
        int heartRate;          // bpm
        int systolicBP;         // mmHg
        int respiratoryRate;    // breaths/min
        int oxygenSaturation;   // %
        double temperatureF;
        int painScore;          // 0-10

        String disposition;     // Step 7 result
        ESILevel esiLevel;      // Step 5 result
        List<String> log = new ArrayList<>(); // Step 6 documentation

        Patient(String name, int age, String chiefComplaint) {
            this.name = name;
            this.age = age;
            this.chiefComplaint = chiefComplaint;
        }
    }

    // ---------- Step 1: Presentation and Registration ----------
    static void registerPatient(Patient p) {
        p.log.add("Step 1 - Registration: " + p.name + " (age " + p.age
                + ") checked in, complaint: \"" + p.chiefComplaint + "\"");
    }

    // ---------- Step 2: Rapid Visual Assessment ----------
    static boolean visualAssessment(Patient p) {
        boolean immediateFlag = p.unresponsive || p.notBreathing || p.severeBleeding;
        p.log.add("Step 2 - Visual Assessment: " +
                (immediateFlag ? "IMMEDIATE RESUSCITATION FLAG TRIGGERED" : "No acute distress observed"));
        return immediateFlag;
    }

    // ---------- Step 3: Chief Complaint and History ----------
    static void takeHistory(Patient p) {
        p.log.add("Step 3 - History: Reviewed onset, allergies, medications, existing conditions for \""
                + p.chiefComplaint + "\"");
    }

    // ---------- Step 4: Vital Signs Measurement ----------
    static void recordVitals(Patient p, int heartRate, int systolicBP, int respRate,
                              int o2Sat, double tempF, int painScore) {
        p.heartRate = heartRate;
        p.systolicBP = systolicBP;
        p.respiratoryRate = respRate;
        p.oxygenSaturation = o2Sat;
        p.temperatureF = tempF;
        p.painScore = painScore;
        p.log.add(String.format(
                "Step 4 - Vital Signs: HR=%d, SBP=%d, RR=%d, O2Sat=%d%%, Temp=%.1fF, Pain=%d/10",
                heartRate, systolicBP, respRate, o2Sat, tempF, painScore));
    }

    // ---------- Step 5: Acuity Level Assignment (ESI logic) ----------
    static ESILevel assignAcuityLevel(Patient p, boolean immediateFlag) {
        ESILevel level;

        if (immediateFlag || p.heartRate == 0 || p.oxygenSaturation < 85) {
            level = ESILevel.LEVEL_1;
        } else if (p.oxygenSaturation < 92 || p.systolicBP < 90
                || p.chiefComplaint.toLowerCase().contains("chest pain")
                || p.chiefComplaint.toLowerCase().contains("stroke")) {
            level = ESILevel.LEVEL_2;
        } else if (p.painScore >= 6
                || p.chiefComplaint.toLowerCase().contains("fracture")
                || p.chiefComplaint.toLowerCase().contains("abdominal")) {
            level = ESILevel.LEVEL_3;
        } else if (p.chiefComplaint.toLowerCase().contains("sprain")
                || p.chiefComplaint.toLowerCase().contains("infection")) {
            level = ESILevel.LEVEL_4;
        } else {
            level = ESILevel.LEVEL_5;
        }

        p.esiLevel = level;
        p.log.add("Step 5 - Acuity Level Assigned: " + level);
        return level;
    }

    // ---------- Step 6: Documentation ----------
    static void documentFindings(Patient p) {
        p.log.add("Step 6 - Documentation: Findings logged for treating team");
    }

    // ---------- Step 7: Disposition / Routing ----------
    static void routePatient(Patient p) {
        String destination;
        switch (p.esiLevel.level) {
            case 1: destination = "Resuscitation Bay"; break;
            case 2: destination = "Urgent Care (high priority)"; break;
            case 3: destination = "Urgent Care"; break;
            case 4: destination = "Fast-Track"; break;
            default: destination = "General Waiting Room";
        }
        p.disposition = destination;
        p.log.add("Step 7 - Routing: Directed to " + destination);
    }

    // ---------- Step 8: Reassessment ----------
    static void reassess(Patient p, int waitedMinutes) {
        p.log.add("Step 8 - Reassessment: Status re-checked after " + waitedMinutes
                + " min in " + p.disposition);
    }

    // ---------- Runs the full 8-step workflow for one patient ----------
    static void runTriageWorkflow(Patient p, int heartRate, int systolicBP, int respRate,
                                   int o2Sat, double tempF, int painScore, int reassessAfterMin) {
        registerPatient(p);
        boolean immediateFlag = visualAssessment(p);
        takeHistory(p);
        recordVitals(p, heartRate, systolicBP, respRate, o2Sat, tempF, painScore);
        assignAcuityLevel(p, immediateFlag);
        documentFindings(p);
        routePatient(p);
        reassess(p, reassessAfterMin);
    }

    static void printSummary(Patient p) {
        System.out.println("=======================================================");
        System.out.println("Patient: " + p.name + " | Complaint: " + p.chiefComplaint);
        for (String entry : p.log) {
            System.out.println("  " + entry);
        }
        System.out.println("RESULT -> " + p.esiLevel + " | Routed to: " + p.disposition);
        System.out.println();
    }

    // ---------- Demo: 5 sample patients matching the ESI table examples ----------
    public static void main(String[] args) {
        System.out.println("HOSPITAL EMERGENCY ROOM TRIAGE SYSTEM");
        System.out.println("Acuity scale simulation for ED staff and triage nurses\n");

        Patient p1 = new Patient("John Doe", 67, "Cardiac arrest");
        p1.unresponsive = true;
        p1.notBreathing = true;
        runTriageWorkflow(p1, 0, 0, 0, 70, 97.0, 0, 0);
        printSummary(p1);

        Patient p2 = new Patient("Maria Alvarez", 54, "Chest pain, stroke symptoms");
        runTriageWorkflow(p2, 118, 88, 24, 91, 99.1, 8, 8);
        printSummary(p2);

        Patient p3 = new Patient("Sam Carter", 30, "Moderate abdominal pain");
        runTriageWorkflow(p3, 95, 122, 18, 97, 99.4, 7, 25);
        printSummary(p3);

        Patient p4 = new Patient("Priya Nair", 22, "Ankle sprain");
        runTriageWorkflow(p4, 78, 118, 16, 99, 98.6, 4, 50);
        printSummary(p4);

        Patient p5 = new Patient("Tom Lee", 40, "Cold symptoms, prescription refill");
        runTriageWorkflow(p5, 72, 120, 14, 99, 98.2, 1, 100);
        printSummary(p5);
    }
}
