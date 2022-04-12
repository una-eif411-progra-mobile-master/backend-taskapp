package edu.backend.taskapp

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
interface PriorityRepository: JpaRepository<Priority, Long>

@Repository
interface TaskRepository: JpaRepository<Task, Long> {
    fun findAllByDueDateIs(dueDate:Date) : List<Task>?
}