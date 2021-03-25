# Spring Boot gRPC Proof of Concept for Microservices

Basic working example using:
* Gradle Kotlin DSL
* Spring Boot
* Kotlin and [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
* [gRPC-Spring-Boot-Starter (by yidongnan)](https://yidongnan.github.io/grpc-spring-boot-starter/en)
* [grpc-kotlin](https://github.com/grpc/grpc-kotlin)
* [logstash-logback-encoder](https://github.com/logstash/logstash-logback-encoder)

It provides:
* authentication and authorization (basic auth)
* request-id tracing across services (supporting kotlins coroutines)
* working JUnit5 tests (consumer and provider)

## Test Communication Manually
```bash
make api-consumer-start
make api-provider-start
```

Call your consumer service via simple rest api:
```bash
curl -H "Authorization: Basic YWRtaW46YWRtaW4=" http://localhost:9010/api/v1/data/my-custom-id
```

Log will look like this:
```
16:22:39.889 http-nio-9010-exec-1 [request_id=f047895f] api-consumer INFO  d.k.p.g.c.controller.DataRestController - get data from service for id 'a-custom-id'
16:22:39.907 http-nio-9010-exec-1 [request_id=f047895f] api-consumer INFO  d.k.p.g.c.r.grpc.DataGrpcService - Loading data via gRPC for id 'a-custom-id'
16:22:40.113 grpc-default-executor-0 [request_id=f047895f] api-provider INFO  d.k.p.g.p.c.g.GrpcBasicAuthenticationProvider - try to authenticate 'admin'...
16:22:40.113 grpc-default-executor-0 [request_id=f047895f] api-provider INFO  d.k.p.g.p.c.g.GrpcBasicAuthenticationProvider - successfully authenticated 'admin'...
16:22:40.162 DefaultDispatcher-worker-1 [request_id=f047895f] api-provider INFO  d.k.p.g.p.controller.grpc.ApiGrpcService - getData for id 'a-custom-id'
16:22:40.162 DefaultDispatcher-worker-1 [request_id=f047895f] api-provider INFO  d.k.p.grpc.provider.service.DataService - get data for id 'a-custom-id'
16:22:40.181 http-nio-9010-exec-1 [request_id=f047895f] api-consumer INFO  d.k.p.g.c.r.grpc.DataGrpcService - Loaded data via gRPC for id 'a-custom-id'
```

## Run JUnit5 Tests

```bash
make api-consumer-test
make api-provider-test
```