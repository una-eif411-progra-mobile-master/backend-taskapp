package edu.backend.taskapp

import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface TaskService {
    /**
     * Find all priorities
     */
    fun findAll() : List<PriorityDetails> ?

    @Service
    class AbstractTaskService (
        @Autowired
        val priorityRepository : PriorityRepository
    ): TaskService {
        val converter = Mappers.getMapper(PriorityMapper::class.java)
        override fun findAll(): List<PriorityDetails>? {
            return converter.priorityListToPriorityDetailsList(priorityRepository.findAll())
        }

    }
}