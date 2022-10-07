package edu.backend.taskapp

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${url.priorities}")
class PriorityController(private val priorityService: PriorityService) {

    /**
     * WS to find all elements of type Priority
     * @return A list of elements of type Priority
     */
    @GetMapping
    @ResponseBody
    fun findAll() = priorityService.findAll()

    /**
     * WS to find one Priority by the id
     * @param id to find Priority
     * @return the Priority found
     */
    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: Long) = priorityService.findById(id)
}

@RestController
@RequestMapping("\${url.tasks}")
class TaskController(private val taskService: TaskService) {
    @GetMapping
    @ResponseBody
    fun findAll() = taskService.findAll()

    @Throws(NoSuchElementException::class)
    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: Long) = taskService.findById(id)

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody taskInput: TaskInput): TaskResult? {
        return taskService.create(taskInput)
    }

    @Throws(NoSuchElementException::class)
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody taskInput: TaskInput): TaskResult? {
        return taskService.update(taskInput)
    }

    @Throws(NoSuchElementException::class)
    @DeleteMapping("{id}")
    @ResponseBody
    fun deleteById(@PathVariable id: Long) {
        taskService.deleteById(id)
    }
}
