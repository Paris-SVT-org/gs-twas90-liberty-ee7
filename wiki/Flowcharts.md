# Application Flowcharts and Sequence Diagrams

## Table of Contents

1. [Customer Checkout Process](#customer-checkout-process)
2. [User Authentication Flow](#user-authentication-flow)
3. [Product Browsing Flow](#product-browsing-flow)
4. [Payment Processing Flow](#payment-processing-flow)
5. [Inventory Management Flow](#inventory-management-flow)
6. [Session Management Flow](#session-management-flow)
7. [Database Population Flow](#database-population-flow)
8. [Order Shipping Flow](#order-shipping-flow)

## Customer Checkout Process

This flowchart illustrates the complete checkout process from cart review to order confirmation.

```mermaid
flowchart TD
    Start([Customer Initiates Checkout]) --> A[Load Shopping Cart]
    A --> B{Cart Empty?}
    B -->|Yes| End1([Display Empty Cart Message])
    B -->|No| C[Display Cart Items]
    C --> D[Calculate Subtotal]
    D --> E[Get Customer Info]
    E --> F{Customer Logged In?}
    F -->|No| G[Redirect to Login]
    G --> H[Authenticate User]
    H --> I{Auth Success?}
    I -->|No| End2([Display Error])
    I -->|Yes| J[Load Customer Profile]
    F -->|Yes| J
    J --> K[Display Shipping Address]
    K --> L[Calculate Shipping Rate]
    L --> M[Calculate Tax]
    M --> N[Calculate Total]
    N --> O[Display Payment Options]
    O --> P[Customer Selects Payment]
    P --> Q{Payment Type?}
    Q -->|Credit Card| R[Validate Credit Card]
    Q -->|Store Credit| S[Check Store Credit Balance]
    R --> T{Card Valid?}
    T -->|No| End3([Display Card Error])
    T -->|Yes| U[Process Credit Card Payment]
    S --> V{Sufficient Balance?}
    V -->|No| End4([Insufficient Credit])
    V -->|Yes| W[Deduct Store Credit]
    U --> X[Create Bank Transaction]
    W --> X
    X --> Y{Transaction Success?}
    Y -->|No| Z[Rollback Transaction]
    Z --> End5([Payment Failed])
    Y -->|Yes| AA[Update Inventory]
    AA --> AB[Create Order Record]
    AB --> AC[Generate Order Number]
    AC --> AD[Send Confirmation Email]
    AD --> AE[Clear Shopping Cart]
    AE --> End6([Display Order Confirmation])
    
    style Start fill:#90EE90
    style End1 fill:#FFB6C1
    style End2 fill:#FFB6C1
    style End3 fill:#FFB6C1
    style End4 fill:#FFB6C1
    style End5 fill:#FFB6C1
    style End6 fill:#90EE90
```

### Checkout Process Steps

1. **Cart Validation**: Verify cart has items
2. **Authentication**: Ensure customer is logged in
3. **Address Verification**: Confirm shipping address
4. **Cost Calculation**: Calculate shipping, tax, and total
5. **Payment Processing**: Process credit card or store credit
6. **Transaction Recording**: Create bank transaction record
7. **Inventory Update**: Reduce inventory quantities
8. **Order Creation**: Generate order record
9. **Confirmation**: Send email and display confirmation

## User Authentication Flow

```mermaid
sequenceDiagram
    participant Browser
    participant JSF as JSF Managed Bean
    participant EJB as GarageSaleStoreManager
    participant CustomerDB as Customer Session Bean
    participant DB as GSDB Database
    participant Session as Redis Session Cache
    
    Browser->>JSF: Submit Login Form
    JSF->>JSF: Validate Input
    alt Invalid Input
        JSF-->>Browser: Display Validation Errors
    else Valid Input
        JSF->>EJB: getCustomer(username, password)
        EJB->>CustomerDB: findCustomerByCredentials()
        CustomerDB->>DB: SELECT * FROM CUSTOMER WHERE...
        DB-->>CustomerDB: Customer Record
        CustomerDB-->>EJB: Customer Entity
        alt Customer Not Found
            EJB-->>JSF: null
            JSF-->>Browser: Invalid Credentials
        else Customer Found
            EJB->>EJB: Validate Password Hash
            alt Password Invalid
                EJB-->>JSF: Authentication Failed
                JSF-->>Browser: Invalid Credentials
            else Password Valid
                EJB-->>JSF: Customer Object
                JSF->>Session: Store User Session
                Session-->>JSF: Session ID
                JSF->>JSF: Set Session Attributes
                JSF-->>Browser: Redirect to Dashboard
            end
        end
    end
```

## Product Browsing Flow

```mermaid
flowchart TD
    Start([User Visits Store]) --> A[Display Home Page]
    A --> B{User Action?}
    B -->|Browse Categories| C[Load Categories]
    B -->|Search Products| D[Enter Search Term]
    B -->|View All Products| E[Load All Inventory]
    
    C --> F[Display Category List]
    F --> G[User Selects Category]
    G --> H[Load Products by Category]
    
    D --> I[Submit Search Query]
    I --> J[Search Inventory]
    J --> K[Display Search Results]
    
    E --> L[Query All Inventory]
    
    H --> M[Display Product List]
    K --> M
    L --> M
    
    M --> N{User Action?}
    N -->|View Details| O[Load Product Details]
    N -->|Add to Cart| P[Add Item to Cart]
    N -->|Filter/Sort| Q[Apply Filters]
    N -->|Back| B
    
    O --> R[Display Product Page]
    R --> S[Load Product Reviews]
    S --> T[Calculate Average Rating]
    T --> U[Display Reviews]
    U --> V{User Action?}
    V -->|Add to Cart| P
    V -->|Write Review| W[Submit Review]
    V -->|Back to List| M
    
    P --> X[Update Cart Session]
    X --> Y[Display Cart Badge]
    Y --> Z{Continue Shopping?}
    Z -->|Yes| M
    Z -->|No| End([Proceed to Checkout])
    
    Q --> M
    W --> U
    
    style Start fill:#90EE90
    style End fill:#90EE90
```

## Payment Processing Flow

```mermaid
sequenceDiagram
    participant UI as Web UI
    participant Store as GarageSaleStoreManager
    participant CC as CreditCardEJB
    participant Bank as WSBankEJB
    participant CCDB as Credit Card DB
    participant BankDB as Bank DB
    participant GSDB as GarageSale DB
    
    UI->>Store: checkOut(customerId, cartItems, paymentInfo)
    Store->>Store: Validate Cart Items
    Store->>Store: Calculate Total Amount
    
    alt Payment Type: Credit Card
        Store->>CC: validateCreditCard(cardNumber, cvv, expiry)
        CC->>CCDB: SELECT * FROM CREDITCARD WHERE...
        CCDB-->>CC: Card Record
        CC->>CC: Validate Card Details
        alt Card Invalid
            CC-->>Store: Validation Failed
            Store-->>UI: Payment Error
        else Card Valid
            CC-->>Store: Card Validated
            Store->>CC: processCreditCardPayment(cardId, amount)
            CC->>CCDB: INSERT INTO CARDTRANSACTION...
            CCDB-->>CC: Transaction ID
            CC->>Bank: doTransaction(accountId, amount, type)
            Bank->>BankDB: BEGIN TRANSACTION
            Bank->>BankDB: UPDATE ACCOUNT SET balance...
            Bank->>BankDB: INSERT INTO TRANSACTIONHISTORY...
            BankDB-->>Bank: Transaction Complete
            Bank->>BankDB: COMMIT
            Bank-->>CC: Transaction Success
            CC-->>Store: Payment Processed
        end
    else Payment Type: Store Credit
        Store->>Store: getCustomerStoreCredit(customerId)
        Store->>GSDB: SELECT * FROM STORECREDIT WHERE...
        GSDB-->>Store: Store Credit Balance
        alt Insufficient Credit
            Store-->>UI: Insufficient Store Credit
        else Sufficient Credit
            Store->>GSDB: UPDATE STORECREDIT SET balance...
            GSDB-->>Store: Update Success
        end
    end
    
    alt Payment Successful
        Store->>Store: updateInventory(cartItems)
        Store->>GSDB: UPDATE INVENTORY SET quantity...
        GSDB-->>Store: Inventory Updated
        Store->>Store: createOrderRecord()
        Store->>GSDB: INSERT INTO ORDERS...
        GSDB-->>Store: Order ID
        Store->>Store: clearShoppingCart()
        Store-->>UI: Order Confirmation
    else Payment Failed
        Store->>Store: Rollback All Changes
        Store-->>UI: Payment Failed
    end
```

## Inventory Management Flow

```mermaid
flowchart TD
    Start([Admin Access]) --> A{Action Type?}
    
    A -->|Populate| B[populateInventory]
    A -->|Update| C[Update Single Item]
    A -->|Reset| D[resetGSDB]
    A -->|View| E[List All Inventory]
    
    B --> F[Generate Sample Data]
    F --> G[Create Categories]
    G --> H[Create Manufacturers]
    H --> I[Create Products]
    I --> J[Set Initial Quantities]
    J --> K[Set Prices]
    K --> L[Batch Insert to DB]
    L --> M{Insert Success?}
    M -->|Yes| N[Log Success]
    M -->|No| O[Rollback Transaction]
    O --> P[Log Error]
    N --> End1([Population Complete])
    P --> End2([Population Failed])
    
    C --> Q[Load Product by ID]
    Q --> R[Update Fields]
    R --> S[Validate Data]
    S --> T{Valid?}
    T -->|No| End3([Validation Error])
    T -->|Yes| U[Persist Changes]
    U --> End4([Update Complete])
    
    D --> V[Clear All Tables]
    V --> W[TRUNCATE INVENTORY]
    W --> X[TRUNCATE CATEGORY]
    X --> Y[TRUNCATE MFGCATEGORY]
    Y --> Z[TRUNCATE CUSTOMER]
    Z --> AA[TRUNCATE STORECREDIT]
    AA --> AB[Reset Sequences]
    AB --> End5([Reset Complete])
    
    E --> AC[Query All Products]
    AC --> AD[Join with Categories]
    AD --> AE[Join with Manufacturers]
    AE --> AF[Calculate Stock Status]
    AF --> AG[Format Response]
    AG --> End6([Display Inventory List])
    
    style Start fill:#90EE90
    style End1 fill:#90EE90
    style End2 fill:#FFB6C1
    style End3 fill:#FFB6C1
    style End4 fill:#90EE90
    style End5 fill:#90EE90
    style End6 fill:#90EE90
```

## Session Management Flow

```mermaid
sequenceDiagram
    participant Browser
    participant Liberty as Liberty Server
    participant Session as Session Manager
    participant Redis as Redis Cluster
    participant App as Application Code
    
    Browser->>Liberty: HTTP Request (First Visit)
    Liberty->>Session: Create New Session
    Session->>Session: Generate Session ID
    Session->>Redis: Store Session Data
    Redis-->>Session: Confirmation
    Session->>Liberty: Session Created
    Liberty->>Browser: Set-Cookie: JSESSIONID=xxx
    
    Browser->>Liberty: HTTP Request (Subsequent)
    Liberty->>Liberty: Extract JSESSIONID from Cookie
    Liberty->>Session: Get Session
    Session->>Redis: GET session:xxx
    Redis-->>Session: Session Data
    Session->>Liberty: Session Object
    Liberty->>App: Process Request with Session
    
    App->>App: Modify Session Attributes
    App->>Session: setAttribute("cart", cartData)
    Session->>Redis: SET session:xxx:cart
    Redis-->>Session: OK
    Session-->>App: Attribute Stored
    
    Note over Browser,Redis: Session Timeout Check
    Liberty->>Session: Check Session Timeout
    Session->>Redis: TTL session:xxx
    Redis-->>Session: Remaining Time
    alt Session Expired
        Session->>Redis: DEL session:xxx
        Redis-->>Session: Deleted
        Session->>Liberty: Session Expired
        Liberty->>Browser: Redirect to Login
    else Session Valid
        Session->>Redis: EXPIRE session:xxx 1800
        Redis-->>Session: TTL Updated
        Session->>Liberty: Session Active
    end
    
    Browser->>Liberty: Logout Request
    Liberty->>Session: Invalidate Session
    Session->>Redis: DEL session:xxx
    Redis-->>Session: Deleted
    Session->>Liberty: Session Destroyed
    Liberty->>Browser: Clear Cookie
```

## Database Population Flow

```mermaid
flowchart TD
    Start([Admin Initiates Population]) --> A{Database Type?}
    
    A -->|GarageSale DB| B[populateGSDB]
    A -->|Bank DB| C[populateBankDB]
    A -->|Credit Card DB| D[populateCCDB]
    
    B --> E[Check Existing Data]
    E --> F{Data Exists?}
    F -->|Yes| G[clearGSDB]
    F -->|No| H[Generate Categories]
    G --> H
    
    H --> I[Insert Categories]
    I --> J[Generate Manufacturers]
    J --> K[Insert Manufacturers]
    K --> L[Generate Products]
    L --> M[Set Product Attributes]
    M --> N[Generate Customers]
    N --> O[Hash Passwords]
    O --> P[Insert Customers]
    P --> Q[Generate Customer Info]
    Q --> R[Insert Customer Info]
    R --> S[Generate Store Credits]
    S --> T[Insert Store Credits]
    T --> U[Generate Settings]
    U --> V[Insert Settings]
    V --> End1([GSDB Population Complete])
    
    C --> W[Generate Bank Accounts]
    W --> X[Set Initial Balances]
    X --> Y[Insert Accounts]
    Y --> Z[Generate Sub-Accounts]
    Z --> AA[Link to Main Accounts]
    AA --> AB[Insert Sub-Accounts]
    AB --> AC[Generate Sample Transactions]
    AC --> AD[Insert Transaction History]
    AD --> End2([BankDB Population Complete])
    
    D --> AE[Generate Credit Cards]
    AE --> AF[Set Card Numbers]
    AF --> AG[Set Expiry Dates]
    AG --> AH[Set CVV Codes]
    AH --> AI[Link to Customers]
    AI --> AJ[Insert Credit Cards]
    AJ --> AK[Generate Card Transactions]
    AK --> AL[Insert Card Transactions]
    AL --> End3([CCDB Population Complete])
    
    style Start fill:#90EE90
    style End1 fill:#90EE90
    style End2 fill:#90EE90
    style End3 fill:#90EE90
```

## Order Shipping Flow

```mermaid
sequenceDiagram
    participant Admin as Admin UI
    participant Store as GarageSaleStoreManager
    participant Ship as ShipRateSessionBean
    participant Tax as TaxRateSessionBean
    participant GSDB as GarageSale DB
    participant Email as Email Service
    
    Admin->>Store: orderShipped(orderId, trackingNumber)
    Store->>GSDB: SELECT * FROM ORDERS WHERE id=?
    GSDB-->>Store: Order Details
    
    Store->>Store: Validate Order Status
    alt Order Already Shipped
        Store-->>Admin: Error: Already Shipped
    else Order Pending
        Store->>GSDB: UPDATE ORDERS SET status='SHIPPED'
        Store->>GSDB: UPDATE ORDERS SET tracking=?
        Store->>GSDB: UPDATE ORDERS SET ship_date=NOW()
        GSDB-->>Store: Update Success
        
        Store->>GSDB: SELECT customer_id FROM ORDERS
        GSDB-->>Store: Customer ID
        
        Store->>GSDB: SELECT email FROM CUSTOMER WHERE id=?
        GSDB-->>Store: Customer Email
        
        Store->>Email: sendShippingNotification(email, orderId, tracking)
        Email->>Email: Compose Email
        Email->>Email: Add Tracking Link
        Email->>Email: Send Email
        Email-->>Store: Email Sent
        
        Store->>Store: Log Shipping Event
        Store->>GSDB: INSERT INTO SHIPPING_LOG...
        GSDB-->>Store: Log Created
        
        Store-->>Admin: Shipping Confirmed
    end
```

## Component Interaction Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[JSF Managed Beans]
        B[REST Controllers]
        C[SOAP Endpoints]
    end
    
    subgraph "Business Layer"
        D[GarageSaleStoreManager]
        E[CustomerSessionBean]
        F[InventorySessionBean]
        G[CategorySessionBean]
    end
    
    subgraph "Integration Layer"
        H[WSBankEJB]
        I[CreditCardEJB]
        J[ProdReviewEJB]
        K[TaxRateEJB]
        L[ShipRateEJB]
    end
    
    subgraph "Data Layer"
        M[GarageSaleDBJPA]
        N[WSBankDBJPA]
        O[CreditCardDBJPA]
    end
    
    subgraph "Infrastructure"
        P[(GSDB)]
        Q[(WSBANKDB)]
        R[(CCDB)]
        S[Redis Cache]
    end
    
    A --> D
    B --> D
    C --> D
    
    D --> E
    D --> F
    D --> G
    D --> H
    D --> I
    
    E --> M
    F --> M
    G --> M
    
    H --> N
    I --> O
    J --> M
    K --> M
    L --> M
    
    M --> P
    N --> Q
    O --> R
    
    A -.Session.-> S
    B -.Session.-> S
    
    style A fill:#E1F5FF
    style B fill:#E1F5FF
    style C fill:#E1F5FF
    style D fill:#FFF4E1
    style E fill:#FFF4E1
    style F fill:#FFF4E1
    style G fill:#FFF4E1
    style H fill:#E8F5E9
    style I fill:#E8F5E9
    style J fill:#E8F5E9
    style K fill:#E8F5E9
    style L fill:#E8F5E9
    style M fill:#F3E5F5
    style N fill:#F3E5F5
    style O fill:#F3E5F5
    style P fill:#FFEBEE
    style Q fill:#FFEBEE
    style R fill:#FFEBEE
    style S fill:#FFF9C4
```

## Key Process Metrics

### Checkout Process
- **Average Duration**: 15-30 seconds
- **Success Rate**: 95%+
- **Critical Path**: Payment Processing → Inventory Update → Order Creation

### Authentication
- **Average Duration**: 1-2 seconds
- **Cache Hit Rate**: 85%+
- **Session Timeout**: 30 minutes

### Product Browsing
- **Average Page Load**: 500ms - 1s
- **Cache Hit Rate**: 90%+
- **Concurrent Users**: 1000+

### Payment Processing
- **Average Duration**: 3-5 seconds
- **Transaction Success Rate**: 98%+
- **Rollback Rate**: <2%

---

**Next**: [API Documentation](API-Documentation.md) | [Troubleshooting Guide](Troubleshooting-Guide.md)