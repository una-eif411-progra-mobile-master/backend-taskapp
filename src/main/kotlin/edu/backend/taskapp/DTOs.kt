package edu.backend.taskapp

import java.util.*

data class PriorityDetails(
    var id:Long?,
    var label: String,
)

data class PrivilegeResult (
    var id: Long? = null,
    var name: String? = null
)

data class RoleResult (
    var id: Long?,
    var name: String?,
    var privileges: List<PrivilegeResult>,
)

data class TaskInput(
    var id: Long?,
    var title: String?,
    var notes: String,
    var createDate: Date,
    var dueDate: Date,
    var priority: PriorityDetails,
)

data class TaskResult(
    var id: Long?,
    var title: String?,
    var notes: String,
    var createDate: Date,
    var dueDate: Date,
    var priority: PriorityDetails,
)

data class UserInput(
    var id: Long?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var enabled: Boolean?,
    var roles: List<RoleResult>,
)

data class UserLoginInput(
    var username: String,
    var password: String,
)

data class UserResult(
    var id: Long?,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var enabled: Boolean?,
    var tokenExpired: Boolean?,
    var createDate: Date,
    var roles: List<RoleResult>,
)

data class UserSignUpInput(
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
)