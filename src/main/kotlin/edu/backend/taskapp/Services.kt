package edu.backend.taskapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface PriorityService {
    /**
     * Find all Priority
     *
     * @return a list of Users
     */
    fun findAll() : List<PriorityDetails> ?

    /**
     * Get one Priority by id
     *
     * @param id of the Priority
     * @return the Priority found
     */
    fun findById(id : Long) : PriorityDetails ?
}

@Service
class AbstractPriorityService (
    @Autowired
    val priorityRepository : PriorityRepository,

    @Autowired
    val priorityMapper: PriorityMapper

): PriorityService {
    /**
     * Find all Priority
     *
     * @return a list of Users
     */
    override fun findAll(): List<PriorityDetails>? {
        return priorityMapper.priorityListToPriorityDetailsList(
            priorityRepository.findAll()
        )
    }

    /**
     * Get one Priority by id
     *
     * @param id of the Priority
     * @return the Priority found
     */
    override fun findById(id: Long): PriorityDetails? {
        val priority : Optional<Priority> = priorityRepository.findById(id)
        return priorityMapper.priorityToPriorityDetails(
            priority.get(),
        )
    }
}

interface TaskService {
    /**
     * Find all Task
     * @return a list of Users
     */
    fun findAll() : List<TaskResult> ?

    /**
     * Get one Task by id
     * @param id of the Task
     * @return the Task found
     */
    fun findById(id : Long) : TaskResult ?

    /**
     * Save and flush a Task entity in the database
     * @param taskInput
     * @return the user created
     */
    fun create(taskInput: TaskInput) : TaskResult ?

    /**
     * Update a Task entity in the database
     * @param taskInput the dto input for Task
     * @return the new Task created
     */
    fun update(taskInput: TaskInput) : TaskResult ?

    /**
     * Delete a Task by id from Database
     * @param id of the Task
     */
    fun deleteById(id : Long)
}

@Service
class AbstractTaskService (
    @Autowired
    val taskRepository: TaskRepository,
    @Autowired
    val taskMapper: TaskMapper,
        ) : TaskService {
    /**
     * Find all Task
     * @return a list of Users
     */
    override fun findAll(): List<TaskResult>? {
        return taskMapper.taskListToTaskListResult(
            taskRepository.findAll()
        )
    }

    /**
     * Get one Task by id
     * @param id of the Task
     * @return the Task found
     */
    override fun findById(id: Long): TaskResult? {
        val task : Optional<Task> = taskRepository.findById(id)
        return taskMapper.taskToTaskResult(
            task.get(),
        )
    }

    /**
     * Save and flush a Task entity in the database
     * @param taskInput
     * @return the user created
     */
    override fun create(taskInput: TaskInput): TaskResult? {
        val task : Task = taskMapper.taskInputToTask(taskInput)
        return taskMapper.taskToTaskResult(
            taskRepository.save(task)
        )
    }

    /**
     * Update a Task entity in the database
     * @param taskInput the dto input for Task
     * @return the new Task created
     */
    override fun update(taskInput: TaskInput): TaskResult? {
        val task : Optional<Task> = taskRepository.findById(taskInput.id!!)
        val taskUpdated : Task = task.get()
        taskMapper.taskInputToTask(taskInput, taskUpdated)
        return taskMapper.taskToTaskResult(taskRepository.save(taskUpdated))
    }

    /**
     * Delete a Task by id from Database
     * @param id of the Task
     */
    override fun deleteById(id: Long) {
        if (!taskRepository.findById(id).isEmpty){
            taskRepository.deleteById(id)
        }
    }

}