package com.pao.laboratory03.bonus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int nextId;

    private TaskService() {
        this.tasksById = new LinkedHashMap<>();
        this.tasksByPriority = new EnumMap<>(Priority.class);
        this.auditLog = new ArrayList<>();
        this.nextId = 1;

        for (Priority priority : Priority.values()) {
            tasksByPriority.put(priority, new ArrayList<>());
        }
    }

    private static class Holder {
        private static final TaskService INSTANCE = new TaskService();
    }

    public static TaskService getInstance() {
        return Holder.INSTANCE;
    }

    public Task addTask(String title, Priority priority) {
        Task task = new Task(generateId(), title, priority);
        addTask(task);
        return task;
    }

    public Task addTask(Task task) {
        if (tasksById.containsKey(task.getId())) {
            throw new DuplicateTaskException("Task-ul cu id-ul '" + task.getId() + "' există deja");
        }

        tasksById.put(task.getId(), task);
        tasksByPriority.get(task.getPriority()).add(task);
        auditLog.add("[ADD] " + task.getId() + ": '" + task.getTitle() + "' (" + task.getPriority() + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findTaskById(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findTaskById(taskId);
        Status oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }

    public Task findTaskById(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return task;
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.getOrDefault(priority, List.of()));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new LinkedHashMap<>();
        for (Status status : Status.values()) {
            long count = tasksById.values().stream()
                    .filter(task -> task.getStatus() == status)
                    .count();
            summary.put(status, count);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                unassigned.add(task);
            }
        }
        return unassigned;
    }

    public void printAuditLog() {
        for (String entry : auditLog) {
            System.out.println(entry);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }

    private String generateId() {
        return String.format("T%03d", nextId++);
    }
}
