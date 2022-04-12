package edu.backend.taskapp

import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface PriorityService {
    /**
     * Find all priorities
     */
    fun findAll() : List<PriorityDetails> ?
}

@Service
class AbstractPriorityService (
    @Autowired
    val priorityRepository : PriorityRepository,

    @Autowired
    val priorityMapper: PriorityMapper

): PriorityService {
    override fun findAll(): List<PriorityDetails>? {
        return priorityMapper.priorityListToPriorityDetailsList(priorityRepository.findAll())
    }

}