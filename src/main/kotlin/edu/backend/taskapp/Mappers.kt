package edu.backend.taskapp

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface PriorityMapper {
    fun priorityToPriorityDetails(priority: Priority) : PriorityDetails

    fun priorityListToPriorityDetailsList(priorityList: List<Priority>) : List<PriorityDetails>
}