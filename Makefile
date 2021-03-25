api-consumer-start:
	cd api-consumer && ./gradlew bootRun

api-consumer-test:
	cd api-consumer && ./gradlew test

api-provider-start:
	cd api-provider && ./gradlew bootRun

api-provider-test:
	cd api-prodvider && ./gradlew test
