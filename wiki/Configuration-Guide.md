# Configuration Guide

## Table of Contents

1. [Overview](#overview)
2. [Environment Variables](#environment-variables)
3. [Liberty Server Configuration](#liberty-server-configuration)
4. [Database Configuration](#database-configuration)
5. [Redis Session Cache Configuration](#redis-session-cache-configuration)
6. [Security Configuration](#security-configuration)
7. [JPA Configuration](#jpa-configuration)
8. [Logging Configuration](#logging-configuration)
9. [Performance Tuning](#performance-tuning)
10. [Environment-Specific Configurations](#environment-specific-configurations)

## Overview

The GarageSale application uses a combination of configuration files and environment variables to manage settings across different deployment environments. This guide covers all configuration aspects.

### Configuration Files

| File | Location | Purpose |
|------|----------|---------|
| `server.xml` | `GarageSaleRuntimeUtil/publish/servers/` | Liberty server configuration |
| `server.env` | `GarageSaleRuntimeUtil/publish/files/` | Environment variables |
| `persistence.xml` | Each JPA module `/META-INF/` | JPA configuration |
| `web.xml` | Each WAR module `/WEB-INF/` | Web application configuration |
| `faces-config.xml` | WAR modules `/WEB-INF/` | JSF configuration |
| `beans.xml` | Modules `/META-INF/` or `/WEB-INF/` | CDI configuration |

## Environment Variables

### Required Environment Variables

Create a `server.env` file in your Liberty server directory:

```properties
# ============================================
# HTTP/HTTPS Configuration
# ============================================
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443

# ============================================
# Security Configuration
# ============================================
KEYSTORE_DEFAULT_PASSWORD=changeit
QS_SECURITY_USERNAME=admin
QS_SECURITY_PASSWORD=admin123

# ============================================
# GSDB (GarageSale Database) Configuration
# ============================================
APP_GSDB_HOSTNAME=localhost
APP_GSDB_PORT=50000
APP_GSDB_NAME=GSDB
APP_GSDB_USER=db2inst1
APP_GSDB_PASSWORD=db2password

# ============================================
# WSBANKDB (Banking Database) Configuration
# ============================================
APP_WSBANKDB_HOSTNAME=localhost
APP_WSBANKDB_PORT=50000
APP_WSBANKDB_NAME=WSBANKDB
APP_WSBANKDB_USER=db2inst1
APP_WSBANKDB_PASSWORD=db2password

# ============================================
# CCDB (Credit Card Database) Configuration
# ============================================
APP_CCDB_HOSTNAME=localhost
APP_CCDB_PORT=50000
APP_CCDB_NAME=CCDB
APP_CCDB_USER=db2inst1
APP_CCDB_PASSWORD=db2password

# ============================================
# Redis Session Cache Configuration
# ============================================
REDIS_URI=redis://localhost:6379
REDIS_SSL_ENABLED=false
REDIS_PASSWORD=

# For Redis Cluster:
# REDIS_URI=redis://node1:6379,node2:6379,node3:6379

# For Redis Sentinel:
# REDIS_URI=redis-sentinel://sentinel1:26379,sentinel2:26379?sentinelMasterId=mymaster
```

### Optional Environment Variables

```properties
# ============================================
# Application Settings
# ============================================
APP_NAME=GarageSale
APP_VERSION=1.0-SNAPSHOT
APP_ENVIRONMENT=development

# ============================================
# Connection Pool Settings
# ============================================
GSDB_MIN_POOL_SIZE=50
GSDB_MAX_POOL_SIZE=300
WSBANKDB_MIN_POOL_SIZE=50
WSBANKDB_MAX_POOL_SIZE=200
CCDB_MIN_POOL_SIZE=50
CCDB_MAX_POOL_SIZE=200

# ============================================
# Session Configuration
# ============================================
SESSION_TIMEOUT=1800
SESSION_COOKIE_NAME=JSESSIONID
SESSION_COOKIE_SECURE=true
SESSION_COOKIE_HTTP_ONLY=true

# ============================================
# Logging Configuration
# ============================================
LOG_LEVEL=INFO
TRACE_SPECIFICATION=*=info

# ============================================
# Email Configuration (if applicable)
# ============================================
SMTP_HOST=smtp.example.com
SMTP_PORT=587
SMTP_USER=noreply@example.com
SMTP_PASSWORD=emailpassword
SMTP_FROM=noreply@example.com
```

## Liberty Server Configuration

### Complete server.xml

Location: `GarageSaleRuntimeUtil/publish/servers/server.xml`

```xml
<server description="GarageSale Liberty Server">

    <!-- ============================================ -->
    <!-- Feature Manager                              -->
    <!-- ============================================ -->
    <featureManager>
        <!-- Core Features -->
        <feature>jndi-1.0</feature>
        <feature>localConnector-1.0</feature>
        <feature>ssl-1.0</feature>
        
        <!-- Web Services -->
        <feature>jaxws-2.2</feature>
        <feature>jaxb-2.2</feature>
        <feature>jaxrs-2.0</feature>
        
        <!-- Enterprise Features -->
        <feature>ejbLite-3.2</feature>
        <feature>jpa-2.1</feature>
        <feature>jdbc-4.1</feature>
        <feature>concurrent-1.0</feature>
        
        <!-- Web Features -->
        <feature>jsf-2.2</feature>
        <feature>el-3.0</feature>
        <feature>websocket-1.1</feature>
        
        <!-- CDI and Bean Validation -->
        <feature>cdi-1.2</feature>
        <feature>beanValidation-1.1</feature>
        <feature>managedBeans-1.0</feature>
        
        <!-- Security and Monitoring -->
        <feature>appSecurity-2.0</feature>
        <feature>monitor-1.0</feature>
        
        <!-- Session Management -->
        <feature>sessionCache-1.0</feature>
    </featureManager>

    <!-- ============================================ -->
    <!-- HTTP Endpoints                               -->
    <!-- ============================================ -->
    <httpEndpoint 
        id="defaultHttpEndpoint"
        host="*"
        httpPort="${DEFAULT_HTTP_PORT}"
        httpsPort="${DEFAULT_HTTPS_PORT}">
        <tcpOptions soReuseAddr="true"/>
        <httpOptions maxKeepAliveRequests="1000"/>
    </httpEndpoint>

    <!-- ============================================ -->
    <!-- Security Configuration                       -->
    <!-- ============================================ -->
    <keyStore 
        id="defaultKeyStore" 
        password="${env.KEYSTORE_DEFAULT_PASSWORD}" />
    
    <quickStartSecurity 
        userName="${env.QS_SECURITY_USERNAME}"
        userPassword="${env.QS_SECURITY_PASSWORD}" />

    <!-- ============================================ -->
    <!-- Application Manager                          -->
    <!-- ============================================ -->
    <applicationManager autoExpand="true" />
    <applicationMonitor updateTrigger="mbean" />

    <!-- ============================================ -->
    <!-- JDBC Driver Configuration                    -->
    <!-- ============================================ -->
    <jdbcDriver id="GSDBProvider" libraryRef="DB2JCC4LIB" />
    
    <library id="DB2JCC4LIB">
        <fileset 
            id="db2jcc4" 
            dir="${shared.resource.dir}/db2drivers" 
            includes="db2jcc4.jar db2jcc_license_cu.jar" />
    </library>

    <!-- ============================================ -->
    <!-- DataSource Configuration                     -->
    <!-- ============================================ -->
    
    <!-- GSDB DataSource -->
    <dataSource 
        id="jdbc/gsdb" 
        jdbcDriverRef="GSDBProvider" 
        jndiName="jdbc/gsdb"
        type="javax.sql.XADataSource">
        <properties 
            databaseName="${env.APP_GSDB_NAME}" 
            driverType="4"
            password="${env.APP_GSDB_PASSWORD}"
            portNumber="${env.APP_GSDB_PORT}"
            serverName="${env.APP_GSDB_HOSTNAME}" 
            user="${env.APP_GSDB_USER}" />
        <connectionManager 
            id="gsdbConnMgr"
            maxPoolSize="${env.GSDB_MAX_POOL_SIZE:300}" 
            minPoolSize="${env.GSDB_MIN_POOL_SIZE:50}"
            connectionTimeout="30s"
            maxIdleTime="1800s"
            reapTime="180s"
            agedTimeout="1800s" />
    </dataSource>
    
    <!-- CCDB DataSource -->
    <dataSource 
        id="jdbc/ccdb" 
        jdbcDriverRef="GSDBProvider" 
        jndiName="jdbc/ccdb"
        type="javax.sql.XADataSource">
        <properties 
            databaseName="${env.APP_CCDB_NAME}" 
            driverType="4"
            password="${env.APP_CCDB_PASSWORD}"
            portNumber="${env.APP_CCDB_PORT}"
            serverName="${env.APP_CCDB_HOSTNAME}" 
            user="${env.APP_CCDB_USER}" />
        <connectionManager 
            id="ccdbConnMgr"
            maxPoolSize="${env.CCDB_MAX_POOL_SIZE:200}" 
            minPoolSize="${env.CCDB_MIN_POOL_SIZE:50}" />
    </dataSource>
    
    <!-- WSBANKDB DataSource -->
    <dataSource 
        id="jdbc/wsbankdb" 
        jdbcDriverRef="GSDBProvider"
        jndiName="jdbc/wsbankdb" 
        type="javax.sql.XADataSource">
        <properties 
            databaseName="${env.APP_WSBANKDB_NAME}" 
            driverType="4"
            password="${env.APP_WSBANKDB_PASSWORD}"
            portNumber="${env.APP_WSBANKDB_PORT}"
            serverName="${env.APP_WSBANKDB_HOSTNAME}"
            user="${env.APP_WSBANKDB_USER}" />
        <connectionManager 
            id="wsbankdbConnMgr"
            maxPoolSize="${env.WSBANKDB_MAX_POOL_SIZE:200}" 
            minPoolSize="${env.WSBANKDB_MIN_POOL_SIZE:50}" />
    </dataSource>

    <!-- ============================================ -->
    <!-- Redis Session Cache Configuration            -->
    <!-- ============================================ -->
    <library id="RedisLib">
        <fileset 
            dir="${shared.resource.dir}/redis" 
            includes="*.jar"/>
    </library>
    
    <httpSessionCache libraryRef="RedisLib">
        <properties
            uri="${env.REDIS_URI}"
            sslEnabled="${env.REDIS_SSL_ENABLED}"
            password="${env.REDIS_PASSWORD}"/>
    </httpSessionCache>
    
    <httpSession 
        cloneId="${env.HOSTNAME:localhost}_cloneID" 
        storageRef="httpSessionCache"
        invalidationTimeout="${env.SESSION_TIMEOUT:1800}"/>

    <!-- ============================================ -->
    <!-- Web Container Configuration                  -->
    <!-- ============================================ -->
    <webContainer 
        deferServletLoad="false"
        invokeFlushAfterService="false"
        disableXPoweredBy="true">
        <properties 
            copyAttributesKeySet="true"
            decodeUrlAsUtf8="true"/>
    </webContainer>

    <!-- ============================================ -->
    <!-- Transaction Configuration                    -->
    <!-- ============================================ -->
    <transaction 
        totalTranLifetimeTimeout="300s"
        clientInactivityTimeout="60s"
        heuristicRetryInterval="60"
        heuristicRetryLimit="10"
        waitForRecovery="false" />

    <!-- ============================================ -->
    <!-- JAX-RS Third Party Libraries                 -->
    <!-- ============================================ -->
    <library id="thirdPartyLib">
        <fileset 
            dir="${shared.resource.dir}/jaxrsThirdPartyJars"
            includes="*.jar" 
            scanInterval="5s" />
    </library>

    <!-- ============================================ -->
    <!-- Application Configuration                    -->
    <!-- ============================================ -->
    <enterpriseApplication 
        id="GarageSaleLibertyEAR7"
        location="GarageSaleLibertyEAR.ear" 
        name="GarageSaleLibertyEAR7">
        <classloader commonLibraryRef="thirdPartyLib" />
    </enterpriseApplication>

    <!-- ============================================ -->
    <!-- Logging Configuration                        -->
    <!-- ============================================ -->
    <logging 
        consoleLogLevel="${env.LOG_LEVEL:INFO}"
        maxFileSize="50" 
        maxFiles="5"
        traceFileName="trace.log"
        traceSpecification="${env.TRACE_SPECIFICATION:*=info}"/>

</server>
```

## Database Configuration

### Connection Pool Tuning

```xml
<connectionManager 
    id="gsdbConnMgr"
    maxPoolSize="300"           <!-- Maximum connections -->
    minPoolSize="50"            <!-- Minimum connections -->
    connectionTimeout="30s"     <!-- Wait time for connection -->
    maxIdleTime="1800s"         <!-- Idle connection timeout -->
    reapTime="180s"             <!-- Cleanup interval -->
    agedTimeout="1800s"         <!-- Max connection age -->
    purgePolicy="EntirePool"    <!-- Purge strategy -->
    numConnectionsPerThreadLocal="1" />
```

### Database-Specific Settings

#### DB2 Configuration

```properties
# DB2 Client Configuration
db2set DB2CODEPAGE=1208
db2set DB2_COMPATIBILITY_VECTOR=ORA
db2set DB2_DEFERRED_PREPARE_SEMANTICS=YES

# Connection Properties
driverType=4
currentSchema=GSDB
retrieveMessagesFromServerOnGetMessage=true
progressiveStreaming=2
```

## Redis Session Cache Configuration

### Basic Configuration

```xml
<httpSessionCache libraryRef="RedisLib">
    <properties
        uri="redis://localhost:6379"
        sslEnabled="false"
        password=""/>
</httpSessionCache>
```

### Cluster Configuration

```xml
<httpSessionCache libraryRef="RedisLib">
    <properties
        uri="redis://node1:6379,node2:6379,node3:6379"
        sslEnabled="true"
        password="${env.REDIS_PASSWORD}"
        maxTotal="100"
        maxIdle="50"
        minIdle="10"
        testOnBorrow="true"
        testOnReturn="false"
        testWhileIdle="true"/>
</httpSessionCache>
```

### Sentinel Configuration

```xml
<httpSessionCache libraryRef="RedisLib">
    <properties
        uri="redis-sentinel://sentinel1:26379,sentinel2:26379,sentinel3:26379?sentinelMasterId=mymaster"
        sslEnabled="true"
        password="${env.REDIS_PASSWORD}"
        sentinelPassword="${env.REDIS_SENTINEL_PASSWORD}"/>
</httpSessionCache>
```

## Security Configuration

### SSL/TLS Configuration

```xml
<keyStore 
    id="defaultKeyStore" 
    location="${server.config.dir}/resources/security/key.p12"
    type="PKCS12"
    password="${env.KEYSTORE_PASSWORD}" />

<ssl 
    id="defaultSSLConfig" 
    keyStoreRef="defaultKeyStore"
    trustStoreRef="defaultTrustStore"
    sslProtocol="TLSv1.2"
    clientAuthenticationSupported="false" />

<trustStore 
    id="defaultTrustStore"
    location="${server.config.dir}/resources/security/trust.p12"
    type="PKCS12"
    password="${env.TRUSTSTORE_PASSWORD}" />
```

### User Registry Configuration

```xml
<basicRegistry id="basic" realm="GarageSaleRealm">
    <user name="admin" password="{xor}Lz4sLCgwLTs=" />
    <user name="user1" password="{xor}Lz4sLCgwLTs=" />
    
    <group name="administrators">
        <member name="admin" />
    </group>
    
    <group name="users">
        <member name="user1" />
    </group>
</basicRegistry>
```

### Password Encoding

```bash
# Encode password using Liberty securityUtility
${WLP_HOME}/bin/securityUtility encode myPassword

# Output: {xor}Lz4sLCgwLTs=
```

## JPA Configuration

### persistence.xml for GSDB

Location: `GarageSaleDBJPA/src/main/resources/META-INF/persistence.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" 
    xmlns="http://xmlns.jcp.org/xml/ns/persistence"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence 
                        http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
    
    <persistence-unit name="GSDB" transaction-type="JTA">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        <jta-data-source>jdbc/gsdb</jta-data-source>
        
        <!-- Entity Classes -->
        <class>com.ibm.websphere.svt.gs.gsdb.entity.Customer</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.CustomerInfo</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.Inventory</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.Category</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.MfgCategory</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.StoreCredit</class>
        <class>com.ibm.websphere.svt.gs.gsdb.entity.Settings</class>
        
        <properties>
            <!-- EclipseLink Properties -->
            <property name="eclipselink.target-database" value="DB2"/>
            <property name="eclipselink.logging.level" value="INFO"/>
            <property name="eclipselink.logging.level.sql" value="FINE"/>
            <property name="eclipselink.logging.parameters" value="true"/>
            
            <!-- Connection Pool -->
            <property name="eclipselink.jdbc.cache-statements" value="true"/>
            <property name="eclipselink.jdbc.cache-statements.size" value="100"/>
            
            <!-- Performance -->
            <property name="eclipselink.cache.shared.default" value="true"/>
            <property name="eclipselink.cache.size.default" value="1000"/>
            <property name="eclipselink.query-results-cache" value="true"/>
            
            <!-- Schema Generation (Development Only) -->
            <!-- <property name="eclipselink.ddl-generation" value="create-or-extend-tables"/> -->
            <!-- <property name="eclipselink.ddl-generation.output-mode" value="database"/> -->
        </properties>
    </persistence-unit>
</persistence>
```

## Logging Configuration

### Console and File Logging

```xml
<logging 
    consoleLogLevel="INFO"
    consoleFormat="simple"
    consoleSource="message,trace,accessLog,ffdc,audit"
    maxFileSize="50" 
    maxFiles="5"
    traceFileName="trace.log"
    traceFormat="ENHANCED"
    traceSpecification="*=info:com.ibm.websphere.svt.gs.*=all"/>
```

### Trace Specifications

```properties
# All components at INFO level
*=info

# Application packages at ALL level
com.ibm.websphere.svt.gs.*=all

# Specific component tracing
com.ibm.ws.webcontainer.*=all
com.ibm.ws.jpa.*=fine
com.ibm.ws.ejbcontainer.*=fine
com.ibm.ws.session.*=all

# Database tracing
eclipselink.sql=fine
eclipselink.logging.sql=fine
```

### JSON Logging (for Cloud)

```xml
<logging 
    messageFormat="json"
    messageSource="message,trace,accessLog,ffdc,audit"
    consoleFormat="json"
    consoleSource="message,trace,accessLog,ffdc,audit"/>
```

## Performance Tuning

### JVM Options

Create `jvm.options` file:

```properties
# Heap Size
-Xms2048m
-Xmx4096m

# Garbage Collection
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:ParallelGCThreads=8
-XX:ConcGCThreads=2

# GC Logging
-Xlog:gc*:file=${server.output.dir}/logs/gc.log:time,level,tags:filecount=5,filesize=20M

# Performance
-XX:+AlwaysPreTouch
-XX:+UseStringDeduplication
-XX:+OptimizeStringConcat

# Monitoring
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9999
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
```

### Thread Pool Configuration

```xml
<executor 
    id="DefaultExecutor"
    name="DefaultExecutor"
    coreThreads="50"
    maxThreads="200"
    keepAlive="60s"
    stealPolicy="STRICT"
    rejectedWorkPolicy="CALLER_RUNS"/>
```

## Environment-Specific Configurations

### Development Environment

```properties
# server.env.dev
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443
LOG_LEVEL=ALL
TRACE_SPECIFICATION=*=info:com.ibm.websphere.svt.gs.*=all
APP_GSDB_HOSTNAME=localhost
REDIS_URI=redis://localhost:6379
REDIS_SSL_ENABLED=false
```

### Test Environment

```properties
# server.env.test
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443
LOG_LEVEL=INFO
TRACE_SPECIFICATION=*=info
APP_GSDB_HOSTNAME=test-db.example.com
REDIS_URI=redis://test-redis1:6379,test-redis2:6379
REDIS_SSL_ENABLED=true
```

### Production Environment

```properties
# server.env.prod
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443
LOG_LEVEL=WARNING
TRACE_SPECIFICATION=*=warning
APP_GSDB_HOSTNAME=prod-db.example.com
REDIS_URI=redis://prod-redis1:6379,prod-redis2:6379,prod-redis3:6379
REDIS_SSL_ENABLED=true
GSDB_MAX_POOL_SIZE=500
SESSION_COOKIE_SECURE=true
```

## Configuration Validation

### Verify Configuration

```bash
# Check server.xml syntax
${WLP_HOME}/bin/server validate <server-name>

# Test database connections
${WLP_HOME}/bin/server dump <server-name> --include=heap,system

# View effective configuration
${WLP_HOME}/bin/server dump <server-name> --include=config
```

### Common Configuration Issues

1. **Database Connection Failures**
   - Verify hostname and port
   - Check credentials
   - Ensure DB2 drivers are in correct location

2. **Redis Connection Issues**
   - Verify Redis URI format
   - Check SSL settings
   - Ensure Redis client JARs are present

3. **Memory Issues**
   - Increase heap size in jvm.options
   - Adjust connection pool sizes
   - Enable GC logging

---

**Next**: [API Documentation](API-Documentation.md) | [Troubleshooting Guide](Troubleshooting-Guide.md)