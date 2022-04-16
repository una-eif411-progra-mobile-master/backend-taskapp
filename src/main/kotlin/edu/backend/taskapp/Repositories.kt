package edu.backend.taskapp

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PriorityRepository : JpaRepository<Priority, Long>

@Repository
interface TaskRepository : JpaRepository<Task, Long>

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName (@Param("name") name : String) : Optional<Role>
}

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(@Param("email") email : String) : Optional<User>
}
