import java.util.Scanner;


public class HospitalTriageSystem {

    static final int MAX_PATIENTS = 50;

    // 1D arrays: parallel arrays hold each patient's details
    static String[] names = new String[MAX_PATIENTS];
    static int[] ages = new int[MAX_PATIENTS];
    static int[] severities = new int[MAX_PATIENTS];
    static int count = 0;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  HOSPITAL EMERGENCY ROOM TRIAGE SYSTEM");
        System.out.println("  (Classroom simulation - fictional data)");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ", 1, 6);
            switch (choice) {
                case 1: addPatient(); break;
                case 2: displayInArrivalOrder(); break;
                case 3: displayByPriority(); break;
                case 4: showSummary(); break;
                case 5: loadSampleData(); break;
                case 6:
                    System.out.println("Exiting. Stay safe - never use this for real triage!");
                    running = false;
                    break;
            }
        }
    }

    // ---------- Menu ----------
    static void showMenu() {
        System.out.println("\n----------------- MENU -----------------");
        System.out.println("1. Add patient");
        System.out.println("2. Display patients (arrival order)");
        System.out.println("3. Display patients (by priority)");
        System.out.println("4. Priority summary");
        System.out.println("5. Load sample patients");
        System.out.println("6. Exit");
        System.out.println("Severity guide: 1 Critical | 2 Urgent | 3 Low Priority");
    }

    // ---------- Input helpers (with validation) ----------
    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("  Invalid: enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid: please enter a whole number.");
            }
        }
    }

    static String readName() {
        while (true) {
            System.out.print("Patient name: ");
            String name = sc.nextLine().trim();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("  Invalid: name cannot be empty.");
        }
    }

    // ---------- Core methods ----------
    static void addPatient() {
        if (count >= MAX_PATIENTS) {
            System.out.println("Record list is full (" + MAX_PATIENTS + ").");
            return;
        }
        String name = readName();
        int age = readInt("Age (0-120): ", 0, 120);
        int severity = readInt("Severity (1=Critical, 2=Urgent, 3=Low): ", 1, 3);
        store(name, age, severity);
        System.out.println("Added: " + name + " -> " + classify(severity));
    }

    static void store(String name, int age, int severity) {
        names[count] = name;
        ages[count] = age;
        severities[count] = severity;
        count++;
    }

    // Maps a severity value to its label using if-else
    static String classify(int severity) {
        if (severity == 1) {
            return "CRITICAL";
        } else if (severity == 2) {
            return "URGENT";
        } else if (severity == 3) {
            return "LOW PRIORITY";
        } else {
            return "UNKNOWN";
        }
    }

    static void printRecord(int i) {
        System.out.printf("Patient: %-12s | Age: %3d | Severity: %d | Priority: %s%n",
                names[i], ages[i], severities[i], classify(severities[i]));
    }

    static void displayInArrivalOrder() {
        if (count == 0) {
            System.out.println("No patient records yet.");
            return;
        }
        System.out.println("\n--- Patients (arrival order) ---");
        for (int i = 0; i < count; i++) {
            printRecord(i);
        }
    }

    // Builds an index order: Critical first, then Urgent, then Low.
    // Same-severity patients keep arrival order (stable insertion sort).
    static int[] priorityOrder() {
        int[] order = new int[count];
        for (int i = 0; i < count; i++) {
            order[i] = i;
        }
        for (int i = 1; i < count; i++) {
            int key = order[i];
            int j = i - 1;
            while (j >= 0 && severities[order[j]] > severities[key]) {
                order[j + 1] = order[j];
                j--;
            }
            order[j + 1] = key;
        }
        return order;
    }

    static void displayByPriority() {
        if (count == 0) {
            System.out.println("No patient records yet.");
            return;
        }
        int[] order = priorityOrder();
        System.out.println("\n--- Patients (Critical -> Urgent -> Low Priority) ---");
        for (int k = 0; k < order.length; k++) {
            System.out.print((k + 1) + ". ");
            printRecord(order[k]);
        }
    }

    // 2D array: rows = priority level, columns = {severity code, number of patients}
    static void showSummary() {
        int[][] summary = { {1, 0}, {2, 0}, {3, 0} };
        for (int i = 0; i < count; i++) {
            summary[severities[i] - 1][1]++;
        }
        System.out.println("\n--- Priority Summary ---");
        for (int r = 0; r < summary.length; r++) {
            System.out.println(String.format("%-13s (severity %d): %d patient(s)",
                    classify(summary[r][0]), summary[r][0], summary[r][1]));
        }
        System.out.println("Total patients: " + count);
    }

    static void loadSampleData() {
        String[] sampleNames = {"Asha", "Kiran", "Dev", "Meera", "Rohan"};
        int[] sampleAges = {45, 30, 22, 67, 8};
        int[] sampleSev = {1, 2, 3, 1, 2};
        int added = 0;
        for (int i = 0; i < sampleNames.length && count < MAX_PATIENTS; i++) {
            store(sampleNames[i], sampleAges[i], sampleSev[i]);
            added++;
        }
        System.out.println("Loaded " + added + " sample patients.");
    }
}