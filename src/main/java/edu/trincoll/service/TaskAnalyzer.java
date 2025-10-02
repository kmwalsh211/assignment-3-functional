package edu.trincoll.service;

import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AI Collaboration Report:
 * Tool: Gemini Code Agent
 *
 * Most Helpful Prompts:
 * 1. "Explain why ____ specific test is failing"
 * 2. "Explain Optional"
 * 3. "Show me a cleaner way to write ____ method"
 *
 * Concepts Learned:
 * - Key stream methods stream(), filter(), map(), flatmap(), collect(), etc.
 * - The Optional type
 * - Lambdas
 * - Higher order functions
 *
 * Team: Kayla Walsh McCarter, AJ Mitchell, Saqlain Anjum
 */
public class TaskAnalyzer {
    private final List<Task> tasks;

    public TaskAnalyzer(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Filters tasks based on the provided predicate using streams.
     *
     * @param predicate condition to filter tasks
     * @return list of tasks that match the predicate
     */
    public List<Task> filterTasks(Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Finds a task by its ID using Optional.
     *
     * @param id task ID
     * @return an Optional containing the task if found, otherwise empty
     */
    public Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }

    /**
     * Retrieves the top priority tasks, sorted in descending order of priority.
     *
     * @param limit maximum number of tasks to return
     * @return list of top priority tasks
     */
    public List<Task> getTopPriorityTasks(int limit) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::priority).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Groups tasks by their status.
     *
     * @return map of task status to list of tasks
     */
    public Map<Task.Status, List<Task>> groupByStatus() {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::status));
    }

    /**
     * Partitions tasks based on whether they are overdue.
     *
     * @return map with true for overdue tasks and false for non-overdue tasks
     */
    public Map<Boolean, List<Task>> partitionByOverdue() {
        return tasks.stream()
                .collect(Collectors.partitioningBy(Task::isOverdue));
    }

    /**
     * Retrieves all unique tags across all tasks.
     *
     * @return set of unique task tags
     */
    public Set<String> getAllUniqueTags() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .collect(Collectors.toSet());
    }

    /**
     * Calculates the total estimated hours for all tasks using reduce.
     *
     * @return optional containing total estimated hours if present
     */
    public Optional<Integer> getTotalEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .reduce(Integer::sum);
    }

    /**
     * Computes the average estimated hours for all tasks.
     *
     * @return optional double representing the average estimated hours
     */
    public OptionalDouble getAverageEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average();
    }

    /**
     * Retrieves all task titles.
     *
     * @return list of task titles
     */
    public List<String> getTaskTitles() {
        return tasks.stream()
                .map(Task::title)
                .collect(Collectors.toList());
    }

    /**
     * Filters tasks using a custom functional predicate.
     *
     * @param predicate custom predicate for filtering tasks
     * @return list of tasks that satisfy the predicate
     */
    public List<Task> filterWithCustomPredicate(TaskPredicate predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all tags across tasks and sorts them.
     *
     * @return sorted list of task tags
     */
    public List<String> getAllTagsSorted() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Counts tasks by their priority.
     *
     * @return map of task priority to count
     */
    public Map<Task.Priority, Long> countTasksByPriority() {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::priority, Collectors.counting()));
    }

    /**
     * Generates a summary string for a task given its ID.
     *
     * @param taskId ID of the task
     * @return summary string containing task title and status, or "Task not found"
     */
    public String getTaskSummary(Long taskId) {
        return findTaskById(taskId)
                .map(task -> String.format("%s - %s", task.title(), task.status()))
                .orElse("Task not found");
    }

    /**
     * Checks if there are any overdue tasks.
     *
     * @return true if any task is overdue, otherwise false
     */
    public boolean hasOverdueTasks() {
        return tasks.stream().anyMatch(Task::isOverdue);
    }

    /**
     * Checks if all tasks are assigned (i.e., not in TODO status).
     *
     * @return true if all tasks are assigned, otherwise false
     */
    public boolean areAllTasksAssigned() {
        return tasks.stream().allMatch(task -> task.status() != Task.Status.TODO);
    }
}