# ansc-quarkus-tool

## Quickstart

Requirement :

* maven 3.9.x
* java 21+ (GraalVM for native version)

1. Verify the app

```shell
mvn verify
```

2. Start the app

```shell
mvn quarkus:dev
```

3. Try the app

Open the [swagger-ui](http://localhost:8080/q/swagger-ui/)

NOTE:

* Powered by Quarkus 3.24.2
* Using Fugerit Venus Doc 8.13.14 (extensions : base,freemarker)

4. OpenAPI schema

[Here](src/main/openapi/schema.yaml) you can find the OpenAPI schema.