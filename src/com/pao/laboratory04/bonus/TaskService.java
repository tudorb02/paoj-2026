package com.pao.laboratory04.bonus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private Map<String, Task> tasksById;
    private Map<Priority, List<Task>> tasksByPriority;
    private List<String> auditLog;
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
        String id = String.format("T%03d", nextId++);
        return addTaskWithId(id, title, priority);
    }

    public Task addTaskWithId(String id, String title, Priority priority) {
        if (tasksById.containsKey(id)) {
            throw new DuplicateTaskException("Task-ul '" + id + "' există deja");
        }

        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = findTask(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = findTask(taskId);
        Status oldStatus = task.getStatus();
        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + oldStatus + " → " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return new ArrayList<>(tasksByPriority.get(priority));
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            summary.put(status, 0L);
        }

        for (Task task : tasksById.values()) {
            summary.put(task.getStatus(), summary.get(task.getStatus()) + 1);
        }

        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                result.add(task);
            }
        }
        return result;
    }

    public void printAuditLog() {
        for (String entry : auditLog) {
            System.out.println(entry);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0.0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }

    public Task findTask(String taskId) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        }
        return task;
    }
}
