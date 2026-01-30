# Redis Client Libraries for Liberty Session Cache

## Overview
This directory should contain the Redis client JAR files required for Liberty's sessionCache feature to connect to your Redis cluster.

## Required Libraries

### Option 1: Jedis (Recommended for Redis Cluster)

Download the following JARs and place them in this directory:

1. **Jedis** (Latest stable version recommended)
   - Download from: https://repo1.maven.org/maven2/redis/clients/jedis/
   - File: `jedis-4.3.1.jar` (or latest version)

2. **Apache Commons Pool 2** (Required dependency)
   - Download from: https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/
   - File: `commons-pool2-2.11.1.jar` (or latest version)

3. **SLF4J API** (Logging dependency)
   - Download from: https://repo1.maven.org/maven2/org/slf4j/slf4j-api/
   - File: `slf4j-api-2.0.7.jar` (or latest version)

### Option 2: Lettuce (Alternative Redis Client)

If you prefer Lettuce, download these JARs:

1. **Lettuce Core**
   - Download from: https://repo1.maven.org/maven2/io/lettuce/lettuce-core/
   - File: `lettuce-core-6.2.4.RELEASE.jar` (or latest version)

2. **Netty** (Required dependency)
   - Download from: https://repo1.maven.org/maven2/io/netty/netty-all/
   - File: `netty-all-4.1.92.Final.jar` (or latest version)

3. **Reactive Streams** (Required dependency)
   - Download from: https://repo1.maven.org/maven2/org/reactivestreams/reactive-streams/
   - File: `reactive-streams-1.0.4.jar` (or latest version)

## Maven Coordinates

If you're using Maven to manage dependencies, here are the coordinates:

### Jedis
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.3.1</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.11.1</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.7</version>
</dependency>
```

### Lettuce
```xml
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.2.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.92.Final</version>
</dependency>
<dependency>
    <groupId>org.reactivestreams</groupId>
    <artifactId>reactive-streams</artifactId>
    <version>1.0.4</version>
</dependency>
```

## Installation Steps

### Manual Installation

1. Create this directory if it doesn't exist:
   ```bash
   mkdir -p GarageSaleRuntimeUtil/publish/redis
   ```

2. Download the required JAR files from Maven Central

3. Copy all JAR files to this directory

4. Verify the files are in place:
   ```bash
   ls -la GarageSaleRuntimeUtil/publish/redis/
   ```

### Using Maven Dependency Plugin

If you have Maven installed, you can use the dependency plugin to download JARs:

```bash
# For Jedis
mvn dependency:copy -Dartifact=redis.clients:jedis:4.3.1 -DoutputDirectory=./
mvn dependency:copy -Dartifact=org.apache.commons:commons-pool2:2.11.1 -DoutputDirectory=./
mvn dependency:copy -Dartifact=org.slf4j:slf4j-api:2.0.7 -DoutputDirectory=./
```

### Using Gradle

If you're using Gradle, add to your build.gradle:

```gradle
configurations {
    redisLibs
}

dependencies {
    redisLibs 'redis.clients:jedis:4.3.1'
    redisLibs 'org.apache.commons:commons-pool2:2.11.1'
    redisLibs 'org.slf4j:slf4j-api:2.0.7'
}

task copyRedisLibs(type: Copy) {
    from configurations.redisLibs
    into 'GarageSaleRuntimeUtil/publish/redis'
}
```

Then run:
```bash
gradle copyRedisLibs
```

## Verification

After placing the JARs in this directory, the Liberty server configuration will automatically load them via the library reference in server.xml:

```xml
<library id="RedisLib">
    <fileset dir="${shared.resource.dir}/redis" includes="*.jar"/>
</library>
```

## Troubleshooting

### ClassNotFoundException
If you see ClassNotFoundException errors in the Liberty logs, ensure:
1. All required JAR files are present in this directory
2. The JAR files are not corrupted (verify file sizes)
3. The Liberty server has read permissions for these files

### Version Compatibility
- Ensure all dependency versions are compatible with each other
- Check the Redis client library documentation for version compatibility
- Liberty sessionCache-1.0 works with most modern Redis client versions

## Additional Resources

- [Jedis GitHub](https://github.com/redis/jedis)
- [Lettuce Documentation](https://lettuce.io/)
- [Maven Central Repository](https://repo1.maven.org/maven2/)
- [Liberty sessionCache Documentation](https://www.ibm.com/docs/en/was-liberty/base?topic=liberty-sessioncache-10)