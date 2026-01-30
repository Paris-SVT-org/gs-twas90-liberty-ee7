# Troubleshooting Guide

## Table of Contents

1. [Overview](#overview)
2. [Common Issues](#common-issues)
3. [Application Startup Issues](#application-startup-issues)
4. [Database Connection Issues](#database-connection-issues)
5. [Redis Session Cache Issues](#redis-session-cache-issues)
6. [Performance Issues](#performance-issues)
7. [Memory Issues](#memory-issues)
8. [Web Service Issues](#web-service-issues)
9. [Deployment Issues](#deployment-issues)
10. [Diagnostic Tools](#diagnostic-tools)
11. [Log Analysis](#log-analysis)
12. [Support Resources](#support-resources)

## Overview

This guide provides solutions to common issues encountered when deploying and running the GarageSale application on WebSphere Liberty.

### Quick Diagnostic Checklist

- [ ] Check Liberty server logs: `${server.output.dir}/logs/messages.log`
- [ ] Verify environment variables in `server.env`
- [ ] Test database connectivity
- [ ] Verify Redis connection
- [ ] Check application deployment status
- [ ] Review trace logs if enabled
- [ ] Verify required libraries are present

## Common Issues

### Issue: Application Won't Start

**Symptoms**:
- Server starts but application doesn't load
- Error messages in `messages.log`
- Application state shows "STOPPED"

**Possible Causes**:
1. Missing dependencies
2. Configuration errors
3. Database connection failure
4. Port conflicts

**Solutions**:

#### Check Application Status
```bash
# View application status
${WLP_HOME}/bin/server status <server-name>

# Check deployed applications
${WLP_HOME}/bin/server dump <server-name> --include=heap
```

#### Review Logs
```bash
# Check messages.log
tail -f ${server.output.dir}/logs/messages.log

# Look for specific errors
grep -i "error\|exception\|failed" ${server.output.dir}/logs/messages.log
```

#### Verify Configuration
```bash
# Validate server.xml
${WLP_HOME}/bin/server validate <server-name>

# Check for syntax errors
xmllint --noout ${server.config.dir}/server.xml
```

### Issue: ClassNotFoundException

**Symptoms**:
```
java.lang.ClassNotFoundException: com.ibm.db2.jcc.DB2Driver
```

**Solution**:

1. **Verify DB2 Drivers**:
```bash
# Check if drivers exist
ls -la ${shared.resource.dir}/db2drivers/

# Should contain:
# - db2jcc4.jar
# - db2jcc_license_cu.jar
```

2. **Check Library Configuration**:
```xml
<library id="DB2JCC4LIB">
    <fileset 
        id="db2jcc4" 
        dir="${shared.resource.dir}/db2drivers" 
        includes="db2jcc4.jar db2jcc_license_cu.jar" />
</library>
```

3. **Verify File Permissions**:
```bash
chmod 644 ${shared.resource.dir}/db2drivers/*.jar
```

### Issue: Port Already in Use

**Symptoms**:
```
CWWKO0221E: TCP Channel defaultHttpEndpoint has been unable to bind to port 9080
```

**Solution**:

1. **Find Process Using Port**:
```bash
# Windows
netstat -ano | findstr :9080

# Linux/Mac
lsof -i :9080
```

2. **Change Port in server.env**:
```properties
DEFAULT_HTTP_PORT=9081
DEFAULT_HTTPS_PORT=9444
```

3. **Kill Conflicting Process** (if appropriate):
```bash
# Windows
taskkill /PID <process-id> /F

# Linux/Mac
kill -9 <process-id>
```

## Application Startup Issues

### Issue: Slow Startup

**Symptoms**:
- Application takes more than 2 minutes to start
- Server appears hung during startup

**Diagnostic Steps**:

1. **Enable Startup Trace**:
```xml
<logging 
    traceSpecification="*=info:com.ibm.ws.kernel.launch.*=all:com.ibm.ws.app.manager.*=all"/>
```

2. **Check for Database Delays**:
```bash
# Test database connection
db2 connect to GSDB user <username> using <password>
db2 "SELECT 1 FROM SYSIBM.SYSDUMMY1"
```

3. **Review JPA Initialization**:
```bash
grep -i "jpa\|eclipselink" ${server.output.dir}/logs/messages.log
```

**Solutions**:

1. **Optimize Connection Pools**:
```xml
<connectionManager 
    minPoolSize="10"
    maxPoolSize="50"
    connectionTimeout="10s"/>
```

2. **Disable Unnecessary Features**:
```xml
<!-- Comment out unused features -->
<!-- <feature>osgiConsole-1.0</feature> -->
```

3. **Increase JVM Heap**:
```properties
# jvm.options
-Xms2048m
-Xmx4096m
```

## Database Connection Issues

### Issue: DSRA0010E Connection Error

**Symptoms**:
```
DSRA0010E: SQL State = 08001, Error Code = -4,499
```

**Diagnostic Steps**:

1. **Test Database Connectivity**:
```bash
# Ping database server
ping <database-hostname>

# Test port connectivity
telnet <database-hostname> 50000

# Test DB2 connection
db2 connect to GSDB user <username> using <password>
```

2. **Verify Credentials**:
```bash
# Check environment variables
echo $APP_GSDB_HOSTNAME
echo $APP_GSDB_PORT
echo $APP_GSDB_USER
```

3. **Check Database Status**:
```bash
# DB2 status
db2 list active databases

# Check if database is started
db2start
```

**Solutions**:

1. **Update Connection Properties**:
```properties
# server.env
APP_GSDB_HOSTNAME=correct-hostname
APP_GSDB_PORT=50000
APP_GSDB_NAME=GSDB
APP_GSDB_USER=db2inst1
APP_GSDB_PASSWORD=correct-password
```

2. **Increase Connection Timeout**:
```xml
<connectionManager 
    connectionTimeout="60s"
    maxIdleTime="1800s"/>
```

3. **Check Firewall Rules**:
```bash
# Ensure port 50000 is open
# Add firewall rule if needed
```

### Issue: Connection Pool Exhausted

**Symptoms**:
```
DSRA0010E: Connection pool exhausted
```

**Solution**:

1. **Increase Pool Size**:
```xml
<connectionManager 
    maxPoolSize="500"
    minPoolSize="100"/>
```

2. **Check for Connection Leaks**:
```bash
# Enable connection leak detection
grep -i "connection.*leak" ${server.output.dir}/logs/messages.log
```

3. **Review Application Code**:
```java
// Ensure connections are closed
try (Connection conn = dataSource.getConnection()) {
    // Use connection
} // Auto-closed
```

## Redis Session Cache Issues

### Issue: Redis Connection Failed

**Symptoms**:
```
Unable to connect to Redis: Connection refused
```

**Diagnostic Steps**:

1. **Test Redis Connectivity**:
```bash
# Ping Redis server
redis-cli -h <redis-host> -p 6379 ping

# Expected: PONG
```

2. **Check Redis Status**:
```bash
# Redis server info
redis-cli -h <redis-host> -p 6379 info server
```

3. **Verify Redis Configuration**:
```bash
# Check environment variables
echo $REDIS_URI
echo $REDIS_PASSWORD
```

**Solutions**:

1. **Update Redis URI**:
```properties
# server.env
REDIS_URI=redis://correct-hostname:6379
REDIS_SSL_ENABLED=false
REDIS_PASSWORD=correct-password
```

2. **Check Redis Client Libraries**:
```bash
# Verify JARs exist
ls -la ${shared.resource.dir}/redis/

# Should contain:
# - jedis-4.3.1.jar
# - commons-pool2-2.11.1.jar
# - slf4j-api-2.0.7.jar
```

3. **Test with Simple Client**:
```bash
redis-cli -h <redis-host> -p 6379 -a <password>
SET test "value"
GET test
```

### Issue: Session Not Persisting

**Symptoms**:
- Users logged out unexpectedly
- Session data lost between requests

**Solution**:

1. **Verify Session Configuration**:
```xml
<httpSession 
    cloneId="server1_cloneID" 
    storageRef="httpSessionCache"
    invalidationTimeout="1800"/>
```

2. **Check Redis Memory**:
```bash
redis-cli -h <redis-host> info memory
```

3. **Enable Session Debug Logging**:
```xml
<logging 
    traceSpecification="*=info:com.ibm.ws.session.*=all"/>
```

## Performance Issues

### Issue: Slow Response Times

**Symptoms**:
- Page load times > 5 seconds
- API responses delayed
- High CPU usage

**Diagnostic Steps**:

1. **Check Server Metrics**:
```bash
# CPU and memory usage
top -p <liberty-pid>

# Thread dump
${WLP_HOME}/bin/server dump <server-name> --include=thread
```

2. **Review Database Performance**:
```sql
-- Check slow queries
SELECT * FROM SYSIBMADM.LONG_RUNNING_SQL;

-- Check table statistics
RUNSTATS ON TABLE CUSTOMER;
```

3. **Analyze Logs**:
```bash
# Find slow operations
grep -i "duration\|elapsed" ${server.output.dir}/logs/messages.log
```

**Solutions**:

1. **Optimize Database Queries**:
```java
// Use pagination
@Query("SELECT i FROM Inventory i")
List<Inventory> findAll(Pageable pageable);

// Use fetch joins to avoid N+1
@Query("SELECT i FROM Inventory i JOIN FETCH i.category")
List<Inventory> findAllWithCategory();
```

2. **Enable Query Caching**:
```xml
<!-- persistence.xml -->
<property name="eclipselink.cache.shared.default" value="true"/>
<property name="eclipselink.query-results-cache" value="true"/>
```

3. **Increase Thread Pool**:
```xml
<executor 
    id="DefaultExecutor"
    coreThreads="100"
    maxThreads="300"/>
```

4. **Add Database Indexes**:
```sql
CREATE INDEX IDX_INVENTORY_CATEGORY ON INVENTORY(CATEGORY_ID);
CREATE INDEX IDX_INVENTORY_PRICE ON INVENTORY(PRICE);
```

### Issue: High Memory Usage

**Symptoms**:
- OutOfMemoryError
- Frequent garbage collection
- Server becomes unresponsive

**Diagnostic Steps**:

1. **Generate Heap Dump**:
```bash
${WLP_HOME}/bin/server dump <server-name> --include=heap

# Analyze with Eclipse MAT or VisualVM
```

2. **Check Memory Settings**:
```bash
# View current JVM settings
ps aux | grep java | grep Xmx
```

3. **Monitor GC Activity**:
```bash
# Enable GC logging in jvm.options
-Xlog:gc*:file=${server.output.dir}/logs/gc.log:time,level,tags
```

**Solutions**:

1. **Increase Heap Size**:
```properties
# jvm.options
-Xms4096m
-Xmx8192m
```

2. **Optimize Connection Pools**:
```xml
<connectionManager 
    maxPoolSize="200"
    minPoolSize="50"
    reapTime="180s"
    agedTimeout="1800s"/>
```

3. **Clear Caches Periodically**:
```java
@Schedule(hour="2", minute="0")
public void clearCache() {
    entityManager.getEntityManagerFactory()
        .getCache().evictAll();
}
```

4. **Use G1GC**:
```properties
# jvm.options
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
```

## Web Service Issues

### Issue: SOAP Service Not Responding

**Symptoms**:
- WSDL not accessible
- SOAP requests timeout
- 404 errors

**Diagnostic Steps**:

1. **Check Service Endpoint**:
```bash
# Test WSDL access
curl http://localhost:9080/GarageSaleStoreManagerService?wsdl
```

2. **Verify Feature Configuration**:
```xml
<featureManager>
    <feature>jaxws-2.2</feature>
    <feature>jaxb-2.2</feature>
</featureManager>
```

3. **Check Application Deployment**:
```bash
# List deployed applications
${WLP_HOME}/bin/server list <server-name>
```

**Solutions**:

1. **Restart Application**:
```bash
${WLP_HOME}/bin/server stop <server-name>
${WLP_HOME}/bin/server start <server-name>
```

2. **Enable JAX-WS Tracing**:
```xml
<logging 
    traceSpecification="*=info:com.ibm.ws.jaxws.*=all"/>
```

3. **Verify EJB Bindings**:
```bash
grep -i "jndi\|ejb" ${server.output.dir}/logs/messages.log
```

### Issue: REST API 404 Errors

**Symptoms**:
- REST endpoints return 404
- API not accessible

**Solution**:

1. **Check JAX-RS Feature**:
```xml
<featureManager>
    <feature>jaxrs-2.0</feature>
</featureManager>
```

2. **Verify Application Path**:
```java
@ApplicationPath("/api/v1")
public class RestApplication extends Application {
}
```

3. **Check URL Mapping**:
```bash
# Test endpoint
curl -v http://localhost:9080/api/v1/products
```

## Deployment Issues

### Issue: EAR Deployment Failed

**Symptoms**:
```
CWWKZ0002E: An exception occurred while starting the application
```

**Solution**:

1. **Check EAR Structure**:
```bash
# List EAR contents
jar -tf GarageSaleLibertyEAR.ear
```

2. **Verify Dependencies**:
```bash
# Check for missing libraries
grep -i "noclassdef\|classnotfound" ${server.output.dir}/logs/messages.log
```

3. **Validate Deployment Descriptors**:
```bash
# Check application.xml
xmllint --noout META-INF/application.xml
```

### Issue: IBM Cloud Deployment Failed

**Symptoms**:
- Application won't start on IBM Cloud
- Environment variables not set

**Solution**:

1. **Check Application Logs**:
```bash
ibmcloud cf logs garagesale-app --recent
```

2. **Verify Environment Variables**:
```bash
ibmcloud cf env garagesale-app
```

3. **Check Service Bindings**:
```bash
ibmcloud cf services
ibmcloud cf service garagesale-app
```

4. **Increase Memory**:
```bash
ibmcloud cf scale garagesale-app -m 4G
```

## Diagnostic Tools

### Liberty Server Commands

```bash
# Server status
${WLP_HOME}/bin/server status <server-name>

# Generate dump
${WLP_HOME}/bin/server dump <server-name> --include=heap,thread,system

# Package server
${WLP_HOME}/bin/server package <server-name> --include=all

# Validate configuration
${WLP_HOME}/bin/server validate <server-name>
```

### Database Diagnostics

```bash
# DB2 connection test
db2 connect to GSDB user <username> using <password>

# List tables
db2 "SELECT TABNAME FROM SYSCAT.TABLES WHERE TABSCHEMA='<schema>'"

# Check table size
db2 "SELECT CARD FROM SYSCAT.TABLES WHERE TABNAME='CUSTOMER'"

# View active connections
db2 list applications
```

### Redis Diagnostics

```bash
# Redis CLI
redis-cli -h <host> -p 6379 -a <password>

# Check memory
INFO memory

# List keys
KEYS *

# Monitor commands
MONITOR

# Check cluster status
CLUSTER INFO
```

## Log Analysis

### Important Log Files

| Log File | Location | Purpose |
|----------|----------|---------|
| `messages.log` | `${server.output.dir}/logs/` | Main server log |
| `trace.log` | `${server.output.dir}/logs/` | Detailed trace log |
| `console.log` | `${server.output.dir}/logs/` | Console output |
| `ffdc/` | `${server.output.dir}/logs/` | First Failure Data Capture |

### Log Analysis Commands

```bash
# Find errors
grep -i "error\|exception\|failed" messages.log

# Find warnings
grep -i "warn" messages.log

# Find specific component
grep -i "ejb\|jpa\|jdbc" messages.log

# Count errors
grep -c "ERROR" messages.log

# Recent errors
tail -100 messages.log | grep -i error

# Time-based search
grep "2026-01-30.*ERROR" messages.log
```

### Common Error Patterns

```bash
# Database errors
grep "DSRA\|SQL" messages.log

# Memory errors
grep "OutOfMemory\|heap" messages.log

# ClassLoader errors
grep "ClassNotFound\|NoClassDef" messages.log

# Transaction errors
grep "transaction\|rollback" messages.log
```

## Support Resources

### IBM Documentation

- [WebSphere Liberty Documentation](https://www.ibm.com/docs/en/was-liberty)
- [DB2 Documentation](https://www.ibm.com/docs/en/db2)
- [IBM Cloud Documentation](https://cloud.ibm.com/docs)

### Community Resources

- [Liberty GitHub](https://github.com/OpenLiberty)
- [Stack Overflow - WebSphere](https://stackoverflow.com/questions/tagged/websphere-liberty)
- [IBM Developer](https://developer.ibm.com/)

### Getting Help

1. **Collect Diagnostic Information**:
```bash
# Generate server dump
${WLP_HOME}/bin/server dump <server-name> --include=heap,thread,system

# Collect logs
tar -czf logs.tar.gz ${server.output.dir}/logs/
```

2. **Document the Issue**:
   - Error messages
   - Steps to reproduce
   - Environment details
   - Recent changes

3. **Contact Support**:
   - IBM Support Portal
   - Development team
   - Community forums

### Preventive Measures

1. **Regular Monitoring**:
   - Set up application monitoring
   - Configure alerts for errors
   - Track performance metrics

2. **Regular Maintenance**:
   - Update Liberty runtime
   - Apply security patches
   - Optimize databases
   - Clean up logs

3. **Testing**:
   - Test in staging environment
   - Load testing
   - Failover testing
   - Backup/restore testing

---

**Related**: [Configuration Guide](Configuration-Guide.md) | [Deployment Guide](Deployment-Guide-IBM-Cloud.md)