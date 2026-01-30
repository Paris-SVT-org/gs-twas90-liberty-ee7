# Deployment Guide - IBM Enterprise Application Service (EASeJ)

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Architecture on IBM Cloud](#architecture-on-ibm-cloud)
4. [Pre-Deployment Checklist](#pre-deployment-checklist)
5. [Step-by-Step Deployment](#step-by-step-deployment)
6. [Configuration](#configuration)
7. [Post-Deployment Verification](#post-deployment-verification)
8. [Monitoring and Management](#monitoring-and-management)
9. [Troubleshooting](#troubleshooting)
10. [Rollback Procedures](#rollback-procedures)

## Overview

This guide provides comprehensive instructions for deploying the GarageSale application to **IBM Enterprise Application Service (EASeJ)** cloud platform. EASeJ provides a managed WebSphere Liberty runtime environment optimized for enterprise Java applications.

### Deployment Model

```
┌─────────────────────────────────────────────────────────────────┐
│                    IBM Cloud Platform                            │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │         IBM Enterprise Application Service (EASeJ)        │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │          WebSphere Liberty Runtime                  │  │  │
│  │  │  ┌──────────────────────────────────────────────┐   │  │  │
│  │  │  │      GarageSale Application (EAR)            │   │  │  │
│  │  │  │  - GSJSFLibertyWeb (WAR)                     │   │  │  │
│  │  │  │  - GarageSaleEJB (EJB-JAR)                   │   │  │  │
│  │  │  │  - WSBankEJB (EJB-JAR)                       │   │  │  │
│  │  │  │  - CreditCardEJB (EJB-JAR)                   │   │  │  │
│  │  │  │  - WASPersonaWebServicesPMIWeb (WAR)         │   │  │  │
│  │  │  └──────────────────────────────────────────────┘   │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              IBM Cloud Databases for DB2                  │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │  │
│  │  │    GSDB      │  │  WSBANKDB    │  │    CCDB      │   │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │         IBM Cloud Databases for Redis                     │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │           Redis Cluster (Session Cache)             │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Prerequisites

### Required Tools

1. **IBM Cloud CLI**
   ```bash
   # Install IBM Cloud CLI
   curl -fsSL https://clis.cloud.ibm.com/install/linux | sh
   
   # Verify installation
   ibmcloud --version
   ```

2. **Maven 3.6+**
   ```bash
   mvn --version
   ```

3. **Java 8 or higher**
   ```bash
   java -version
   ```

4. **Git**
   ```bash
   git --version
   ```

### IBM Cloud Account Setup

1. **Create IBM Cloud Account**
   - Sign up at https://cloud.ibm.com
   - Verify email address

2. **Install Required Plugins**
   ```bash
   # Install Cloud Foundry plugin
   ibmcloud cf install
   
   # Install Container Registry plugin
   ibmcloud plugin install container-registry
   
   # Install Kubernetes Service plugin
   ibmcloud plugin install kubernetes-service
   ```

3. **Login to IBM Cloud**
   ```bash
   ibmcloud login
   
   # Or with SSO
   ibmcloud login --sso
   
   # Select your account and region
   ibmcloud target --cf
   ```

### Required IBM Cloud Services

1. **IBM Enterprise Application Service (EASeJ)**
   - Service plan: Standard or Enterprise
   - Runtime: WebSphere Liberty

2. **IBM Cloud Databases for DB2**
   - Three database instances (GSDB, WSBANKDB, CCDB)
   - Plan: Standard or Enterprise

3. **IBM Cloud Databases for Redis**
   - Redis cluster for session caching
   - Plan: Standard (minimum 3 nodes)

## Architecture on IBM Cloud

### Network Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Internet / Public Network                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    IBM Cloud Load Balancer                       │
│                    (SSL Termination)                             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    IBM Cloud VPC / Private Network               │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              EASeJ Application Instances                  │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐               │  │
│  │  │ Liberty  │  │ Liberty  │  │ Liberty  │               │  │
│  │  │ Instance │  │ Instance │  │ Instance │               │  │
│  │  │    1     │  │    2     │  │    3     │               │  │
│  │  └──────────┘  └──────────┘  └──────────┘               │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                   │
│                    ┌─────────┼─────────┐                        │
│                    ▼         ▼         ▼                        │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Private Service Endpoints                    │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │  │
│  │  │ Redis Cluster│  │  DB2 GSDB    │  │ DB2 WSBANKDB │   │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘   │  │
│  │                    ┌──────────────┐                       │  │
│  │                    │  DB2 CCDB    │                       │  │
│  │                    └──────────────┘                       │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Pre-Deployment Checklist

### 1. Build Verification

```bash
# Clone repository
git clone <repository-url>
cd gs-twas90-liberty-ee7

# Clean build
mvn clean install

# Verify EAR file created
ls -lh GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear
```

### 2. Database Setup

#### Create DB2 Instances

```bash
# Create GSDB instance
ibmcloud resource service-instance-create gsdb-instance \
  databases-for-db2 standard us-south \
  -p '{"members_memory_allocation_mb": "4096", "members_disk_allocation_mb": "20480"}'

# Create WSBANKDB instance
ibmcloud resource service-instance-create wsbankdb-instance \
  databases-for-db2 standard us-south \
  -p '{"members_memory_allocation_mb": "4096", "members_disk_allocation_mb": "20480"}'

# Create CCDB instance
ibmcloud resource service-instance-create ccdb-instance \
  databases-for-db2 standard us-south \
  -p '{"members_memory_allocation_mb": "4096", "members_disk_allocation_mb": "20480"}'
```

#### Create Service Credentials

```bash
# Create credentials for GSDB
ibmcloud resource service-key-create gsdb-credentials \
  Manager --instance-name gsdb-instance

# Create credentials for WSBANKDB
ibmcloud resource service-key-create wsbankdb-credentials \
  Manager --instance-name wsbankdb-instance

# Create credentials for CCDB
ibmcloud resource service-key-create ccdb-credentials \
  Manager --instance-name ccdb-instance

# View credentials
ibmcloud resource service-key gsdb-credentials
```

#### Initialize Database Schemas

```bash
# Connect to GSDB and run schema scripts
db2 connect to GSDB user <username> using <password>
db2 -tvf database/scripts/gsdb-schema.sql

# Connect to WSBANKDB and run schema scripts
db2 connect to WSBANKDB user <username> using <password>
db2 -tvf database/scripts/wsbankdb-schema.sql

# Connect to CCDB and run schema scripts
db2 connect to CCDB user <username> using <password>
db2 -tvf database/scripts/ccdb-schema.sql
```

### 3. Redis Setup

```bash
# Create Redis instance
ibmcloud resource service-instance-create redis-session-cache \
  databases-for-redis standard us-south \
  -p '{"members_memory_allocation_mb": "2048", "members_disk_allocation_mb": "10240"}'

# Create service credentials
ibmcloud resource service-key-create redis-credentials \
  Manager --instance-name redis-session-cache

# View credentials
ibmcloud resource service-key redis-credentials
```

### 4. Prepare Configuration Files

Create `server.env` with database and Redis connection details:

```properties
# HTTP/HTTPS Ports
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443

# Security Configuration
KEYSTORE_DEFAULT_PASSWORD=<your-keystore-password>
QS_SECURITY_USERNAME=admin
QS_SECURITY_PASSWORD=<your-admin-password>

# GSDB Configuration
APP_GSDB_HOSTNAME=<gsdb-hostname>
APP_GSDB_PORT=50001
APP_GSDB_NAME=GSDB
APP_GSDB_USER=<gsdb-username>
APP_GSDB_PASSWORD=<gsdb-password>

# WSBANKDB Configuration
APP_WSBANKDB_HOSTNAME=<wsbankdb-hostname>
APP_WSBANKDB_PORT=50001
APP_WSBANKDB_NAME=WSBANKDB
APP_WSBANKDB_USER=<wsbankdb-username>
APP_WSBANKDB_PASSWORD=<wsbankdb-password>

# CCDB Configuration
APP_CCDB_HOSTNAME=<ccdb-hostname>
APP_CCDB_PORT=50001
APP_CCDB_NAME=CCDB
APP_CCDB_USER=<ccdb-username>
APP_CCDB_PASSWORD=<ccdb-password>

# Redis Configuration
REDIS_URI=redis://<redis-host1>:6379,<redis-host2>:6379,<redis-host3>:6379
REDIS_SSL_ENABLED=true
REDIS_PASSWORD=<redis-password>
```

## Step-by-Step Deployment

### Step 1: Create EASeJ Application

```bash
# Create application in IBM Cloud
ibmcloud cf push garagesale-app \
  --no-start \
  -p GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear \
  -b liberty-for-java \
  -m 2G \
  -k 1G \
  --instances 3
```

### Step 2: Bind Services

```bash
# Bind DB2 services
ibmcloud cf bind-service garagesale-app gsdb-instance
ibmcloud cf bind-service garagesale-app wsbankdb-instance
ibmcloud cf bind-service garagesale-app ccdb-instance

# Bind Redis service
ibmcloud cf bind-service garagesale-app redis-session-cache
```

### Step 3: Set Environment Variables

```bash
# Set environment variables from server.env
ibmcloud cf set-env garagesale-app DEFAULT_HTTP_PORT 9080
ibmcloud cf set-env garagesale-app DEFAULT_HTTPS_PORT 9443
ibmcloud cf set-env garagesale-app QS_SECURITY_USERNAME admin
ibmcloud cf set-env garagesale-app QS_SECURITY_PASSWORD <password>

# Set database configurations
ibmcloud cf set-env garagesale-app APP_GSDB_HOSTNAME <hostname>
ibmcloud cf set-env garagesale-app APP_GSDB_PORT 50001
ibmcloud cf set-env garagesale-app APP_GSDB_NAME GSDB
ibmcloud cf set-env garagesale-app APP_GSDB_USER <username>
ibmcloud cf set-env garagesale-app APP_GSDB_PASSWORD <password>

# Repeat for WSBANKDB and CCDB...

# Set Redis configuration
ibmcloud cf set-env garagesale-app REDIS_URI "redis://<host1>:6379,<host2>:6379"
ibmcloud cf set-env garagesale-app REDIS_SSL_ENABLED true
ibmcloud cf set-env garagesale-app REDIS_PASSWORD <password>
```

### Step 4: Upload Required Libraries

```bash
# Create shared resources directory structure
mkdir -p shared-resources/db2drivers
mkdir -p shared-resources/redis
mkdir -p shared-resources/jaxrsThirdPartyJars

# Copy DB2 drivers
cp db2jcc4.jar shared-resources/db2drivers/
cp db2jcc_license_cu.jar shared-resources/db2drivers/

# Copy Redis client libraries
cp jedis-4.3.1.jar shared-resources/redis/
cp commons-pool2-2.11.1.jar shared-resources/redis/
cp slf4j-api-2.0.7.jar shared-resources/redis/

# Upload to IBM Cloud
ibmcloud cf push garagesale-app-resources \
  -p shared-resources \
  --no-route \
  --no-start
```

### Step 5: Configure Liberty Server

Create `server.xml` configuration (already included in GarageSaleRuntimeUtil):

```bash
# The server.xml is packaged with the application
# Verify it's in GarageSaleRuntimeUtil/publish/servers/server.xml
```

### Step 6: Deploy Application

```bash
# Restage application with new configuration
ibmcloud cf restage garagesale-app

# Start application
ibmcloud cf start garagesale-app
```

### Step 7: Configure Auto-Scaling (Optional)

```bash
# Install auto-scaling plugin
ibmcloud plugin install auto-scaling

# Create auto-scaling policy
cat > autoscaling-policy.json <<EOF
{
  "instance_min_count": 2,
  "instance_max_count": 10,
  "scaling_rules": [
    {
      "metric_type": "memoryutil",
      "breach_duration_secs": 120,
      "threshold": 80,
      "operator": ">",
      "cool_down_secs": 300,
      "adjustment": "+1"
    },
    {
      "metric_type": "memoryutil",
      "breach_duration_secs": 120,
      "threshold": 30,
      "operator": "<",
      "cool_down_secs": 300,
      "adjustment": "-1"
    }
  ]
}
EOF

# Attach policy
ibmcloud cf attach-autoscaling-policy garagesale-app autoscaling-policy.json
```

## Configuration

### Liberty Server Configuration

The `server.xml` configuration is located in `GarageSaleRuntimeUtil/publish/servers/server.xml`. Key configurations:

#### Features

```xml
<featureManager>
    <feature>jndi-1.0</feature>
    <feature>localConnector-1.0</feature>
    <feature>ssl-1.0</feature>
    <feature>jaxws-2.2</feature>
    <feature>jaxb-2.2</feature>
    <feature>managedBeans-1.0</feature>
    <feature>monitor-1.0</feature>
    <feature>appSecurity-2.0</feature>
    <feature>sessionCache-1.0</feature>
    <feature>jdbc-4.1</feature>
    <feature>concurrent-1.0</feature>
    <feature>websocket-1.1</feature>
    <feature>el-3.0</feature>
    <feature>beanValidation-1.1</feature>
    <feature>cdi-1.2</feature>
    <feature>jsf-2.2</feature>
    <feature>ejbLite-3.2</feature>
    <feature>jpa-2.1</feature>
    <feature>jaxrs-2.0</feature>
</featureManager>
```

#### DataSource Configuration

```xml
<dataSource id="jdbc/gsdb" jdbcDriverRef="GSDBProvider" 
            jndiName="jdbc/gsdb" type="javax.sql.XADataSource">
    <properties databaseName="${env.APP_GSDB_NAME}" 
                driverType="4"
                password="${env.APP_GSDB_PASSWORD}"
                portNumber="${env.APP_GSDB_PORT}"
                serverName="${env.APP_GSDB_HOSTNAME}" 
                user="${env.APP_GSDB_USER}" />
    <connectionManager maxPoolSize="300" minPoolSize="50" />
</dataSource>
```

#### Redis Session Cache

```xml
<library id="RedisLib">
    <fileset dir="${shared.resource.dir}/redis" includes="*.jar"/>
</library>

<httpSessionCache libraryRef="RedisLib">
    <properties
        uri="${env.REDIS_URI}"
        sslEnabled="${env.REDIS_SSL_ENABLED}"
        password="${env.REDIS_PASSWORD}"/>
</httpSessionCache>

<httpSession cloneId="localhost_cloneID" storageRef="httpSessionCache"/>
```

### Environment-Specific Configuration

Create different `server.env` files for each environment:

- `server.env.dev` - Development
- `server.env.test` - Testing
- `server.env.prod` - Production

## Post-Deployment Verification

### 1. Check Application Status

```bash
# View application status
ibmcloud cf app garagesale-app

# View recent logs
ibmcloud cf logs garagesale-app --recent

# Stream logs
ibmcloud cf logs garagesale-app
```

### 2. Verify Endpoints

```bash
# Get application URL
APP_URL=$(ibmcloud cf app garagesale-app | grep routes | awk '{print $2}')

# Test web UI
curl -I https://$APP_URL/GSjsf20LibertyWeb

# Test admin console
curl -I https://$APP_URL/WASPersonaWebServicesPMIWeb

# Test SOAP service
curl -X POST https://$APP_URL/GarageSaleStoreManagerService \
  -H "Content-Type: text/xml" \
  -d @test-soap-request.xml
```

### 3. Database Connectivity Test

```bash
# Check database connections in logs
ibmcloud cf logs garagesale-app --recent | grep "DSRA"

# Should see successful connection messages
```

### 4. Redis Connectivity Test

```bash
# Check Redis connection in logs
ibmcloud cf logs garagesale-app --recent | grep -i redis

# Test session creation
curl -c cookies.txt https://$APP_URL/GSjsf20LibertyWeb/login
curl -b cookies.txt https://$APP_URL/GSjsf20LibertyWeb/dashboard
```

### 5. Health Check

```bash
# Liberty health check endpoint
curl https://$APP_URL/health

# Expected response:
# {"checks":[{"name":"database","state":"UP"},{"name":"redis","state":"UP"}],"outcome":"UP"}
```

## Monitoring and Management

### Application Monitoring

```bash
# View application metrics
ibmcloud cf app garagesale-app --guid
ibmcloud cf curl /v3/apps/<app-guid>/stats

# View application events
ibmcloud cf events garagesale-app
```

### Log Management

```bash
# Stream logs to file
ibmcloud cf logs garagesale-app > app-logs.txt

# Filter logs by component
ibmcloud cf logs garagesale-app | grep "GarageSaleEJB"

# View crash logs
ibmcloud cf crashlogs garagesale-app
```

### Performance Monitoring

Access IBM Cloud Monitoring dashboard:
1. Navigate to IBM Cloud Console
2. Select your application
3. Click "Monitoring" tab
4. View metrics: CPU, Memory, Response Time, Throughput

### Database Monitoring

```bash
# View DB2 metrics
ibmcloud resource service-instance gsdb-instance --output json

# Access DB2 console
# Navigate to IBM Cloud Console > Databases > gsdb-instance > Manage
```

## Troubleshooting

### Common Issues

#### 1. Application Won't Start

```bash
# Check logs for errors
ibmcloud cf logs garagesale-app --recent | grep ERROR

# Common causes:
# - Missing environment variables
# - Database connection failure
# - Redis connection failure
# - Insufficient memory
```

**Solution**:
```bash
# Verify environment variables
ibmcloud cf env garagesale-app

# Increase memory if needed
ibmcloud cf scale garagesale-app -m 4G

# Restart application
ibmcloud cf restart garagesale-app
```

#### 2. Database Connection Errors

```bash
# Check database service status
ibmcloud resource service-instance gsdb-instance

# Test database connectivity
db2 connect to GSDB user <username> using <password>
```

**Solution**:
```bash
# Verify database credentials
ibmcloud resource service-key gsdb-credentials

# Update environment variables if needed
ibmcloud cf set-env garagesale-app APP_GSDB_PASSWORD <new-password>
ibmcloud cf restage garagesale-app
```

#### 3. Redis Connection Errors

```bash
# Check Redis service status
ibmcloud resource service-instance redis-session-cache

# View Redis logs
ibmcloud cdb logs redis-session-cache
```

**Solution**:
```bash
# Verify Redis credentials
ibmcloud resource service-key redis-credentials

# Update Redis configuration
ibmcloud cf set-env garagesale-app REDIS_URI <new-uri>
ibmcloud cf restage garagesale-app
```

#### 4. Out of Memory Errors

```bash
# Check memory usage
ibmcloud cf app garagesale-app
```

**Solution**:
```bash
# Increase memory allocation
ibmcloud cf scale garagesale-app -m 4G -k 2G

# Or scale horizontally
ibmcloud cf scale garagesale-app -i 5
```

### Debug Mode

Enable debug logging:

```bash
# Set trace specification
ibmcloud cf set-env garagesale-app WLP_LOGGING_TRACE_SPECIFICATION "*=info:com.ibm.websphere.svt.gs.*=all"

# Restage application
ibmcloud cf restage garagesale-app

# View detailed logs
ibmcloud cf logs garagesale-app
```

## Rollback Procedures

### Rollback to Previous Version

```bash
# List previous versions
ibmcloud cf app garagesale-app --guid
ibmcloud cf curl /v3/apps/<app-guid>/revisions

# Rollback to specific revision
ibmcloud cf rollback garagesale-app --version <revision-number>
```

### Emergency Rollback

```bash
# Stop current application
ibmcloud cf stop garagesale-app

# Deploy previous EAR file
ibmcloud cf push garagesale-app \
  -p GarageSaleLibertyEAR-backup.ear \
  -b liberty-for-java

# Verify deployment
ibmcloud cf app garagesale-app
```

### Database Rollback

```bash
# Restore database from backup
db2 restore database GSDB from /backup/path taken at <timestamp>

# Verify data integrity
db2 "SELECT COUNT(*) FROM CUSTOMER"
```

## Best Practices

1. **Use Blue-Green Deployment**
   - Deploy to staging environment first
   - Test thoroughly
   - Switch traffic to new version
   - Keep old version running for quick rollback

2. **Implement Health Checks**
   - Configure liveness and readiness probes
   - Monitor application health continuously

3. **Enable Auto-Scaling**
   - Configure based on CPU and memory metrics
   - Set appropriate min/max instance counts

4. **Regular Backups**
   - Schedule daily database backups
   - Test restore procedures regularly

5. **Security**
   - Use IBM Cloud Secrets Manager for credentials
   - Enable SSL/TLS for all connections
   - Rotate passwords regularly

6. **Monitoring**
   - Set up alerts for critical metrics
   - Monitor application logs continuously
   - Track performance trends

## Additional Resources

- [IBM Cloud Documentation](https://cloud.ibm.com/docs)
- [WebSphere Liberty Documentation](https://www.ibm.com/docs/en/was-liberty)
- [IBM Cloud CLI Reference](https://cloud.ibm.com/docs/cli)
- [DB2 on Cloud Documentation](https://cloud.ibm.com/docs/Db2onCloud)
- [Redis on IBM Cloud](https://cloud.ibm.com/docs/databases-for-redis)

---

**Next**: [Configuration Guide](Configuration-Guide.md) | [Troubleshooting Guide](Troubleshooting-Guide.md)