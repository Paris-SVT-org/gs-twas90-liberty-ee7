# GarageSale Enterprise Application - Documentation

Welcome to the GarageSale Enterprise Application documentation. This is a Java EE 7 enterprise application migrated from traditional WebSphere Application Server (tWAS) 9.0 to WebSphere Liberty runtime, designed for deployment on IBM Enterprise Application Service (EASeJ) cloud platform.

## 📋 Table of Contents

1. [Overview](#overview)
2. [Quick Links](#quick-links)
3. [Getting Started](#getting-started)
4. [Documentation Structure](#documentation-structure)

## Overview

**GarageSale** is a comprehensive e-commerce application demonstrating enterprise Java patterns and best practices. It simulates an online marketplace where customers can browse products, manage shopping carts, process payments, and complete transactions.

### Key Features

- 🛒 **Product Catalog Management** - Browse and search products by category and manufacturer
- 👤 **Customer Management** - User registration, authentication, and profile management
- 💳 **Payment Processing** - Credit card validation and transaction processing
- 🏦 **Banking Integration** - Integration with banking services for payment verification
- 📦 **Inventory Management** - Real-time inventory tracking and updates
- 🚚 **Shipping & Tax Calculation** - Dynamic shipping rates and tax calculations
- 📊 **Product Reviews** - Customer product review and rating system
- 💰 **Store Credit System** - Customer loyalty and store credit management
- 🔄 **Session Management** - Redis-backed distributed session caching

### Technology Stack

- **Java EE 7** - Enterprise Java specification
- **WebSphere Liberty** - Lightweight Java application server
- **JPA 2.1** - Java Persistence API with EclipseLink
- **EJB 3.2** - Enterprise JavaBeans for business logic
- **JSF 2.2** - JavaServer Faces for web UI
- **JAX-WS 2.2** - SOAP web services
- **JAX-RS 2.0** - RESTful web services
- **CDI 1.2** - Contexts and Dependency Injection
- **WebSocket 1.1** - Real-time bidirectional communication
- **DB2** - IBM DB2 database
- **Redis** - Distributed session cache
- **Maven** - Build and dependency management

## Quick Links

### Documentation Pages

- **[Architecture Overview](Architecture-Overview.md)** - System architecture, components, and design patterns
- **[Application Components](Application-Components.md)** - Detailed module descriptions
- **[Database Schema](Database-Schema.md)** - Database design and entity relationships
- **[Deployment Guide](Deployment-Guide-IBM-Cloud.md)** - Step-by-step deployment to IBM Cloud
- **[Configuration Guide](Configuration-Guide.md)** - Server and application configuration
- **[API Documentation](API-Documentation.md)** - Web services and REST API reference
- **[Flowcharts](Flowcharts.md)** - Process flows and sequence diagrams
- **[Troubleshooting Guide](Troubleshooting-Guide.md)** - Common issues and solutions

### Diagrams

- [System Architecture Diagram](diagrams/system-architecture.md)
- [Component Diagram](diagrams/component-diagram.md)
- [Deployment Diagram](diagrams/deployment-diagram.md)
- [Database ER Diagram](diagrams/database-er-diagram.md)
- [Checkout Process Flow](diagrams/checkout-flow.md)
- [Session Management Flow](diagrams/session-management-flow.md)

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6+
- IBM DB2 database (or compatible)
- Redis cluster (for session caching)
- WebSphere Liberty 20.0.0.12+

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd gs-twas90-liberty-ee7
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Configure environment variables**
   - Copy `GarageSaleRuntimeUtil/publish/files/server.env.template` to `server.env`
   - Update database and Redis connection settings

4. **Deploy to Liberty**
   ```bash
   # Copy EAR file to Liberty dropins
   cp GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear <liberty-home>/usr/servers/<server-name>/dropins/
   ```

5. **Start the server**
   ```bash
   <liberty-home>/bin/server start <server-name>
   ```

6. **Access the application**
   - Web UI: `http://localhost:9080/GSjsf20LibertyWeb`
   - Admin Console: `http://localhost:9080/WASPersonaWebServicesPMIWeb`

## Documentation Structure

### Core Documentation

| Document | Description |
|----------|-------------|
| **Architecture Overview** | High-level system architecture, design patterns, and technology choices |
| **Application Components** | Detailed description of each module and its responsibilities |
| **Database Schema** | Database tables, relationships, and data model |
| **API Documentation** | SOAP and REST API endpoints, request/response formats |

### Operational Documentation

| Document | Description |
|----------|-------------|
| **Deployment Guide** | Complete deployment instructions for IBM Cloud EASeJ |
| **Configuration Guide** | Server configuration, environment variables, and tuning |
| **Troubleshooting Guide** | Common issues, error messages, and solutions |

### Visual Documentation

| Document | Description |
|----------|-------------|
| **Flowcharts** | Business process flows and sequence diagrams |
| **Architecture Diagrams** | System, component, and deployment diagrams |

## Application Modules

The application is organized into the following Maven modules:

### Core Modules

- **GarageSaleLibertyEAR** - Enterprise Application Archive (EAR) packaging
- **GarageSaleEJB** - Core business logic and EJB session beans
- **GarageSaleDBJPA** - JPA entities for GarageSale database
- **GSJSFLibertyWeb** - JSF web application (customer-facing UI)
- **WASPersonaWebServicesPMIWeb** - Admin web interface and monitoring

### Supporting Modules

- **WSBankEJB** - Banking service integration
- **WSBankDBJPA** - JPA entities for banking database
- **CreditCardEJB** - Credit card processing logic
- **CreditCardDBJPA** - JPA entities for credit card database
- **GarageSaleWSClient** - Web service client utilities
- **ProdReviewTaxShipRateEJB** - Product reviews, tax, and shipping calculations
- **GarageSaleRuntimeUtil** - Runtime utilities and configuration files

## Key Features Explained

### Multi-Database Architecture

The application uses three separate databases:
- **GSDB** - Main GarageSale database (products, customers, orders)
- **CCDB** - Credit card database (payment information)
- **WSBANKDB** - Banking database (accounts, transactions)

This separation provides:
- Enhanced security through data isolation
- Better scalability and performance
- Compliance with PCI-DSS requirements

### Redis Session Caching

The application uses Redis for distributed session management:
- High-performance in-memory session storage
- Session sharing across multiple Liberty instances
- Automatic failover with Redis cluster
- Reduced database load

### Web Services Architecture

The application exposes both SOAP and REST APIs:
- **SOAP Services** - Traditional enterprise integration
- **REST Services** - Modern API access
- **WebSocket** - Real-time updates and notifications

## Support and Contribution

### Getting Help

- Check the [Troubleshooting Guide](Troubleshooting-Guide.md)
- Review server logs in `<liberty-home>/usr/servers/<server-name>/logs/`
- Contact the development team

### Contributing

Please follow the standard Git workflow:
1. Create a feature branch
2. Make your changes
3. Submit a pull request
4. Ensure all tests pass

## Version Information

- **Application Version**: 1.0-SNAPSHOT
- **Java EE Version**: 7.0
- **Target Runtime**: WebSphere Liberty 20.0.0.12+
- **Build Tool**: Maven 3.6+
- **Java Version**: 8+

## License

Copyright © IBM Corporation. All rights reserved.

---

**Last Updated**: January 2026  
**Maintained By**: WebSphere SVT Team