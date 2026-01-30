# Architecture Overview

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Architectural Patterns](#architectural-patterns)
3. [Technology Stack](#technology-stack)
4. [Component Architecture](#component-architecture)
5. [Data Architecture](#data-architecture)
6. [Integration Architecture](#integration-architecture)
7. [Security Architecture](#security-architecture)
8. [Scalability and Performance](#scalability-and-performance)

## System Architecture

The GarageSale application follows a **multi-tier enterprise architecture** pattern, designed for scalability, maintainability, and high availability.

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Layer                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ Web Browser  │  │ Mobile App   │  │ External API │          │
│  │   (JSF UI)   │  │   Client     │  │   Consumer   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┼─────────┐
                    │         │         │
                    ▼         ▼         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Presentation Layer                            │
│  ┌──────────────────────┐  ┌──────────────────────┐            │
│  │  GSJSFLibertyWeb     │  │ WASPersonaWebServices│            │
│  │  (JSF 2.2 Web App)   │  │  PMIWeb (Admin UI)   │            │
│  │  - Managed Beans     │  │  - Monitoring        │            │
│  │  - View Controllers  │  │  - Management        │            │
│  └──────────────────────┘  └──────────────────────┘            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Service Layer                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   JAX-WS     │  │   JAX-RS     │  │  WebSocket   │          │
│  │ SOAP Services│  │ REST Services│  │   Services   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Business Logic Layer                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              GarageSaleEJB (Session Beans)               │  │
│  │  ┌────────────────┐  ┌────────────────┐  ┌────────────┐ │  │
│  │  │ GarageSale     │  │  Customer      │  │ Inventory  │ │  │
│  │  │ StoreManager   │  │  Management    │  │ Management │ │  │
│  │  └────────────────┘  └────────────────┘  └────────────┘ │  │
│  └──────────────────────────────────────────────────────────┘  │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐ │
│  │   WSBankEJB      │  │  CreditCardEJB   │  │ ProdReview   │ │
│  │ (Banking Logic)  │  │ (Payment Logic)  │  │ TaxShipRate  │ │
│  └──────────────────┘  └──────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Data Access Layer (JPA)                       │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐ │
│  │ GarageSaleDBJPA  │  │  WSBankDBJPA     │  │ CreditCardDB │ │
│  │ (JPA Entities)   │  │  (JPA Entities)  │  │ JPA Entities │ │
│  └──────────────────┘  └──────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┼─────────┐
                    ▼         ▼         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Data Layer                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   GSDB       │  │  WSBANKDB    │  │    CCDB      │          │
│  │  (DB2)       │  │   (DB2)      │  │   (DB2)      │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                          │
│  ┌──────────────────────┐  ┌──────────────────────┐            │
│  │   Redis Cluster      │  │  WebSphere Liberty   │            │
│  │ (Session Cache)      │  │  (Application Server)│            │
│  └──────────────────────┘  └──────────────────────┘            │
└─────────────────────────────────────────────────────────────────┘
```

## Architectural Patterns

### 1. **Multi-Tier Architecture**

The application implements a classic n-tier architecture:

- **Presentation Tier**: JSF web pages, managed beans, view controllers
- **Service Tier**: SOAP/REST web services, WebSocket endpoints
- **Business Tier**: EJB session beans containing business logic
- **Data Access Tier**: JPA entities and repositories
- **Data Tier**: DB2 databases

**Benefits**:
- Clear separation of concerns
- Independent scaling of tiers
- Easier maintenance and testing
- Technology flexibility per tier

### 2. **Domain-Driven Design (DDD)**

The application is organized around business domains:

- **GarageSale Domain**: Core e-commerce functionality
- **Banking Domain**: Financial transactions and accounts
- **Credit Card Domain**: Payment processing
- **Product Review Domain**: Customer feedback system

**Benefits**:
- Business logic aligned with domain concepts
- Ubiquitous language across team
- Bounded contexts for each domain

### 3. **Service-Oriented Architecture (SOA)**

Business capabilities exposed as services:

- **SOAP Web Services**: Enterprise integration
- **REST APIs**: Modern API access
- **WebSocket Services**: Real-time communication

**Benefits**:
- Reusable business services
- Platform-independent integration
- Loose coupling between components

### 4. **Repository Pattern**

Data access abstracted through JPA repositories:

```java
// Example: Session Bean acting as Repository
@Stateless
public class CustomerSessionBean implements CustomerSessionLocal {
    @PersistenceContext
    private EntityManager em;
    
    public Customer findCustomer(int customerId) {
        return em.find(Customer.class, customerId);
    }
}
```

**Benefits**:
- Abstraction of data access logic
- Centralized query management
- Easier testing with mock repositories

### 5. **Dependency Injection (CDI)**

Components wired together using CDI:

```java
@Named
@SessionScoped
public class ShoppingCartBean {
    @Inject
    private GarageSaleStoreManagerLocal storeManager;
    
    @Inject
    private CustomerSessionLocal customerSession;
}
```

**Benefits**:
- Loose coupling
- Easier testing
- Lifecycle management

## Technology Stack

### Core Technologies

| Layer | Technology | Version | Purpose |
|-------|-----------|---------|---------|
| **Application Server** | WebSphere Liberty | 20.0.0.12+ | Runtime environment |
| **Java Platform** | Java EE | 7.0 | Enterprise platform |
| **Build Tool** | Maven | 3.6+ | Build automation |
| **Java Version** | JDK | 8+ | Programming language |

### Java EE Specifications

| Specification | Version | Usage |
|--------------|---------|-------|
| **EJB** | 3.2 | Business logic, session beans |
| **JPA** | 2.1 | Object-relational mapping |
| **JSF** | 2.2 | Web UI framework |
| **CDI** | 1.2 | Dependency injection |
| **JAX-WS** | 2.2 | SOAP web services |
| **JAX-RS** | 2.0 | RESTful web services |
| **WebSocket** | 1.1 | Real-time communication |
| **Servlet** | 4.0 | HTTP request handling |
| **Bean Validation** | 1.1 | Data validation |
| **JTA** | 1.2 | Transaction management |

### Data Technologies

| Technology | Purpose |
|-----------|---------|
| **IBM DB2** | Primary database |
| **EclipseLink** | JPA implementation |
| **JDBC** | Database connectivity |
| **XA Transactions** | Distributed transactions |

### Caching & Session Management

| Technology | Purpose |
|-----------|---------|
| **Redis** | Distributed session cache |
| **Jedis/Lettuce** | Redis client libraries |

### Web Technologies

| Technology | Purpose |
|-----------|---------|
| **JSF 2.2** | Component-based UI |
| **Facelets** | View templating |
| **PrimeFaces** | UI component library (optional) |
| **Dojo Toolkit** | JavaScript framework |

## Component Architecture

### Module Dependencies

```
GarageSaleLibertyEAR (EAR)
├── GSJSFLibertyWeb (WAR)
│   ├── GarageSaleEJB (EJB-JAR) [provided]
│   ├── GarageSaleWSClient (JAR) [provided]
│   └── ProdReviewTaxShipRateEJB (EJB-JAR)
├── WASPersonaWebServicesPMIWeb (WAR)
├── GarageSaleEJB (EJB-JAR)
│   ├── GarageSaleDBJPA (JAR)
│   ├── WSBankEJB (EJB-JAR)
│   ├── CreditCardEJB (EJB-JAR)
│   └── GarageSaleWSClient (JAR)
├── WSBankEJB (EJB-JAR)
│   └── WSBankDBJPA (JAR)
├── CreditCardEJB (EJB-JAR)
│   └── CreditCardDBJPA (JAR)
├── GarageSaleDBJPA (JAR)
├── WSBankDBJPA (JAR)
├── CreditCardDBJPA (JAR)
└── GarageSaleWSClient (JAR)
```

### Component Interaction

```
┌─────────────────────────────────────────────────────────────┐
│                    GSJSFLibertyWeb                          │
│  ┌──────────────┐         ┌──────────────┐                 │
│  │ Managed Bean │────────▶│ EJB Session  │                 │
│  │ (CDI/JSF)    │  @Inject│ Bean (Local) │                 │
│  └──────────────┘         └──────────────┘                 │
└─────────────────────────────────────────────────────────────┘
                                    │
                                    │ Local Interface
                                    ▼
┌─────────────────────────────────────────────────────────────┐
│                    GarageSaleEJB                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         GarageSaleStoreManager                       │  │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐    │  │
│  │  │ Customer   │  │ Inventory  │  │  Category  │    │  │
│  │  │ Session    │  │ Session    │  │  Session   │    │  │
│  │  └────────────┘  └────────────┘  └────────────┘    │  │
│  └──────────────────────────────────────────────────────┘  │
│                          │                                  │
│                          │ @Inject / @EJB                   │
│                          ▼                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │  WSBankEJB   │  │ CreditCardEJB│  │ WSClient     │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ JPA
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                    JPA Entities                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ GarageSaleDB │  │  WSBankDB    │  │ CreditCardDB │    │
│  │   Entities   │  │   Entities   │  │   Entities   │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Data Architecture

### Multi-Database Strategy

The application uses **three separate databases** for security and scalability:

#### 1. **GSDB (GarageSale Database)**
- **Purpose**: Core e-commerce data
- **Tables**: Customer, Inventory, Category, MfgCategory, Settings, StoreCredit, CustomerInfo
- **Access**: Via GarageSaleDBJPA entities

#### 2. **WSBANKDB (Banking Database)**
- **Purpose**: Financial accounts and transactions
- **Tables**: Account, SubAccount, TransactionHistory
- **Access**: Via WSBankDBJPA entities

#### 3. **CCDB (Credit Card Database)**
- **Purpose**: Payment card information
- **Tables**: CreditCard, CardTransaction
- **Access**: Via CreditCardDBJPA entities

### Data Access Pattern

```
┌─────────────────────────────────────────────────────────────┐
│                    EJB Session Bean                         │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  @PersistenceContext(unitName="GSDB")                │  │
│  │  private EntityManager gsEM;                         │  │
│  │                                                       │  │
│  │  @PersistenceContext(unitName="WSBANKDB")           │  │
│  │  private EntityManager bankEM;                       │  │
│  │                                                       │  │
│  │  @PersistenceContext(unitName="CCDB")               │  │
│  │  private EntityManager ccEM;                         │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ JDBC/XA
                          ▼
┌─────────────────────────────────────────────────────────────┐
│              DataSource Configuration                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐    │
│  │ jdbc/gsdb    │  │jdbc/wsbankdb │  │  jdbc/ccdb   │    │
│  │ (XA)         │  │    (XA)      │  │    (XA)      │    │
│  └──────────────┘  └──────────────┘  └──────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

### Transaction Management

- **Container-Managed Transactions (CMT)**: Default for all EJBs
- **XA Transactions**: Support for distributed transactions across multiple databases
- **Transaction Attributes**: 
  - `REQUIRED` (default) - Join existing or create new
  - `REQUIRES_NEW` - Always create new transaction
  - `SUPPORTS` - Join if exists, otherwise non-transactional

## Integration Architecture

### Web Services

#### SOAP Services (JAX-WS)

```
┌─────────────────────────────────────────────────────────────┐
│              GarageSaleStoreManager WSDL                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Operations:                                         │  │
│  │  - listAllInventory()                                │  │
│  │  - listInventoryByCategoryOrMfg()                    │  │
│  │  - getCustomer()                                     │  │
│  │  - checkOut()                                        │  │
│  │  - populateInventory()                               │  │
│  │  - populateCustomers()                               │  │
│  │  - resetGSDB()                                       │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

#### REST Services (JAX-RS)

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Endpoints                       │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  GET    /api/products                                │  │
│  │  GET    /api/products/{id}                           │  │
│  │  GET    /api/categories                              │  │
│  │  POST   /api/cart/add                                │  │
│  │  POST   /api/checkout                                │  │
│  │  GET    /api/customer/{id}                           │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### WebSocket Communication

```
┌─────────────────────────────────────────────────────────────┐
│                WebSocket Endpoints                          │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  /ws/inventory - Real-time inventory updates        │  │
│  │  /ws/orders    - Order status notifications          │  │
│  │  /ws/chat      - Customer support chat               │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Security Architecture

### Authentication & Authorization

```
┌─────────────────────────────────────────────────────────────┐
│                  Security Configuration                     │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  <quickStartSecurity>                                │  │
│  │    userName="${env.QS_SECURITY_USERNAME}"            │  │
│  │    userPassword="${env.QS_SECURITY_PASSWORD}"        │  │
│  │  </quickStartSecurity>                               │  │
│  │                                                       │  │
│  │  <appSecurity-2.0/>                                  │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Security Features

1. **SSL/TLS**: HTTPS endpoints for secure communication
2. **Database Isolation**: Separate databases for sensitive data
3. **Session Security**: Secure session management with Redis
4. **Input Validation**: Bean Validation for data integrity
5. **SQL Injection Prevention**: JPA parameterized queries

## Scalability and Performance

### Horizontal Scaling

```
┌─────────────────────────────────────────────────────────────┐
│                    Load Balancer                            │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  Liberty     │    │  Liberty     │    │  Liberty     │
│  Instance 1  │    │  Instance 2  │    │  Instance 3  │
└──────────────┘    └──────────────┘    └──────────────┘
         │                    │                    │
         └────────────────────┼────────────────────┘
                              ▼
                    ┌──────────────────┐
                    │  Redis Cluster   │
                    │ (Session Cache)  │
                    └──────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │   DB2 Cluster    │
                    │  (with HADR)     │
                    └──────────────────┘
```

### Performance Optimizations

1. **Connection Pooling**: 
   - GSDB: 50-300 connections
   - CCDB: 50-200 connections
   - WSBANKDB: 50-200 connections

2. **Session Caching**: Redis for distributed sessions

3. **JPA Optimization**:
   - Lazy loading for associations
   - Query result caching
   - Batch operations

4. **EJB Pooling**: Stateless session bean pooling

5. **Async Processing**: Concurrent API for background tasks

## Deployment Architecture

See [Deployment Guide](Deployment-Guide-IBM-Cloud.md) for detailed deployment architecture on IBM Cloud.

---

**Next**: [Application Components](Application-Components.md) | [Database Schema](Database-Schema.md)