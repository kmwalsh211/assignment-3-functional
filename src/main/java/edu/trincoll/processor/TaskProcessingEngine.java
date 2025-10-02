package edu.trincoll.processor;

import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.functional.TaskProcessor;
import edu.trincoll.functional.TaskTransformer;
import edu.trincoll.model.Task;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * TaskProcessingEngine provides a collection of utilities to process {@link Task}
 * objects using Java functional programming constructs such as {@link Function},
 * {@link Supplier}, {@link Consumer}, {@link BiFunction}, and custom functional
 * interfaces.
 *
 * <p>This class demonstrates various paradigms including lazy evaluation,
 * side-effects, transformation, filtering, batch processing, optional chaining,
 * and comparator composition for sorting.</p>
 */
public class TaskProcessingEngine {

    /**
     * Applies a sequence of transformation operations to a list of tasks using
     * function composition. Each function in {@code operations} takes a list of
     * tasks and produces a new list.
     *
     * @param tasks      the initial list of tasks
     * @param operations the pipeline of functions to apply in order
     * @return the final transformed list of tasks
     */
    public List<Task> processPipeline(List<Task> tasks, List<Function<List<Task>, List<Task>>> operations) {
        Function<List<Task>, List<Task>> pipeline = operations.stream()
                .reduce(Function.identity(), Function::andThen);
        return pipeline.apply(tasks);
    }


    /**
     * Returns the task if present; otherwise generates a default task using the
     * supplied {@link Supplier}.
     *
     * @param taskOpt         optional containing a task or empty
     * @param defaultSupplier supplier used to lazily create a default task
     * @return the existing task or a default task if none is present
     */
    public Task getOrCreateDefault(Optional<Task> taskOpt, Supplier<Task> defaultSupplier) {
        return taskOpt.orElseGet(defaultSupplier);
    }

    /**
     * Processes each task with the given side-effect operation. Side effects may
     * include logging, printing, or updating external state.
     *
     * @param tasks      the list of tasks to process
     * @param sideEffect the side-effecting operation to apply to each task
     */
    public void processTasksWithSideEffects(List<Task> tasks, Consumer<Task> sideEffect) {
        tasks.forEach(sideEffect);
    }

    /**
     * Merges two tasks into a new task using the provided merger function.
     *
     * @param task1  the first task
     * @param task2  the second task
     * @param merger a function that merges two tasks into one
     * @return the merged task
     */
    public Task mergeTasks(Task task1, Task task2, BiFunction<Task, Task, Task> merger) {
        return merger.apply(task1, task2);
    }

    /**
     * Applies a transformation to all tasks using a {@link UnaryOperator}.
     *
     * @param tasks       the list of tasks to transform
     * @param transformer the operator to apply to each task
     * @return a new list containing all transformed tasks
     */
    public List<Task> transformAll(List<Task> tasks, UnaryOperator<Task> transformer) {
        return tasks.stream()
                .map(transformer)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Filters and then transforms a list of tasks using custom functional
     * interfaces: {@link TaskPredicate} and {@link TaskTransformer}.
     *
     * @param tasks       the list of tasks
     * @param filter      predicate for filtering tasks
     * @param transformer transformer for mapping tasks
     * @return a new list of tasks after filtering and transformation
     */
    public List<Task> filterAndTransform(
            List<Task> tasks,
            TaskPredicate filter,
            TaskTransformer transformer) {
        return tasks.stream()
                .filter(filter)
                .map(transformer)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Processes tasks in batches using a {@link TaskProcessor}. Each batch is a
     * sublist of the original list.
     *
     * @param tasks     the list of tasks
     * @param batchSize the maximum size of each batch
     * @param processor the processor that consumes each batch
     */
    public void batchProcess(
            List<Task> tasks,
            int batchSize,
            TaskProcessor processor) {
        for (int i = 0; i < tasks.size(); i += batchSize) {
            int end = Math.min(i + batchSize, tasks.size());
            processor.process(tasks.subList(i, end));
        }
    }

    /**
     * Finds the title of the task with the highest priority.
     *
     * @param tasks the list of tasks
     * @return an {@link Optional} containing the highest-priority task's title,
     * or empty if no tasks exist
     */
    public Optional<String> getHighestPriorityTaskTitle(List<Task> tasks) {
        return tasks.stream()
                .max(Comparator.comparing(Task::priority))
                .map(Task::title);
    }

    /**
     * Creates an infinite stream of tasks generated by the given supplier.
     *
     * @param taskSupplier the supplier that generates new tasks
     * @return an infinite stream of tasks
     */
    public Stream<Task> generateTaskStream(Supplier<Task> taskSupplier) {
        return Stream.generate(taskSupplier);
    }

    /**
     * Sorts tasks using multiple comparators composed in order. The comparators
     * are chained using {@link Comparator#thenComparing(Comparator)}.
     *
     * @param tasks       the list of tasks to sort
     * @param comparators the list of comparators to apply in order
     * @return a new sorted list of tasks
     */
    public List<Task> sortByMultipleCriteria(
            List<Task> tasks,
            List<Comparator<Task>> comparators) {
        return tasks.stream()
                .sorted(comparators.stream().reduce((a, b) -> 0, Comparator::thenComparing))
                .collect(java.util.stream.Collectors.toList());
    }
}