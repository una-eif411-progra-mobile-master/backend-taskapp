package edu.backend.taskapp

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reminder")
data class Reminder(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "reminder_date")
    var reminderDate:Date,
    // Entity Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Share the same primary key between 2 tables
    var task: Task,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Reminder) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Reminder(id=$id, reminderDate=$reminderDate, task=$task)"
    }

}

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
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

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Task(id=$id, title='$title', notes='$notes', createDate=$createDate, dueDate=$dueDate, priority=$priority, status=$status, user=$user)"
    }

}

@Entity
@Table(name = "status")
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var label: String,
    // Entity Relationship
    @OneToMany(mappedBy = "status")
    var taskList: List<Task>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Status) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Status(id=$id, label='$label', taskList=$taskList)"
    }

}

@Entity
@Table(name = "role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    // Entity Relationship
    @ManyToMany
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privilegeList: Set<Privilege>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name', privilegeList=$privilegeList)"
    }

}

@Entity
@Table(name = "privilege")
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long? = null,
    var name: String,
    // Entity Relationship
    @ManyToMany(fetch = FetchType.LAZY)
    var userList: Set<User>,
    @ManyToMany(fetch = FetchType.LAZY)
    var roleList: Set<Role>,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Privilege) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Privilege(id=$id, name='$name', userList=$userList, roleList=$roleList)"
    }
}

@Entity
@Table(name = "priority")
data class Priority(
    var label: String,
    // Entity Relationship
    @OneToMany(mappedBy = "priority")
    var taskList: List<Task>? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Priority) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Priority(label='$label', taskList=$taskList, id=$id)"
    }
}

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
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

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + email.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, firstName='$firstName', lastName='$lastName', password='$password', email='$email', createDate=$createDate, enabled=$enabled, tokenExpired=$tokenExpired, taskList=$taskList, roleList=$roleList)"
    }

}