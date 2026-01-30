# API Documentation

## Table of Contents

1. [Overview](#overview)
2. [SOAP Web Services](#soap-web-services)
3. [REST APIs](#rest-apis)
4. [WebSocket Endpoints](#websocket-endpoints)
5. [Authentication](#authentication)
6. [Error Handling](#error-handling)
7. [Rate Limiting](#rate-limiting)
8. [API Examples](#api-examples)

## Overview

The GarageSale application exposes multiple API interfaces for integration with external systems and client applications.

### API Types

| API Type | Protocol | Base URL | Authentication |
|----------|----------|----------|----------------|
| **SOAP** | HTTP/HTTPS | `/GarageSaleStoreManagerService` | Basic Auth |
| **REST** | HTTP/HTTPS | `/api/v1` | JWT/Basic Auth |
| **WebSocket** | WS/WSS | `/ws` | Session-based |

### API Versioning

- SOAP: Version embedded in namespace
- REST: Version in URL path (`/api/v1`)
- WebSocket: Version in connection parameters

## SOAP Web Services

### GarageSaleStoreManager Service

**WSDL Location**: `http://localhost:9080/GarageSaleStoreManagerService?wsdl`

**Namespace**: `http://view.session.gsdb.gs.svt.websphere.ibm.com/`

**Service Endpoint**: `/GarageSaleStoreManagerService`

#### Operations

##### 1. listAllInventory

Get all products in the inventory.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:listAllInventory/>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response**:
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:listAllInventoryResponse xmlns:ns2="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
         <return>
            <inventoryId>1</inventoryId>
            <productName>Laptop Computer</productName>
            <description>High-performance laptop</description>
            <price>999.99</price>
            <quantity>50</quantity>
            <categoryId>1</categoryId>
            <mfgId>1</mfgId>
            <sku>LAP-001</sku>
         </return>
         <return>
            <inventoryId>2</inventoryId>
            <productName>Wireless Mouse</productName>
            <description>Ergonomic wireless mouse</description>
            <price>29.99</price>
            <quantity>200</quantity>
            <categoryId>2</categoryId>
            <mfgId>2</mfgId>
            <sku>MOU-001</sku>
         </return>
      </ns2:listAllInventoryResponse>
   </soap:Body>
</soap:Envelope>
```

##### 2. listInventoryByCategoryOrMfg

Filter products by category or manufacturer.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:listInventoryByCategoryOrMfg>
         <categoryId>1</categoryId>
         <mfgId>0</mfgId>
      </view:listInventoryByCategoryOrMfg>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response**: Similar to listAllInventory but filtered

##### 3. getCustomer

Retrieve customer information.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:getCustomer>
         <customerId>1</customerId>
      </view:getCustomer>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response**:
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:getCustomerResponse xmlns:ns2="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
         <return>
            <customerId>1</customerId>
            <username>john.doe</username>
            <email>john.doe@example.com</email>
            <firstName>John</firstName>
            <lastName>Doe</lastName>
            <accountStatus>ACTIVE</accountStatus>
         </return>
      </ns2:getCustomerResponse>
   </soap:Body>
</soap:Envelope>
```

##### 4. checkOut

Process customer checkout.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:checkOut>
         <customerId>1</customerId>
         <cartItems>
            <inventoryId>1</inventoryId>
            <quantity>2</quantity>
         </cartItems>
         <cartItems>
            <inventoryId>3</inventoryId>
            <quantity>1</quantity>
         </cartItems>
         <paymentInfo>
            <paymentType>CREDIT_CARD</paymentType>
            <cardId>1</cardId>
         </paymentInfo>
      </view:checkOut>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response**:
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:checkOutResponse xmlns:ns2="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
         <return>
            <orderId>12345</orderId>
            <orderNumber>ORD-2026-12345</orderNumber>
            <totalAmount>2029.97</totalAmount>
            <status>COMPLETED</status>
            <transactionId>TXN-67890</transactionId>
         </return>
      </ns2:checkOutResponse>
   </soap:Body>
</soap:Envelope>
```

##### 5. populateInventory

Initialize inventory data (Admin operation).

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:populateInventory>
         <numberOfProducts>100</numberOfProducts>
      </view:populateInventory>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response**:
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:populateInventoryResponse xmlns:ns2="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
         <return>
            <status>SUCCESS</status>
            <message>Successfully populated 100 products</message>
            <productsCreated>100</productsCreated>
         </return>
      </ns2:populateInventoryResponse>
   </soap:Body>
</soap:Envelope>
```

##### 6. populateCustomers

Initialize customer data (Admin operation).

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:populateCustomers>
         <numberOfCustomers>50</numberOfCustomers>
      </view:populateCustomers>
   </soapenv:Body>
</soapenv:Envelope>
```

##### 7. resetGSDB

Clear and reset GarageSale database (Admin operation).

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.gsdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:resetGSDB/>
   </soapenv:Body>
</soapenv:Envelope>
```

### WSBankManager Service

**WSDL Location**: `http://localhost:9080/WSBankManagerService?wsdl`

#### Operations

##### 1. createAccountsForGS

Create bank accounts for GarageSale customers.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.wsbankdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:createAccountsForGS>
         <customerId>1</customerId>
         <accountType>CHECKING</accountType>
         <initialBalance>1000.00</initialBalance>
      </view:createAccountsForGS>
   </soapenv:Body>
</soapenv:Envelope>
```

##### 2. doTransaction

Process a financial transaction.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.wsbankdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:doTransaction>
         <accountId>1</accountId>
         <amount>99.99</amount>
         <transactionType>DEBIT</transactionType>
      </view:doTransaction>
   </soapenv:Body>
</soapenv:Envelope>
```

##### 3. getAccountBalances

Query account balances.

**Request**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:view="http://view.session.wsbankdb.gs.svt.websphere.ibm.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <view:getAccountBalances>
         <customerId>1</customerId>
      </view:getAccountBalances>
   </soapenv:Body>
</soapenv:Envelope>
```

## REST APIs

### Base URL

```
http://localhost:9080/api/v1
```

### Common Headers

```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer <jwt-token>
```

### Products API

#### GET /products

List all products.

**Request**:
```http
GET /api/v1/products HTTP/1.1
Host: localhost:9080
Accept: application/json
```

**Query Parameters**:
- `page` (optional): Page number (default: 1)
- `size` (optional): Page size (default: 20)
- `category` (optional): Filter by category ID
- `manufacturer` (optional): Filter by manufacturer ID
- `minPrice` (optional): Minimum price
- `maxPrice` (optional): Maximum price
- `inStock` (optional): Only in-stock items (true/false)

**Response**:
```json
{
  "status": "success",
  "data": {
    "products": [
      {
        "inventoryId": 1,
        "productName": "Laptop Computer",
        "description": "High-performance laptop",
        "price": 999.99,
        "quantity": 50,
        "categoryId": 1,
        "categoryName": "Electronics",
        "mfgId": 1,
        "mfgName": "TechCorp",
        "sku": "LAP-001",
        "imageUrl": "/images/products/lap-001.jpg"
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalPages": 5,
      "totalItems": 100
    }
  }
}
```

#### GET /products/{id}

Get product details.

**Request**:
```http
GET /api/v1/products/1 HTTP/1.1
Host: localhost:9080
Accept: application/json
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "inventoryId": 1,
    "productName": "Laptop Computer",
    "description": "High-performance laptop with 16GB RAM and 512GB SSD",
    "price": 999.99,
    "quantity": 50,
    "categoryId": 1,
    "categoryName": "Electronics",
    "mfgId": 1,
    "mfgName": "TechCorp",
    "sku": "LAP-001",
    "imageUrl": "/images/products/lap-001.jpg",
    "weight": 2.5,
    "dimensions": "15x10x1 inches",
    "averageRating": 4.5,
    "reviewCount": 23
  }
}
```

### Categories API

#### GET /categories

List all categories.

**Request**:
```http
GET /api/v1/categories HTTP/1.1
Host: localhost:9080
Accept: application/json
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "categories": [
      {
        "categoryId": 1,
        "categoryName": "Electronics",
        "description": "Electronic devices and accessories",
        "productCount": 45
      },
      {
        "categoryId": 2,
        "categoryName": "Home & Garden",
        "description": "Home and garden products",
        "productCount": 32
      }
    ]
  }
}
```

### Cart API

#### POST /cart/add

Add item to cart.

**Request**:
```http
POST /api/v1/cart/add HTTP/1.1
Host: localhost:9080
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "inventoryId": 1,
  "quantity": 2
}
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "cartId": "cart-12345",
    "items": [
      {
        "inventoryId": 1,
        "productName": "Laptop Computer",
        "quantity": 2,
        "unitPrice": 999.99,
        "subtotal": 1999.98
      }
    ],
    "subtotal": 1999.98,
    "tax": 159.99,
    "shipping": 0.00,
    "total": 2159.97
  }
}
```

#### GET /cart

Get current cart.

**Request**:
```http
GET /api/v1/cart HTTP/1.1
Host: localhost:9080
Authorization: Bearer <jwt-token>
```

#### DELETE /cart/items/{inventoryId}

Remove item from cart.

**Request**:
```http
DELETE /api/v1/cart/items/1 HTTP/1.1
Host: localhost:9080
Authorization: Bearer <jwt-token>
```

### Checkout API

#### POST /checkout

Process checkout.

**Request**:
```http
POST /api/v1/checkout HTTP/1.1
Host: localhost:9080
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "paymentMethod": "CREDIT_CARD",
  "cardId": 1,
  "shippingAddress": {
    "addressLine1": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001"
  }
}
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "orderId": 12345,
    "orderNumber": "ORD-2026-12345",
    "orderDate": "2026-01-30T21:00:00Z",
    "totalAmount": 2159.97,
    "status": "COMPLETED",
    "transactionId": "TXN-67890",
    "estimatedDelivery": "2026-02-05"
  }
}
```

### Customer API

#### GET /customers/me

Get current customer profile.

**Request**:
```http
GET /api/v1/customers/me HTTP/1.1
Host: localhost:9080
Authorization: Bearer <jwt-token>
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "customerId": 1,
    "username": "john.doe",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "accountStatus": "ACTIVE",
    "storeCredit": 50.00,
    "memberSince": "2025-01-15T10:30:00Z"
  }
}
```

#### PUT /customers/me

Update customer profile.

**Request**:
```http
PUT /api/v1/customers/me HTTP/1.1
Host: localhost:9080
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@newemail.com",
  "phone": "555-1234"
}
```

### Orders API

#### GET /orders

List customer orders.

**Request**:
```http
GET /api/v1/orders HTTP/1.1
Host: localhost:9080
Authorization: Bearer <jwt-token>
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "orders": [
      {
        "orderId": 12345,
        "orderNumber": "ORD-2026-12345",
        "orderDate": "2026-01-30T21:00:00Z",
        "totalAmount": 2159.97,
        "status": "SHIPPED",
        "trackingNumber": "1Z999AA10123456784",
        "itemCount": 3
      }
    ]
  }
}
```

#### GET /orders/{orderId}

Get order details.

**Request**:
```http
GET /api/v1/orders/12345 HTTP/1.1
Host: localhost:9080
Authorization: Bearer <jwt-token>
```

## WebSocket Endpoints

### Inventory Updates

**Endpoint**: `ws://localhost:9080/ws/inventory`

**Purpose**: Real-time inventory updates

**Message Format**:
```json
{
  "type": "INVENTORY_UPDATE",
  "data": {
    "inventoryId": 1,
    "quantity": 45,
    "status": "IN_STOCK"
  }
}
```

### Order Status

**Endpoint**: `ws://localhost:9080/ws/orders`

**Purpose**: Real-time order status updates

**Message Format**:
```json
{
  "type": "ORDER_STATUS",
  "data": {
    "orderId": 12345,
    "status": "SHIPPED",
    "trackingNumber": "1Z999AA10123456784",
    "timestamp": "2026-01-30T21:00:00Z"
  }
}
```

## Authentication

### Basic Authentication (SOAP)

```http
Authorization: Basic base64(username:password)
```

### JWT Authentication (REST)

#### Login

**Request**:
```http
POST /api/v1/auth/login HTTP/1.1
Host: localhost:9080
Content-Type: application/json

{
  "username": "john.doe",
  "password": "password123"
}
```

**Response**:
```json
{
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600,
    "refreshToken": "refresh-token-here"
  }
}
```

#### Using JWT Token

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Error Handling

### Error Response Format

```json
{
  "status": "error",
  "error": {
    "code": "PRODUCT_NOT_FOUND",
    "message": "Product with ID 999 not found",
    "details": {
      "inventoryId": 999
    }
  }
}
```

### HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful request |
| 201 | Created | Resource created |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Service temporarily unavailable |

### Error Codes

| Code | Description |
|------|-------------|
| `INVALID_REQUEST` | Request validation failed |
| `AUTHENTICATION_FAILED` | Invalid credentials |
| `AUTHORIZATION_FAILED` | Insufficient permissions |
| `PRODUCT_NOT_FOUND` | Product does not exist |
| `CUSTOMER_NOT_FOUND` | Customer does not exist |
| `INSUFFICIENT_STOCK` | Not enough inventory |
| `PAYMENT_FAILED` | Payment processing failed |
| `DATABASE_ERROR` | Database operation failed |
| `EXTERNAL_SERVICE_ERROR` | External service unavailable |

## Rate Limiting

### Limits

| API Type | Limit | Window |
|----------|-------|--------|
| REST API | 1000 requests | per hour |
| SOAP API | 500 requests | per hour |
| WebSocket | 100 connections | per user |

### Rate Limit Headers

```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1643587200
```

## API Examples

### cURL Examples

#### Get Products
```bash
curl -X GET "http://localhost:9080/api/v1/products?page=1&size=10" \
  -H "Accept: application/json"
```

#### Add to Cart
```bash
curl -X POST "http://localhost:9080/api/v1/cart/add" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "inventoryId": 1,
    "quantity": 2
  }'
```

#### Checkout
```bash
curl -X POST "http://localhost:9080/api/v1/checkout" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "paymentMethod": "CREDIT_CARD",
    "cardId": 1
  }'
```

### Java Client Example

```java
// SOAP Client
GarageSaleStoreManagerService service = new GarageSaleStoreManagerService();
GarageSaleStoreManagerLocal port = service.getGarageSaleStoreManagerPort();

// List all inventory
List<Inventory> products = port.listAllInventory();

// REST Client
Client client = ClientBuilder.newClient();
WebTarget target = client.target("http://localhost:9080/api/v1");

Response response = target.path("products")
    .request(MediaType.APPLICATION_JSON)
    .get();

if (response.getStatus() == 200) {
    String json = response.readEntity(String.class);
    // Process JSON response
}
```

### JavaScript/Node.js Example

```javascript
// REST API Call
const axios = require('axios');

async function getProducts() {
  try {
    const response = await axios.get('http://localhost:9080/api/v1/products', {
      params: {
        page: 1,
        size: 20,
        category: 1
      }
    });
    console.log(response.data);
  } catch (error) {
    console.error('Error:', error.response.data);
  }
}

// WebSocket Connection
const WebSocket = require('ws');
const ws = new WebSocket('ws://localhost:9080/ws/inventory');

ws.on('open', function open() {
  console.log('Connected to inventory updates');
});

ws.on('message', function incoming(data) {
  const update = JSON.parse(data);
  console.log('Inventory update:', update);
});
```

---

**Next**: [Troubleshooting Guide](Troubleshooting-Guide.md) | [Configuration Guide](Configuration-Guide.md)