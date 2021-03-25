package de.kgrupp.poc.grpc.provider.service

import de.kgrupp.poc.grpc.provider.model.Data
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val logger: Logger = LoggerFactory.getLogger(DataService::class.java)

@Service
class DataService {
    suspend fun get(id: String): Data {
        logger.info("get data for id '$id'")
        return Data("This is your custom data for this id $id.")
    }
}
