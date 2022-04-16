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
        val priority: Optional<Priority> = priorityRepository.findById(id)
        if (priority.isEmpty) {
            throw NoSuchElementException(String.format("The Priority with the id: %s not found!", id))
        }
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
        val task: Optional<Task> = taskRepository.findById(id)
        if (task.isEmpty) {
            throw NoSuchElementException(String.format("The Task with the id: %s not found!", id))
        }
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
        val task: Task = taskMapper.taskInputToTask(taskInput)
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
        val task: Optional<Task> = taskRepository.findById(taskInput.id!!)
        if (task.isEmpty) {
            throw NoSuchElementException(String.format("The Task with the id: %s not found!", taskInput.id))
        }
        val taskUpdated: Task = task.get()
        taskMapper.taskInputToTask(taskInput, taskUpdated)
        return taskMapper.taskToTaskResult(taskRepository.save(taskUpdated))
    }

    /**
     * Delete a Task by id from Database
     * @param id of the Task
     */
    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        if (!taskRepository.findById(id).isEmpty) {
            taskRepository.deleteById(id)
        } else {
            throw NoSuchElementException(String.format("The Task with the id: %s not found!", id))
        }
    }

}

@Service
@Transactional
class AppUserDetailsService(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val roleMapper: RoleMapper,
) : UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
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
        var userAuth: org.springframework.security.core.userdetails.User? = null
        val user: Optional<User> = userRepository.findByEmail(username)
        if (user.isEmpty) {
            return org.springframework.security.core.userdetails.User(
                "", "", true, true, true, true,
                getAuthorities(Arrays.asList(
                    roleRepository.findByName("ROLE_USER").get())))
        }

        userAuth = org.springframework.security.core.userdetails.User(
            user.get().email, user.get().password, user.get().enabled, true, true,
            true, getAuthorities(user.get().roleList!!.toMutableList()))

        return userAuth
    }

    private fun getAuthorities(
        roles: MutableList<Role>,
    ): Collection<GrantedAuthority?>? {
        return getGrantedAuthorities(getPrivileges(roles))
    }

    private fun getPrivileges(roles: MutableList<Role>?): List<String> {
        val privileges: MutableList<String> = ArrayList()
        val collection: MutableList<Privilege> = ArrayList()
        if (roles != null) {
            for (role in roles) {
                collection.addAll(role.privilegeList)
            }
        }
        for (item in collection) {
            privileges.add(item.name)
        }
        return privileges
    }

    private fun getGrantedAuthorities(privileges: List<String>): List<GrantedAuthority?>? {
        val authorities: MutableList<GrantedAuthority?> = ArrayList()
        for (privilege in privileges) {
            authorities.add(SimpleGrantedAuthority(privilege))
        }
        return authorities
    }

}