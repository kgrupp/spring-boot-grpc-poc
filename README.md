# Spring Boot gRPC Proof of Concept for Microservices

Basic working example using:
* Gradle Kotlin DSL
* Spring Boot
* Kotlin
* [gRPC-Spring-Boot-Starter (by yidongnan)](https://yidongnan.github.io/grpc-spring-boot-starter/en)

It provides:
* authentication and authorization (basic auth)
* request-id tracing across services with logback
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
13:02:27.078 [request_id=63cf5f84] api-consumer INFO  d.k.p.g.c.controller.DataRestController - get data from service for id 'my-custom-id'
13:02:27.078 [request_id=63cf5f84] api-consumer INFO  d.k.p.g.c.r.grpc.DataGrpcService - Loading data via gRPC for id 'my-custom-id'
13:02:27.195 [request_id=63cf5f84] api-provider INFO  d.k.p.g.p.c.g.GrpcBasicAuthenticationProvider - try to authenticate 'admin'...
13:02:27.195 [request_id=63cf5f84] api-provider INFO  d.k.p.g.p.c.g.GrpcBasicAuthenticationProvider - successfully authenticated 'admin'...
13:02:27.196 [request_id=63cf5f84] api-provider INFO  d.k.p.g.p.controller.grpc.ApiGrpcService - getData for id 'my-custom-id'
13:02:27.196 [request_id=63cf5f84] api-provider INFO  d.k.p.grpc.provider.service.DataService - get data for id 'my-custom-id'
13:02:27.203 [request_id=63cf5f84] api-consumer INFO  d.k.p.g.c.r.grpc.DataGrpcService - Loaded data via gRPC for id 'my-custom-id'
```

## Run JUnit5 Tests

```bash
make api-consumer-test
make api-provider-test
```