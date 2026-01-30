# GarageSale Application - Complete Documentation

## 📚 Documentation Overview

This repository contains comprehensive documentation for the **GarageSale Enterprise Application** - a Java EE 7 e-commerce application migrated from traditional WebSphere Application Server (tWAS) 9.0 to WebSphere Liberty runtime, designed for deployment on IBM Enterprise Application Service (EASeJ) cloud platform.

## 🎯 Quick Navigation

### Getting Started
- **[Home](Home.md)** - Start here for an overview and quick links
- **[Architecture Overview](Architecture-Overview.md)** - Understand the system design
- **[Application Components](Application-Components.md)** - Explore the modules and their responsibilities

### Development & Configuration
- **[Database Schema](Database-Schema.md)** - Complete database documentation with ER diagrams
- **[Configuration Guide](Configuration-Guide.md)** - Server and application configuration
- **[API Documentation](API-Documentation.md)** - SOAP, REST, and WebSocket API reference

### Deployment & Operations
- **[Deployment Guide - IBM Cloud](Deployment-Guide-IBM-Cloud.md)** - Step-by-step deployment to IBM Cloud EASeJ
- **[Troubleshooting Guide](Troubleshooting-Guide.md)** - Common issues and solutions

### Visual Documentation
- **[Flowcharts](Flowcharts.md)** - Process flows and sequence diagrams

## 📖 Documentation Structure

```
wiki/
├── README.md (this file)
├── Home.md
├── Architecture-Overview.md
├── Application-Components.md
├── Database-Schema.md
├── Deployment-Guide-IBM-Cloud.md
├── Configuration-Guide.md
├── API-Documentation.md
├── Troubleshooting-Guide.md
└── Flowcharts.md
```

## 🏗️ Application Architecture

### Technology Stack
- **Runtime**: WebSphere Liberty 20.0.0.12+
- **Java Platform**: Java EE 7
- **Build Tool**: Maven 3.6+
- **Databases**: IBM DB2 (3 separate databases)
- **Session Cache**: Redis Cluster
- **Web Framework**: JSF 2.2, JAX-RS 2.0, JAX-WS 2.2

### Key Features
- 🛒 E-commerce product catalog and shopping cart
- 💳 Credit card payment processing
- 🏦 Banking service integration
- 📦 Inventory management
- 👤 Customer account management
- 🔄 Redis-backed distributed sessions
- 🌐 SOAP and REST web services
- 📊 Real-time WebSocket updates

## 📋 Documentation by Role

### For Developers

1. **Getting Started**
   - Read [Home](Home.md) for overview
   - Review [Architecture Overview](Architecture-Overview.md)
   - Study [Application Components](Application-Components.md)
   - Understand [Database Schema](Database-Schema.md)

2. **Development**
   - Follow [Configuration Guide](Configuration-Guide.md) for local setup
   - Reference [API Documentation](API-Documentation.md) for integration
   - Use [Flowcharts](Flowcharts.md) to understand business logic

3. **Troubleshooting**
   - Consult [Troubleshooting Guide](Troubleshooting-Guide.md)

### For DevOps Engineers

1. **Deployment**
   - Follow [Deployment Guide - IBM Cloud](Deployment-Guide-IBM-Cloud.md)
   - Configure using [Configuration Guide](Configuration-Guide.md)

2. **Operations**
   - Monitor using guidelines in [Troubleshooting Guide](Troubleshooting-Guide.md)
   - Reference [Architecture Overview](Architecture-Overview.md) for system design

### For Architects

1. **System Design**
   - Review [Architecture Overview](Architecture-Overview.md)
   - Study [Database Schema](Database-Schema.md)
   - Analyze [Flowcharts](Flowcharts.md)

2. **Integration**
   - Reference [API Documentation](API-Documentation.md)
   - Review [Application Components](Application-Components.md)

### For QA Engineers

1. **Testing**
   - Use [API Documentation](API-Documentation.md) for API testing
   - Reference [Flowcharts](Flowcharts.md) for test scenarios
   - Follow [Configuration Guide](Configuration-Guide.md) for test environment setup

2. **Issue Reporting**
   - Use [Troubleshooting Guide](Troubleshooting-Guide.md) for diagnostics

## 🚀 Quick Start Guide

### Prerequisites
```bash
# Required software
- Java 8 or higher
- Maven 3.6+
- IBM DB2 database
- Redis cluster
- WebSphere Liberty 20.0.0.12+
```

### Build Application
```bash
# Clone repository
git clone <repository-url>
cd gs-twas90-liberty-ee7

# Build with Maven
mvn clean install

# EAR file location
ls -lh GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear
```

### Configure Environment
```bash
# Copy and edit server.env
cp GarageSaleRuntimeUtil/publish/files/server.env.template server.env
vi server.env

# Update database and Redis settings
```

### Deploy to Liberty
```bash
# Copy EAR to Liberty
cp GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear \
   ${WLP_HOME}/usr/servers/<server-name>/dropins/

# Start server
${WLP_HOME}/bin/server start <server-name>
```

### Access Application
```
Web UI: http://localhost:9080/GSjsf20LibertyWeb
Admin Console: http://localhost:9080/WASPersonaWebServicesPMIWeb
SOAP WSDL: http://localhost:9080/GarageSaleStoreManagerService?wsdl
REST API: http://localhost:9080/api/v1
```

## 📊 Application Modules

### Core Modules
- **GarageSaleLibertyEAR** - Enterprise Application Archive
- **GarageSaleEJB** - Core business logic (EJB session beans)
- **GarageSaleDBJPA** - JPA entities for main database
- **GSJSFLibertyWeb** - Customer-facing web UI (JSF)
- **WASPersonaWebServicesPMIWeb** - Admin interface

### Supporting Modules
- **WSBankEJB** - Banking service integration
- **WSBankDBJPA** - Banking database entities
- **CreditCardEJB** - Payment processing
- **CreditCardDBJPA** - Credit card database entities
- **ProdReviewTaxShipRateEJB** - Auxiliary services
- **GarageSaleWSClient** - Web service client utilities
- **GarageSaleRuntimeUtil** - Runtime configuration files

## 🗄️ Database Architecture

### Three-Database Design

1. **GSDB (GarageSale Database)**
   - Products, customers, orders
   - Categories and manufacturers
   - Store credits and settings

2. **WSBANKDB (Banking Database)**
   - Bank accounts
   - Sub-accounts
   - Transaction history

3. **CCDB (Credit Card Database)**
   - Credit card information (encrypted)
   - Card transactions
   - PCI-DSS compliant

## 🔧 Configuration Highlights

### Environment Variables
```properties
# HTTP Ports
DEFAULT_HTTP_PORT=9080
DEFAULT_HTTPS_PORT=9443

# Database Configuration
APP_GSDB_HOSTNAME=localhost
APP_GSDB_PORT=50000
APP_GSDB_NAME=GSDB

# Redis Configuration
REDIS_URI=redis://localhost:6379
REDIS_SSL_ENABLED=false
```

### Liberty Features
```xml
<featureManager>
    <feature>ejbLite-3.2</feature>
    <feature>jpa-2.1</feature>
    <feature>jsf-2.2</feature>
    <feature>jaxws-2.2</feature>
    <feature>jaxrs-2.0</feature>
    <feature>cdi-1.2</feature>
    <feature>sessionCache-1.0</feature>
</featureManager>
```

## 🌐 API Overview

### SOAP Web Services
- **GarageSaleStoreManager** - Main store operations
- **WSBankManager** - Banking operations
- Endpoint: `/GarageSaleStoreManagerService`

### REST APIs
- **Products API** - `/api/v1/products`
- **Cart API** - `/api/v1/cart`
- **Checkout API** - `/api/v1/checkout`
- **Customer API** - `/api/v1/customers`
- **Orders API** - `/api/v1/orders`

### WebSocket Endpoints
- **Inventory Updates** - `/ws/inventory`
- **Order Status** - `/ws/orders`

## 🔍 Troubleshooting Quick Reference

### Common Issues

| Issue | Quick Fix |
|-------|-----------|
| Application won't start | Check logs: `${server.output.dir}/logs/messages.log` |
| Database connection failed | Verify credentials in `server.env` |
| Redis connection error | Check `REDIS_URI` and Redis server status |
| Port already in use | Change ports in `server.env` |
| Out of memory | Increase heap size in `jvm.options` |

### Diagnostic Commands
```bash
# Server status
${WLP_HOME}/bin/server status <server-name>

# Generate dump
${WLP_HOME}/bin/server dump <server-name> --include=heap,thread

# View logs
tail -f ${server.output.dir}/logs/messages.log

# Test database
db2 connect to GSDB user <username> using <password>

# Test Redis
redis-cli -h <host> -p 6379 ping
```

## 📈 Performance Tuning

### JVM Options
```properties
-Xms2048m
-Xmx4096m
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
```

### Connection Pools
```xml
<connectionManager 
    maxPoolSize="300" 
    minPoolSize="50"
    connectionTimeout="30s"/>
```

### Session Configuration
```xml
<httpSession 
    invalidationTimeout="1800"
    storageRef="httpSessionCache"/>
```

## 🔐 Security Considerations

- SSL/TLS for all connections
- Encrypted credit card storage (PCI-DSS compliant)
- Separate databases for sensitive data
- Secure session management with Redis
- Basic authentication for admin operations
- JWT tokens for REST API

## 📞 Support and Resources

### Documentation
- [IBM WebSphere Liberty Docs](https://www.ibm.com/docs/en/was-liberty)
- [IBM Cloud Docs](https://cloud.ibm.com/docs)
- [DB2 Documentation](https://www.ibm.com/docs/en/db2)

### Community
- [OpenLiberty GitHub](https://github.com/OpenLiberty)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/websphere-liberty)
- [IBM Developer](https://developer.ibm.com/)

### Getting Help
1. Check [Troubleshooting Guide](Troubleshooting-Guide.md)
2. Review server logs
3. Search community forums
4. Contact IBM Support

## 📝 Version Information

- **Application Version**: 1.0-SNAPSHOT
- **Java EE Version**: 7.0
- **Target Runtime**: WebSphere Liberty 20.0.0.12+
- **Build Tool**: Maven 3.6+
- **Java Version**: 8+

## 🤝 Contributing

Please follow standard Git workflow:
1. Create feature branch
2. Make changes
3. Submit pull request
4. Ensure all tests pass

## 📄 License

Copyright © IBM Corporation. All rights reserved.

---

**Last Updated**: January 2026  
**Maintained By**: WebSphere SVT Team

For detailed information on any topic, please refer to the specific documentation pages linked above.