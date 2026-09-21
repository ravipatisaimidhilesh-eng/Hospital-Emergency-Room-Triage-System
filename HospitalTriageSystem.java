import java.util.*;

class Patient {
    int id;
    String name;
    int age;
    String condition;
    int priority;

    // Constructor
    Patient(int id, String name, int age, String condition, int priority) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.condition = condition;
        this.priority = priority;
    }

    // Display patient details
    void displayPatient() {
        System.out.println("--------------------------------");
        System.out.println("Patient ID   : " + id);
        System.out.println("Name         : " + name);
        System.out.println("Age          : " + age);
        System.out.println("Condition    : " + condition);
        System.out.println("Priority     : " + priority);
    }
}

public class HospitalTriageSystem {

    static Scanner sc = new Scanner(System.in);

    // Priority Queue: smaller priority number = more urgent
    static PriorityQueue<Patient> patients =
        new PriorityQueue<>((p1, p2) -> p1.priority - p2.priority);

    static int patientId = 1;

    // Add patient
    static void addPatient() {

        System.out.print("Enter patient name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter medical condition: ");
        String condition = sc.nextLine();

        System.out.println("\nSelect Priority:");
        System.out.println("1 - Critical");
        System.out.println("2 - Serious");
        System.out.println("3 - Moderate");
        System.out.println("4 - Minor");

        System.out.print("Enter priority: ");
        int priority = sc.nextInt();
        sc.nextLine();

        if (priority < 1 || priority > 4) {
            System.out.println("Invalid priority!");
            return;
        }

        Patient p = new Patient(
            patientId++,
            name,
            age,
            condition,
            priority
        );

        patients.add(p);

        System.out.println("\nPatient added successfully!");
    }

    // Treat next patient
    static void treatPatient() {

        if (patients.isEmpty()) {
            System.out.println("\nNo patients waiting.");
            return;
        }

        Patient p = patients.poll();

        System.out.println("\nPatient selected for treatment:");
        p.displayPatient();
    }

    // Display waiting patients
    static void displayPatients() {

        if (patients.isEmpty()) {
            System.out.println("\nNo patients waiting.");
            return;
        }

        System.out.println("\n===== PATIENTS WAITING =====");

        PriorityQueue<Patient> temp =
            new PriorityQueue<>(patients);

        while (!temp.isEmpty()) {
            Patient p = temp.poll();
            p.displayPatient();
        }
    }

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n=================================");
            System.out.println("   HOSPITAL TRIAGE SYSTEM");
            System.out.println("=================================");
            System.out.println("1. Add Patient");
            System.out.println("2. Treat Next Patient");
            System.out.println("3. Display Waiting Patients");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addPatient();
                    break;

                case 2:
                    treatPatient();
                    break;

                case 3:
                    displayPatients();
                    break;

                case 4:
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);

        sc.close();
    }
}