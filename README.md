# PHASEZERO Catalog Service

A Spring Boot microservice for managing product catalog data with comprehensive REST API endpoints.

## Architecture Overview

The application follows a layered architecture pattern with clear separation of concerns:

- **Controller Layer**: Handles HTTP requests/responses and API endpoint mapping
- **Service Layer**: Contains business logic and rule enforcement
- **Repository Layer**: Manages data persistence using in-memory storage
- **Model Layer**: Defines data structures and validation rules
- **DTO Layer**: Data transfer objects for API communication
- **Exception Layer**: Custom exception handling and error responses

### Key Design Decisions

- **In-Memory Storage**: Uses ConcurrentHashMap for thread-safe operations without external database dependencies
- **Validation**: Jakarta Bean Validation for input validation with custom error responses
- **Business Rules**: Automatic lowercase normalization for product names and duplicate prevention

## Build and Run Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Building the Application
```bash
mvn clean compile
```

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Running Tests
```bash
mvn test
```

## API Endpoints

### 1. Create Product
- **POST** `/products`
- **Description**: Creates a new product with validation
- **Request Body**:
```json
{
  "partNumber": "P001",
  "partName": "Hydraulic Filter",
  "category": "Filters",
  "price": 25.99,
  "stock": 100
}
```
- **Response**: Created product with generated ID and timestamp
- **Status Codes**: 201 (Created), 400 (Bad Request), 409 (Conflict)

### 2. List All Products
- **GET** `/products`
- **Description**: Returns all products in the catalog
- **Response**: Array of product objects

### 3. Search Products by Name
- **GET** `/products/search?name={searchTerm}`
- **Description**: Case-insensitive search in product names
- **Example**: `/products/search?name=filter`

### 4. Filter by Category
- **GET** `/products/category/{categoryName}`
- **Description**: Returns products belonging to specified category
- **Example**: `/products/category/Filters`

### 5. Sort Products by Price
- **GET** `/products/sorted`
- **Description**: Returns all products sorted by price (ascending)

### 6. Calculate Inventory Value
- **GET** `/products/inventory/value`
- **Description**: Returns total inventory value (sum of price × stock)
- **Response**:
```json
{
  "totalValue": 2599.00
}
```

## Sample API Usage

### Creating a Product
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "partNumber": "HF001",
    "partName": "Premium Oil Filter",
    "category": "Filters",
    "price": 15.99,
    "stock": 50
  }'
```

### Searching Products
```bash
curl "http://localhost:8080/products/search?name=filter"
```

### Getting Inventory Value
```bash
curl http://localhost:8080/products/inventory/value
```

## Business Rules Implementation

1. **Part Name Normalization**: All product names are automatically converted to lowercase before storage
2. **Unique Part Numbers**: System prevents duplicate part numbers with 409 Conflict response
3. **Price Validation**: Prices must be positive (greater than 0)
4. **Stock Validation**: Stock levels cannot be negative (minimum 0)

## Error Handling

The API returns consistent error responses with appropriate HTTP status codes:

```json
{
  "message": "Product with part number P001 already exists",
  "code": "DUPLICATE_PART_NUMBER",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Assumptions and Limitations

- **Data Persistence**: Uses in-memory storage; data is lost on application restart
- **Concurrency**: Thread-safe operations using ConcurrentHashMap
- **Search**: Case-insensitive partial matching for product name search
- **Category Filtering**: Exact match (case-insensitive) for category filtering
- **Scalability**: Suitable for moderate data volumes; consider database for production use

## Additional Features

- **Timestamps**: Automatic creation timestamp for audit purposes
- **Thread Safety**: Concurrent access support for multi-user scenarios
- **Input Validation**: Comprehensive validation with descriptive error messages
- **RESTful Design**: Follows REST conventions for intuitive API usage