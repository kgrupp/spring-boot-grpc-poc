package de.kgrupp.poc.grpc.consumer.service

import de.kgrupp.poc.grpc.consumer.model.Data
import de.kgrupp.poc.grpc.consumer.repository.grpc.DataGrpcService
import org.springframework.stereotype.Service

@Service
class DataService(private val dataGrpcService: DataGrpcService) {
    suspend fun get(id: String): Data = dataGrpcService.loadData(id)
}