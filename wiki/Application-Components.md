# Application Components

## Table of Contents

1. [Overview](#overview)
2. [Module Architecture](#module-architecture)
3. [Core Modules](#core-modules)
4. [Supporting Modules](#supporting-modules)
5. [Web Modules](#web-modules)
6. [Data Access Modules](#data-access-modules)
7. [Integration Modules](#integration-modules)
8. [Runtime Utilities](#runtime-utilities)

## Overview

The GarageSale application is organized as a multi-module Maven project, following a modular architecture that promotes separation of concerns, reusability, and maintainability. The application is packaged as an Enterprise Archive (EAR) containing multiple WAR and EJB-JAR modules.

### Module Dependency Graph

```
GarageSaleLibertyEAR (EAR)
│
├── GSJSFLibertyWeb (WAR) - Customer-facing web application
│   ├── GarageSaleEJB (EJB-JAR) [provided]
│   ├── GarageSaleWSClient (JAR) [provided]
│   └── ProdReviewTaxShipRateEJB (EJB-JAR)
│
├── WASPersonaWebServicesPMIWeb (WAR) - Admin/monitoring interface
│
├── GarageSaleEJB (EJB-JAR) - Core business logic
│   ├── GarageSaleDBJPA (JAR)
│   ├── WSBankEJB (EJB-JAR)
│   ├── CreditCardEJB (EJB-JAR)
│   └── GarageSaleWSClient (JAR)
│
├── WSBankEJB (EJB-JAR) - Banking services
│   └── WSBankDBJPA (JAR)
│
├── CreditCardEJB (EJB-JAR) - Payment processing
│   └── CreditCardDBJPA (JAR)
│
├── ProdReviewTaxShipRateEJB (EJB-JAR) - Auxiliary services
│
├── GarageSaleDBJPA (JAR) - GarageSale database entities
├── WSBankDBJPA (JAR) - Banking database entities
├── CreditCardDBJPA (JAR) - Credit card database entities
└── GarageSaleWSClient (JAR) - Web service client utilities
```

## Core Modules

### 1. GarageSaleLibertyEAR

**Type**: Enterprise Archive (EAR)  
**Artifact ID**: `GarageSaleLibertyEAR`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Top-level packaging module that assembles all components into a deployable EAR file.

**Key Features**:
- Packages all WAR, EJB-JAR, and JAR modules
- Manages shared libraries (DB2 drivers, Redis clients)
- Configures application metadata
- Includes Git commit information in manifest

**Configuration Files**:
- `application.xml` - EAR deployment descriptor
- `pom.xml` - Maven build configuration

**Build Output**:
```
GarageSaleLibertyEAR.ear
├── GSjsf20LibertyWeb.war
├── WASPersonaWebServicesPMIWeb.war
├── GarageSaleEJB.jar
├── WSBankEJB.jar
├── CreditCardEJB.jar
├── ProdReviewTaxShipRateEJB.jar
├── GarageSaleDBJPA.jar
├── WSBankDBJPA.jar
├── CreditCardDBJPA.jar
├── GarageSaleWSClient.jar
└── META-INF/
    ├── application.xml
    └── MANIFEST.MF
```

### 2. GarageSaleEJB

**Type**: EJB Module (EJB-JAR)  
**Artifact ID**: `GarageSaleEJB`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Core business logic layer containing session beans for e-commerce operations.

**Key Components**:

#### Session Beans

1. **GarageSaleStoreManager** (Main Facade)
   - `@Stateless`
   - `@WebService` - Exposes SOAP operations
   - Coordinates all store operations
   - Methods:
     - `listAllInventory()` - Get all products
     - `listInventoryByCategoryOrMfg()` - Filter products
     - `getCustomer()` - Retrieve customer details
     - `checkOut()` - Process order checkout
     - `populateInventory()` - Initialize product data
     - `populateCustomers()` - Initialize customer data
     - `resetGSDB()` - Clear and reset database

2. **CustomerSessionBean**
   - `@Stateless`
   - Customer CRUD operations
   - Authentication and authorization
   - Methods:
     - `createCustomer()`
     - `findCustomer()`
     - `updateCustomer()`
     - `deleteCustomer()`
     - `listAllCustomers()`

3. **InventorySessionBean**
   - `@Stateless`
   - Product inventory management
   - Stock level tracking
   - Methods:
     - `createInventory()`
     - `findInventory()`
     - `updateInventory()`
     - `listAllInventory()`
     - `updateStockLevel()`

4. **CategorySessionBean**
   - `@Stateless`
   - Product category management
   - Methods:
     - `createCategory()`
     - `findCategory()`
     - `listCategories()`

5. **MfgCategorySessionBean**
   - `@Stateless`
   - Manufacturer management
   - Methods:
     - `createMfgCategory()`
     - `findMfgCategory()`
     - `listMfgCategories()`

6. **CustomerInfoSessionBean**
   - `@Stateless`
   - Extended customer information
   - Shipping addresses
   - Contact details

7. **StoreCreditSessionBean**
   - `@Stateless`
   - Store credit management
   - Balance tracking
   - Credit transactions

8. **SettingSessionBean**
   - `@Stateless`
   - Application settings
   - Configuration management

**Dependencies**:
- GarageSaleDBJPA - Data access
- WSBankEJB - Banking integration
- CreditCardEJB - Payment processing
- GarageSaleWSClient - External service calls

**Package Structure**:
```
com.ibm.websphere.svt.gs.gsdb.session
├── GarageSaleStoreManager.java
├── CustomerSessionBean.java
├── InventorySessionBean.java
├── CategorySessionBean.java
├── MfgCategorySessionBean.java
├── CustomerInfoSessionBean.java
├── StoreCreditSessionBean.java
├── SettingSessionBean.java
├── util/
│   └── GarageSaleEJBWSClientUtil.java
└── view/
    ├── GarageSaleStoreManagerLocal.java
    ├── CustomerSessionLocal.java
    ├── InventorySessionLocal.java
    └── jaxws/
        ├── CheckOut.java
        ├── CheckOutResponse.java
        ├── GetCustomer.java
        └── ...
```

### 3. WSBankEJB

**Type**: EJB Module (EJB-JAR)  
**Artifact ID**: `WSBankEJB`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Banking service integration for financial transactions.

**Key Components**:

1. **GarageSaleBankManager**
   - `@Stateless`
   - `@WebService`
   - Main banking facade
   - Methods:
     - `createAccountsForGS()` - Create customer accounts
     - `doTransaction()` - Process financial transaction
     - `getAccountBalances()` - Query account balances

2. **AccountSessionBean**
   - `@Stateless`
   - Bank account management
   - Account CRUD operations

3. **SubAccountSessionBean**
   - `@Stateless`
   - Sub-account management
   - Linked account operations

4. **TransactionHistorySessionBean**
   - `@Stateless`
   - Transaction logging
   - History queries

**Database**: WSBANKDB (separate database for banking data)

**Package Structure**:
```
com.ibm.websphere.svt.gs.wsbankdb.session
├── GarageSaleBankManager.java
├── AccountSessionBean.java
├── SubAccountSessionBean.java
├── TransactionHistorySessionBean.java
└── view/
    ├── GarageSaleBankManagerLocal.java
    ├── AccountSessionLocal.java
    └── jaxws/
        ├── CreateAccountsForGS.java
        ├── DoTransaction.java
        └── GetAccountBalances.java
```

### 4. CreditCardEJB

**Type**: EJB Module (EJB-JAR)  
**Artifact ID**: `CreditCardEJB`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Credit card processing and validation.

**Key Components**:

1. **CreditCardManager**
   - `@Stateless`
   - Credit card validation
   - Payment processing
   - Methods:
     - `validateCreditCard()` - Validate card details
     - `processCreditCardPayment()` - Process payment
     - `getCreditCardHistory()` - Transaction history

2. **CreditCardSessionBean**
   - `@Stateless`
   - Credit card CRUD operations
   - Card management

**Database**: CCDB (separate database for PCI compliance)

**Security Features**:
- Encrypted card storage
- PCI-DSS compliance
- Secure transaction logging

## Supporting Modules

### 5. ProdReviewTaxShipRateEJB

**Type**: EJB Module (EJB-JAR)  
**Artifact ID**: `ProdReviewTaxShipRateEJB`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Auxiliary services for product reviews, tax calculation, and shipping rates.

**Key Components**:

1. **ProdReviewSessionBean1**
   - `@Stateless`
   - Product review management
   - Rating calculations
   - Methods:
     - `createReview()`
     - `getProductReviews()`
     - `calculateAverageRating()`

2. **TaxRateSessionBean1**
   - `@Stateless`
   - Tax calculation based on location
   - Tax rate management
   - Methods:
     - `calculateTax()`
     - `getTaxRate()`

3. **ShipRateSessionBean1**
   - `@Stateless`
   - Shipping cost calculation
   - Carrier rate management
   - Methods:
     - `calculateShippingCost()`
     - `getShippingOptions()`

## Web Modules

### 6. GSJSFLibertyWeb

**Type**: Web Application (WAR)  
**Artifact ID**: `GSjsf20LibertyWeb`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Customer-facing web interface using JSF 2.2.

**Key Features**:
- JSF 2.2 with Facelets
- CDI managed beans
- AJAX support
- Responsive design
- WebSocket integration

**Main Components**:

1. **Managed Beans** (CDI/JSF)
   - `@Named` and `@SessionScoped`
   - Shopping cart management
   - User session handling
   - Navigation control

2. **View Pages** (XHTML)
   - Product catalog
   - Product details
   - Shopping cart
   - Checkout process
   - Customer account

3. **Resources**
   - CSS stylesheets
   - JavaScript files
   - Images
   - Dojo Toolkit integration

**Configuration Files**:
- `web.xml` - Web application descriptor
- `faces-config.xml` - JSF configuration
- `beans.xml` - CDI configuration

**URL Mapping**:
```
/GSjsf20LibertyWeb
├── /index.xhtml - Home page
├── /products.xhtml - Product listing
├── /product-detail.xhtml - Product details
├── /cart.xhtml - Shopping cart
├── /checkout.xhtml - Checkout process
├── /account.xhtml - Customer account
└── /login.xhtml - Login page
```

### 7. WASPersonaWebServicesPMIWeb

**Type**: Web Application (WAR)  
**Artifact ID**: `WASPersonaWebServicesPMIWeb`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Administrative interface and monitoring dashboard.

**Key Features**:
- Admin operations
- Database management
- Performance monitoring
- System diagnostics
- Web service testing

**Main Components**:

1. **Admin Pages**
   - Database population
   - Database reset
   - Inventory management
   - Customer management

2. **Monitoring Pages**
   - Performance metrics
   - Transaction logs
   - System health

3. **Testing Tools**
   - SOAP service tester
   - REST API tester
   - Database query tool

**URL Mapping**:
```
/WASPersonaWebServicesPMIWeb
├── /index.xhtml - Dashboard
├── /populate.xhtml - Data population
├── /reset.xhtml - Database reset
├── /monitor.xhtml - Performance monitoring
└── /test.xhtml - Service testing
```

## Data Access Modules

### 8. GarageSaleDBJPA

**Type**: JAR Library  
**Artifact ID**: `GarageSaleDBJPA`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: JPA entities for GarageSale database (GSDB).

**JPA Entities**:

1. **Customer**
   - `@Entity`
   - `@Table(name="CUSTOMER")`
   - Fields: customerId, username, password, email, firstName, lastName

2. **Inventory**
   - `@Entity`
   - `@Table(name="INVENTORY")`
   - Fields: inventoryId, name, description, price, quantity, categoryId, mfgId

3. **Category**
   - `@Entity`
   - `@Table(name="CATEGORY")`
   - Fields: categoryId, name, description

4. **MfgCategory**
   - `@Entity`
   - `@Table(name="MFGCATEGORY")`
   - Fields: mfgId, name, description

5. **CustomerInfo**
   - `@Entity`
   - `@Table(name="CUSTOMERINFO")`
   - Fields: customerId, address, city, state, zip, phone

6. **StoreCredit**
   - `@Entity`
   - `@Table(name="STORECREDIT")`
   - Fields: customerId, balance, lastUpdated

7. **Settings**
   - `@Entity`
   - `@Table(name="SETTINGS")`
   - Fields: settingId, key, value

**Persistence Configuration**:
- Persistence Unit: `GSDB`
- Provider: EclipseLink
- Transaction Type: JTA
- DataSource: `jdbc/gsdb`

### 9. WSBankDBJPA

**Type**: JAR Library  
**Artifact ID**: `WSBankDBJPA`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: JPA entities for banking database (WSBANKDB).

**JPA Entities**:

1. **Account**
   - `@Entity`
   - `@Table(name="ACCOUNT")`
   - Fields: accountId, accountNumber, balance, accountType, customerId

2. **SubAccount**
   - `@Entity`
   - `@Table(name="SUBACCOUNT")`
   - Fields: subAccountId, accountId, balance, subAccountType

3. **TransactionHistory**
   - `@Entity`
   - `@Table(name="TRANSACTIONHISTORY")`
   - Fields: transactionId, accountId, amount, transactionType, timestamp

**Persistence Configuration**:
- Persistence Unit: `WSBANKDB`
- Provider: EclipseLink
- Transaction Type: JTA
- DataSource: `jdbc/wsbankdb`

### 10. CreditCardDBJPA

**Type**: JAR Library  
**Artifact ID**: `CreditCardDBJPA`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: JPA entities for credit card database (CCDB).

**JPA Entities**:

1. **CreditCard**
   - `@Entity`
   - `@Table(name="CREDITCARD")`
   - Fields: cardId, cardNumber (encrypted), cvv (encrypted), expiryDate, customerId

2. **CardTransaction**
   - `@Entity`
   - `@Table(name="CARDTRANSACTION")`
   - Fields: transactionId, cardId, amount, timestamp, status

**Persistence Configuration**:
- Persistence Unit: `CCDB`
- Provider: EclipseLink
- Transaction Type: JTA
- DataSource: `jdbc/ccdb`

## Integration Modules

### 11. GarageSaleWSClient

**Type**: JAR Library  
**Artifact ID**: `GarageSaleWSClient`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Web service client utilities and wrapper classes.

**Key Components**:

1. **Wrapper Classes**
   - `CategoryWrapper`
   - `CustomerWrapper`
   - `CustomerInfoWrapper`
   - `InventoryWrapper`
   - `MfgCategoryWrapper`
   - `PaymentInfoWrapper`
   - `ShoppingCartWrapper`
   - `StoreCreditWrapper`
   - `TransactionHistoryWrapper`

2. **Utility Classes**
   - SOAP client helpers
   - REST client helpers
   - XML/JSON marshalling

**Usage**:
```java
// Example: Calling external web service
GarageSaleWSClient client = new GarageSaleWSClient();
InventoryWrapper[] products = client.listAllInventory();
```

## Runtime Utilities

### 12. GarageSaleRuntimeUtil

**Type**: Utility Module  
**Artifact ID**: `GarageSaleRuntimeUtil`  
**Version**: 0.0.1-SNAPSHOT

**Purpose**: Runtime configuration files and deployment utilities.

**Contents**:

1. **Server Configuration**
   - `publish/servers/server.xml` - Liberty server configuration
   - `publish/files/server.env` - Environment variables
   - `publish/servers/REDIS_SESSION_CACHE_CONFIG.md` - Redis setup guide

2. **Database Drivers**
   - `publish/db2drivers/` - DB2 JDBC drivers
   - `db2jcc4.jar`
   - `db2jcc_license_cu.jar`

3. **Redis Client Libraries**
   - `publish/redis/` - Redis client JARs
   - `jedis-4.3.1.jar`
   - `commons-pool2-2.11.1.jar`
   - `slf4j-api-2.0.7.jar`

4. **Documentation**
   - `publish/redis/README.md` - Redis setup instructions
   - Configuration guides

## Module Communication Patterns

### 1. Local EJB Calls

```java
@Stateless
public class GarageSaleStoreManager {
    @EJB
    private CustomerSessionLocal customerSession;
    
    @EJB
    private InventorySessionLocal inventorySession;
    
    public Customer getCustomer(int id) {
        return customerSession.findCustomer(id);
    }
}
```

### 2. CDI Injection

```java
@Named
@SessionScoped
public class ShoppingCartBean {
    @Inject
    private GarageSaleStoreManagerLocal storeManager;
    
    public void addToCart(int productId) {
        Inventory product = storeManager.getInventory(productId);
        // Add to cart logic
    }
}
```

### 3. JPA Entity Manager

```java
@Stateless
public class CustomerSessionBean {
    @PersistenceContext(unitName="GSDB")
    private EntityManager em;
    
    public Customer findCustomer(int id) {
        return em.find(Customer.class, id);
    }
}
```

## Build and Deployment

### Maven Build Order

```bash
# Build all modules
mvn clean install

# Build order (automatic via Maven reactor):
1. GarageSaleDBJPA
2. WSBankDBJPA
3. CreditCardDBJPA
4. GarageSaleWSClient
5. GarageSaleEJB
6. WSBankEJB
7. CreditCardEJB
8. ProdReviewTaxShipRateEJB
9. GSJSFLibertyWeb
10. WASPersonaWebServicesPMIWeb
11. GarageSaleLibertyEAR
12. GarageSaleRuntimeUtil
```

### Deployment Package

The final EAR file includes all modules and is ready for deployment to WebSphere Liberty:

```
GarageSaleLibertyEAR.ear (Final artifact)
Size: ~50-100 MB
Location: GarageSaleLibertyEAR/target/GarageSaleLibertyEAR.ear
```

---

**Next**: [Database Schema](Database-Schema.md) | [Configuration Guide](Configuration-Guide.md)