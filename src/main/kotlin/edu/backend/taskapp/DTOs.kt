package edu.backend.taskapp

import java.util.*

data class StatusInput(
    var id:Long,
)

data class StatusDetails(
    var id:Long,
    var label: String? = null,
)

data class PriorityInput(
    var id:Long,
)

data class PriorityDetails(
    var id:Long,
    var label: String? = null,
)

data class PrivilegeResult (
    var id: Long,
    var name: String? = null
)

data class RoleResult (
    var id: Long,
    var name: String?,
    var privileges: List<PrivilegeResult>,
)

data class TaskInput(
    var id: Long?=null,
    var title: String?=null,
    var notes: String?=null,
    var createDate: Date?=null,
    var dueDate: Date?=null,
    var priority: PriorityInput?=null,
    var status: StatusInput?=null,
    var user: UserInput?=null,
)

data class TaskResult(
    var id: Long,
    var title: String?,
    var notes: String,
    var createDate: Date,
    var dueDate: Date,
    var priority: PriorityDetails,
    var status: StatusDetails,
)

data class UserInput(
    var id: Long?=null,
    var firstName: String?=null,
    var lastName: String?=null,
    var email: String?=null,
    var password: String?=null,
    var enabled: Boolean?=null,
    var roles: List<RoleResult>?=null,
)

data class UserLoginInput(
    var username: String,
    var password: String,
)

data class UserResult(
    var id: Long,
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