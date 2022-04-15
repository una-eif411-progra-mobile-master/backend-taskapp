package edu.backend.taskapp

import org.mapstruct.*
import java.time.LocalDateTime

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface PriorityMapper {
    fun priorityToPriorityDetails(
        priority: Priority,
    ): PriorityDetails

    fun priorityListToPriorityDetailsList(
        priorityList: List<Priority>,
    ): List<PriorityDetails>
}

@Mapper(
    imports = [LocalDateTime::class],
    componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE
)
interface TaskMapper {
    fun taskToTaskResult(
        task: Task,
    ): TaskResult

    fun taskListToTaskListResult(
        taskList: List<Task>,
    ): List<TaskResult>

    @Mapping(target = "createDate", defaultExpression = "java(new java.util.Date())")
    fun taskInputToTask(
        taskInput: TaskInput,
    ): Task

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun taskInputToTask(dto: TaskInput, @MappingTarget task: Task)
}