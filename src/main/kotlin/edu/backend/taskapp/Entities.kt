package edu.backend.taskapp

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "reminder")
data class Reminder(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    @Column(name = "reminder_date")
    var reminderDate:Date,
    // Entity Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Share the same primary key between 2 tables
    var task: Task,
)

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var title: String,
    var notes: String,
    @Temporal(TemporalType.DATE)
    var createDate: Date,
    @Temporal(TemporalType.DATE)
    var dueDate: Date,
    // Entity Relationship
    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false, referencedColumnName = "id")
    var priority: Priority,
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false, referencedColumnName = "id")
    var status: Status,
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    var user: User,

)

@Entity
@Table(name = "status")
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var label: String,
    // Entity Relationship
    @OneToMany(mappedBy = "status")
    var taskList: List<Task>
)

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var name: String,
    // Entity Relationship
    @ManyToMany
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privilegeList: Set<Privilege>,
)

@Entity
@Table(name = "privilege")
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long?,
    var name: String,
    // Entity Relationship
    @ManyToMany(mappedBy = "roleList", fetch = FetchType.LAZY)
    var userList: Set<User>,
    @ManyToMany(mappedBy = "privilegeList", fetch = FetchType.LAZY)
    var roleList: Set<Role>,
)

@Entity
@Table(name = "priority")
data class Priority(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var name: String,
    // Entity Relationship
    @OneToMany(mappedBy = "priority")
    var taskList: List<Task>,
)

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var firstName: String,
    var lastName: String,
    var password: String,
    var email: String,
    var createDate: Date,
    var enabled: Boolean?,
    var tokenExpired: Boolean?,
    // Entity Relationship
    @OneToMany(mappedBy = "user")
    var taskList: List<Task>,
    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roleList: Set<Role>,

)