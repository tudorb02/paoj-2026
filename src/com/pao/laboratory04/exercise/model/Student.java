package com.pao.laboratory04.exercise.model;

import com.pao.laboratory04.exercise.exception.InvalidGradeException;
import com.pao.laboratory04.exercise.exception.InvalidStudentException;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age) {
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Vârsta " + age + " nu este validă (18-60)");
        }

        this.name = name;
        this.age = age;
        this.grades = new EnumMap<>(Subject.class);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, double grade) {
        if (grade < 1 || grade > 10) {
            throw new InvalidGradeException("Nota " + grade + " nu este validă (1-10)");
        }
        grades.put(subject, grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (double grade : grades.values()) {
            sum += grade;
        }
        return sum / grades.size();
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age +
                ", avg=" + String.format(Locale.US, "%.2f", getAverage()) + "}";
    }
}
