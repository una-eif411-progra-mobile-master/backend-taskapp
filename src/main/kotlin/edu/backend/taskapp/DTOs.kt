package edu.backend.taskapp

import java.util.*

data class StatusDetails(
    var id:Long? = null,
    var label: String? = null,
)

data class PriorityDetails(
    var id:Long? = null,
    var label: String? = null,
)

data class PrivilegeDetails (
    var id: Long? = null,
    var name: String? = null
)

data class RoleDetails (
    var id: Long? = null,
    var name: String? = null,
    var privileges: List<PrivilegeDetails>? = null,
)

data class TaskInput(
    var id: Long?=null,
    var title: String?=null,
    var notes: String?=null,
    var createDate: Date?=null,
    var dueDate: Date?=null,
    var priority: PriorityDetails?=null,
    var status: StatusDetails?=null,
    var user: UserInput?=null,
)

data class TaskResult(
    var id: Long,
    var title: String,
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
    var roles: List<RoleDetails>?=null,
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
    var roles: List<RoleDetails>,
)

data class UserSignUpInput(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null,
)