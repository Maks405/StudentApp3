package student_performance;

import java.io.*;
import java.util.*;

public class JournalApp {
    private static final String FILE_NAME = "data.txt";
    private static Scanner scanner = new Scanner(System.in, "UTF-8");
    private static Group group = new Group();

    public static void main(String[] args) {
        loadFromFile(group);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nВиберіть дію:");
            System.out.println("1 - Додати студента");
            System.out.println("2 - Додати дисципліну студенту");
            System.out.println("3 - Виставити оцінку студенту");
            System.out.println("4 - Переглянути успішність студента");
            System.out.println("5 - Переглянути звіт групи");
            System.out.println("6 - Видалення");
            System.out.println("7 - Переглянути всі оцінки всіх студентів");
            System.out.println("8 - Запустити графічний інтерфейс");
            System.out.println("9 - Зберегти дані вручну");
            System.out.println("0 - Вийти");
            System.out.print("Ваш вибір: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addDisciplineToStudent();
                case 3 -> addGradeToStudent();
                case 4 -> viewStudentPerformance();
                case 5 -> viewGroupReport();
                case 6 -> deleteMenu();
                case 7 -> viewAllGrades();
                case 8 -> JournalAppGUI.launch(group);
                case 9 -> {
                    saveToFile(group);
                    System.out.println("Дані збережено вручну.");
                }
                case 0 -> {
                    saveToFile(group);
                    exit = true;
                    System.out.println("Вихід з програми. Дані збережено.");
                }
                default -> System.out.println("Некоректний вибір.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Введіть прізвище, ім'я та (за бажанням) по батькові студента: ");
        String fullName = scanner.nextLine().trim();
        String[] parts = fullName.split("\\s+");
        if (parts.length < 2 || parts.length > 3) {
            System.out.println("Помилка: Ім’я повинно містити від 2 до 3 слів.");
            return;
        }
        Student student = new Student(fullName);
        group.addStudent(student);
        System.out.println("Студента додано.");
    }

    private static Student findStudent() {
        System.out.print("Введіть прізвище та ім'я студента: ");
        String fullName = scanner.nextLine();
        Student student = group.findStudent(fullName);
        if (student == null) {
            System.out.println("Студент не знайдений.");
        }
        return student;
    }

    private static void addDisciplineToStudent() {
        Student student = findStudent();
        if (student == null) return;

        System.out.print("Введіть назву дисципліни: ");
        String disciplineName = scanner.nextLine();
        Discipline discipline = new Discipline(disciplineName);
        student.addDiscipline(discipline);
        System.out.println("Дисципліну додано студенту.");
    }

    private static void addGradeToStudent() {
        Student student = findStudent();
        if (student == null) return;

        System.out.print("Введіть назву дисципліни: ");
        String disciplineName = scanner.nextLine();
        Discipline discipline = student.getDiscipline(disciplineName);
        if (discipline == null) {
            System.out.println("Дисципліна не знайдена у цього студента.");
            return;
        }
        System.out.print("Введіть оцінку (0-100): ");
        int grade = scanner.nextInt();
        scanner.nextLine();
        if (grade < 0 || grade > 100) {
            System.out.println("Оцінка повинна бути від 0 до 100.");
            return;
        }
        discipline.addGrade(grade);
        System.out.println("Оцінку додано.");
    }

    private static void viewStudentPerformance() {
        Student student = findStudent();
        if (student == null) return;

        System.out.println("Успішність студента " + student.getFullName() + ":");
        for (Discipline d : student.getDisciplines()) {
            System.out.printf("- %s: %.2f (середній бал)%n", d.getName(), d.getAverageGrade());
        }
        System.out.printf("Середній бал по всіх дисциплінах: %.2f%n", student.getAverageGrade());
    }

    private static void viewGroupReport() {
        System.out.println("Звіт по групі:");
        for (Student s : group.getStudents()) {
            System.out.printf("%s - Середній бал: %.2f%n", s.getFullName(), s.getAverageGrade());
        }
        System.out.printf("Середній бал по групі: %.2f%n", group.getGroupAverage());
    }

    private static void deleteMenu() {
        System.out.println("Виберіть варіант видалення:");
        System.out.println("1 - Видалити одного студента");
        System.out.println("2 - Видалити всіх студентів");
        System.out.println("3 - Видалити всі оцінки одного студента");
        System.out.println("4 - Видалити всі оцінки всіх студентів");
        System.out.println("5 - Видалити одну конкретну оцінку");
        System.out.print("Ваш вибір: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                Student s = findStudent();
                if (s != null) {
                    group.getStudents().remove(s);
                    System.out.println("Студента видалено.");
                }
            }
            case 2 -> {
                group.getStudents().clear();
                System.out.println("Усіх студентів видалено.");
            }
            case 3 -> {
                Student s = findStudent();
                if (s != null) {
                    for (Discipline d : s.getDisciplines()) {
                        d.clearGrades();
                    }
                    System.out.println("Усі оцінки видалено у цього студента.");
                }
            }
            case 4 -> {
                for (Student s : group.getStudents()) {
                    for (Discipline d : s.getDisciplines()) {
                        d.clearGrades();
                    }
                }
                System.out.println("Оцінки всіх студентів видалено.");
            }
            case 5 -> deleteOneSpecificGrade();
            default -> System.out.println("Некоректний вибір.");
        }
    }

    private static void deleteOneSpecificGrade() {
        Student student = findStudent();
        if (student == null) return;

        System.out.print("Введіть назву дисципліни: ");
        String disciplineName = scanner.nextLine();
        Discipline discipline = student.getDiscipline(disciplineName);
        if (discipline == null) {
            System.out.println("Дисципліна не знайдена.");
            return;
        }

        System.out.print("Введіть значення оцінки, яку потрібно видалити: ");
        int targetGrade = scanner.nextInt();
        scanner.nextLine();

        boolean removed = discipline.removeOneGrade(targetGrade);
        if (removed) {
            System.out.println("Оцінку " + targetGrade + " видалено.");
        } else {
            System.out.println("Оцінка не знайдена.");
        }
    }

    private static void viewAllGrades() {
        if (group.getStudents().isEmpty()) {
            System.out.println("Група порожня.");
            return;
        }
        for (Student s : group.getStudents()) {
            System.out.println("\n" + s.getFullName() + ":");
            for (Discipline d : s.getDisciplines()) {
                System.out.print(" - " + d.getName() + ": ");
                List<Integer> grades = d.getGrades();
                if (grades.isEmpty()) {
                    System.out.println("немає оцінок");
                } else {
                    for (int g : grades) {
                        System.out.print(g + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    public static void saveToFile(Group groupToSave) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : groupToSave.getStudents()) {
                writer.println(s.getFullName());
                for (Discipline d : s.getDisciplines()) {
                    writer.print(d.getName() + ":");
                    for (int g : d.getGrades()) {
                        writer.print(g + ",");
                    }
                    writer.println();
                }
                writer.println("---");
            }
        } catch (IOException e) {
            System.out.println("Помилка збереження у файл: " + e.getMessage());
        }
    }

    public static void loadFromFile(Group groupToLoad) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            Student currentStudent = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (currentStudent != null) {
                        groupToLoad.addStudent(currentStudent);
                        currentStudent = null;
                    }
                    continue;
                }
                if (currentStudent == null) {
                    currentStudent = new Student(line);
                } else {
                    String[] parts = line.split(":");
                    Discipline d = new Discipline(parts[0]);
                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        for (String g : parts[1].split(",")) {
                            if (!g.isEmpty()) d.addGrade(Integer.parseInt(g));
                        }
                    }
                    currentStudent.addDiscipline(d);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
