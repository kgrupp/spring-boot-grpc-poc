package de.kgrupp.poc.grpc.consumer.controller

import de.kgrupp.poc.grpc.consumer.model.Data
import de.kgrupp.poc.grpc.consumer.service.DataService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val DATA_PATH = "/api/v1/data"

@RestController
@RequestMapping(path = [DATA_PATH])
class DataRestController(private val dataService: DataService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(path = ["/{id}"])
    fun getOne(@PathVariable id: String): ResponseData {
        logger.info("get data from service for id '$id'")
        return dataService.get(id).toResponse()
    }
}

private fun Data.toResponse(): ResponseData = ResponseData(this.id, this.value)

data class ResponseData(val id: String, val value: String)