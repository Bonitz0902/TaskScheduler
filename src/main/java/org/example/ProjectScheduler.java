package org.example;

import java.time.LocalDate;
import java.util.*;

public class ProjectScheduler {
    private final Map<String, Task> taskMap;
    private final Map<String, List<String>> graph;
    private final Map<String, Integer> inDegree;
    private final List<Task> tasks;

    public ProjectScheduler(List<Task> tasks) {
        this.tasks = tasks;
        this.taskMap = new HashMap<>();
        this.graph = new HashMap<>();
        this.inDegree = new HashMap<>();
        initialize();
    }

    private void initialize() {
        for (Task task : tasks) {
            taskMap.put(task.name, task);
            graph.put(task.name, new ArrayList<>());
            inDegree.put(task.name, 0);
        }

        for (Task task : tasks) {
            for (String dependency : task.dependencies) {
                graph.get(dependency).add(task.name);
                inDegree.put(task.name, inDegree.get(task.name) + 1);
            }
        }
    }

    public List<Task> scheduleTasks(LocalDate projectStartDate) {
        List<Task> scheduledTasks = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        for (String taskName : inDegree.keySet()) {
            if (inDegree.get(taskName) == 0) {
                queue.add(taskName);
                taskMap.get(taskName).setSchedule(projectStartDate);
            }
        }

        while (!queue.isEmpty()) {
            String currentTaskName = queue.poll();
            Task currentTask = taskMap.get(currentTaskName);
            scheduledTasks.add(currentTask);

            for (String dependent : graph.get(currentTaskName)) {
                inDegree.put(dependent, inDegree.get(dependent) - 1);

                if (inDegree.get(dependent) == 0) {
                    Task dependentTask = taskMap.get(dependent);
                    LocalDate dependentStartDate = currentTask.endDate.plusDays(1);
                    dependentTask.setSchedule(dependentStartDate);
                    queue.add(dependent);
                }
            }
        }

        if (scheduledTasks.size() != tasks.size()) {
            throw new IllegalStateException("Cycle detected in task dependencies!");
        }

        return scheduledTasks;
    }

    public static void main(String[] args) {
        ProjectScheduler scheduler = getProjectScheduler();

        LocalDate projectStartDate = LocalDate.now();
        List<Task> scheduledTasks = scheduler.scheduleTasks(projectStartDate);

        System.out.println("Project Schedule:");
        for (Task task : scheduledTasks) {
            System.out.println(task);
        }
    }

    private static ProjectScheduler getProjectScheduler() {
        List<Task> tasks = Arrays.asList(
                new Task("Task A", 3, Collections.emptyList()),
                new Task("Task B", 2, List.of("Task A")),
                new Task("Task C", 4, List.of("Task A")),
                new Task("Task D", 1, Arrays.asList("Task B", "Task C"))
        );

        return new ProjectScheduler(tasks);
    }
}