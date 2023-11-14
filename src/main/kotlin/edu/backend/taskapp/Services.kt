package edu.backend.taskapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface PriorityService {
    /**
     * Find all Priority
     *
     * @return a list of Users
     */
    fun findAll(): List<PriorityDetails>?

    /**
     * Get one Priority by id
     *
     * @param id of the Priority
     * @return the Priority found
     */
    fun findById(id: Long): PriorityDetails?
}

@Service
class AbstractPriorityService(
    @Autowired
    val priorityRepository: PriorityRepository,

    @Autowired
    val priorityMapper: PriorityMapper,

    ) : PriorityService {
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
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): PriorityDetails? {
        val priority: Priority = priorityRepository.findById(id).orElse(null)
            ?: throw NoSuchElementException(String.format("The Priority with the id: %s not found!", id))
        return priorityMapper.priorityToPriorityDetails(priority)
    }
}

interface TaskService {
    /**
     * Find all Task
     * @return a list of Users
     */
    fun findAll(): List<TaskResult>?

    /**
     * Get one Task by id
     * @param id of the Task
     * @return the Task found
     */
    fun findById(id: Long): TaskResult?

    /**
     * Save and flush a Task entity in the database
     * @param taskInput
     * @return the user created
     */
    fun create(taskInput: TaskInput): TaskResult?

    /**
     * Update a Task entity in the database
     * @param taskInput the dto input for Task
     * @return the new Task created
     */
    fun update(taskInput: TaskInput): TaskResult?

    /**
     * Delete a Task by id from Database
     * @param id of the Task
     */
    fun deleteById(id: Long)
}

@Service
class AbstractTaskService(
    @Autowired
    val taskRepository: TaskRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val statusRepository: StatusRepository,
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
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): TaskResult? {
        val task: Task = taskRepository.findById(id).orElse(null)
            ?: throw NoSuchElementException(String.format("The Task with the id: %s not found!", id))
        return taskMapper.taskToTaskResult(task)
    }

    /**
     * Save and flush a Task entity in the database
     * @param taskInput
     * @return the user created
     */
    override fun create(taskInput: TaskInput): TaskResult? {
        val task: Task = taskMapper.taskInputToTask(taskInput)
        if (task.user == null) {
            val user = userRepository.findByEmail(LoggedUser.get()).orElse(null)
            task.user = user
        }
        if (task.status == null) {
            val status = statusRepository.findByLabel("Pending").orElse(null)
            task.status = status
        }
        return taskMapper.taskToTaskResult(
            taskRepository.save(task)
        )
    }

    /**
     * Update a Task entity in the database
     * @param taskInput the dto input for Task
     * @return the new Task created
     */
    @Throws(NoSuchElementException::class)
    override fun update(taskInput: TaskInput): TaskResult? {
        val task: Task = taskRepository.findById(taskInput.id!!).orElse(null)
            ?: throw NoSuchElementException(String.format("The Task with the id: %s not found!", taskInput.id))
        val taskUpdated: Task = task
        taskUpdated.priority = Priority()
        taskMapper.taskInputToTask(taskInput, taskUpdated)
        return taskMapper.taskToTaskResult(taskRepository.save(taskUpdated))
    }

    /**
     * Delete a Task by id from Database
     * @param id of the Task
     */
    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        taskRepository.findById(id).orElse(null)
            ?: throw NoSuchElementException(String.format("The Task with the id: %s not found!", id))

        taskRepository.deleteById(id)
    }

}

@Service
@Transactional
class AppUserDetailsService(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository,
) : UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the `UserDetails`
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never `null`)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userAuth: org.springframework.security.core.userdetails.User
        val user: User = userRepository.findByEmail(username).orElse(null)
            ?: return org.springframework.security.core.userdetails.User(
                "", "", true, true, true, true,
                getAuthorities(
                    listOf(
                        roleRepository.findByName("ROLE_USER").get()
                    )
                )
            )

        userAuth = org.springframework.security.core.userdetails.User(
            user.email, user.password, user.enabled, true, true,
            true, getAuthorities(user.roleList!!.toMutableList())
        )

        return userAuth
    }

    private fun getAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        return roles.flatMap { role ->
            sequenceOf(SimpleGrantedAuthority(role.name)) +
                    role.privilegeList.map { privilege -> SimpleGrantedAuthority(privilege.name) }
        }.toList()
    }

}
