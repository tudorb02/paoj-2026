package com.pao.laboratory04.exercise.service;

import com.pao.laboratory04.exercise.exception.StudentNotFoundException;
import com.pao.laboratory04.exercise.model.Student;
import com.pao.laboratory04.exercise.model.Subject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentService {
    private List<Student> students;

    private StudentService() {
        this.students = new ArrayList<>();
    }

    private static class Holder {
        private static final StudentService INSTANCE = new StudentService();
    }

    public static StudentService getInstance() {
        return Holder.INSTANCE;
    }

    public void addStudent(String name, int age) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți.");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println((i + 1) + ". " + student);
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                System.out.println("   " + entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        System.out.println("=== Top studenți ===");
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));

        for (int i = 0; i < sorted.size(); i++) {
            Student student = sorted.get(i);
            System.out.printf(Locale.US, "%d. %s — media: %.2f%n", i + 1, student.getName(), student.getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> averages = new EnumMap<>(Subject.class);

        for (Subject subject : Subject.values()) {
            double sum = 0.0;
            int count = 0;

            for (Student student : students) {
                Double grade = student.getGrades().get(subject);
                if (grade != null) {
                    sum += grade;
                    count++;
                }
            }

            if (count > 0) {
                averages.put(subject, sum / count);
            }
        }

        return averages;
    }
}
