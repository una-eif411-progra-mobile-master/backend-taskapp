package edu.backend.taskapp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/import-users.sql", "/import-priorities.sql", "/import-status.sql", "/import-tasks.sql", "/import-reminders.sql")
/**
 * This class will load the initial data into the database
 */
class LoadInitData (
    @Autowired
    val taskRepository: TaskRepository,
) {

    @Test
    fun testTaskFindAll() {
        val taskList: List<Task> = taskRepository.findAll()
        Assertions.assertTrue(taskList.size == 2)
    }
}