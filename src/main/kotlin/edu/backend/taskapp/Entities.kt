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
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Share the same primary key between 2 tables
    var task: Task,
)

@Entity
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
)

@Entity
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var label: String,
)

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var name: String,
)

@Entity
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long?,
    var name: String,
)

@Entity
data class Priority(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long?,
    var name: String,
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

)