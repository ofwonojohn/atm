# ATM System - Spring Boot Application

A complete ATM System implementation using Java, Spring Boot, and Thymeleaf with a clean MVC architecture. This is a web-based ATM application with user authentication, balance management, transactions (withdrawals/deposits), and transaction history.

## 📋 Project Overview

This ATM System demonstrates best practices in Spring Boot development with:
- **Clean Architecture**: Proper separation of concerns with MVC pattern
- **Database**: H2 in-memory database for simplicity
- **Web Framework**: Spring Boot with Thymeleaf templates
- **ORM**: Spring Data JPA with Hibernate
- **Validation**: Bean validation and custom exception handling
- **Styling**: Bootstrap 5 with custom CSS

## 🎯 Features

### Functional Requirements
✅ User authentication using Account Number + PIN
✅ Display account balance
✅ Withdraw cash (in multiples of 100)
✅ Deposit cash (in multiples of 100)
✅ View transaction history
✅ Logout functionality
✅ Account lockout after 3 failed login attempts

### Technical Implementation
- Spring Boot 4.0.2
- Spring Data JPA with H2 Database
- Thymeleaf template engine
- Bootstrap 5 UI framework
- Lombok for reducing boilerplate code
- Maven build system

## 📁 Project Structure

```
atm/
├── src/
│   ├── main/
│   │   ├── java/com/atm/atm/
│   │   │   ├── AtmApplication.java           # Main Spring Boot Application
│   │   │   ├── entity/                       # JPA Entities
│   │   │   │   ├── Account.java             # Account entity
│   │   │   │   └── Transaction.java         # Transaction entity
│   │   │   ├── repository/                   # Spring Data JPA Repositories
│   │   │   │   ├── AccountRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── service/                      # Business Logic Layer
│   │   │   │   ├── AccountService.java      # Authentication & account operations
│   │   │   │   └── ATMService.java          # ATM operations (withdraw, deposit)
│   │   │   ├── controller/                   # MVC Controllers
│   │   │   │   ├── HomeController.java      # Home page redirects
│   │   │   │   ├── AuthController.java      # Login/Logout
│   │   │   │   └── ATMController.java       # ATM operations
│   │   │   ├── dto/                          # Data Transfer Objects
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── WithdrawRequest.java
│   │   │   │   ├── DepositRequest.java
│   │   │   │   ├── AccountDTO.java
│   │   │   │   └── TransactionDTO.java
│   │   │   ├── exception/                    # Custom Exceptions & Global Handler
│   │   │   │   ├── AccountNotFoundException.java
│   │   │   │   ├── InvalidPinException.java
│   │   │   │   ├── InsufficientBalanceException.java
│   │   │   │   ├── InvalidAmountException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── init/                         # Data Initialization
│   │   │       └── DataInitializer.java     # Sample data on startup
│   │   └── resources/
│   │       ├── application.properties        # Spring Boot configuration
│   │       ├── templates/                    # Thymeleaf templates
│   │       │   ├── login.html               # Login page
│   │       │   ├── dashboard.html           # Main dashboard
│   │       │   ├── withdraw.html            # Withdraw page
│   │       │   ├── deposit.html             # Deposit page
│   │       │   ├── transaction-history.html # Transaction history
│   │       │   ├── error.html               # Error page
│   │       │   └── base.html                # Base layout
│   │       └── static/
│   │           └── css/
│   │               └── style.css             # Custom Bootstrap styles
│   └── test/
│       └── java/com/atm/atm/
│           └── AtmApplicationTests.java      # Application tests
├── pom.xml                                   # Maven configuration
└── README.md                                 # This file
```

## 🗄️ Database Schema

### Accounts Table
```sql
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(50) UNIQUE NOT NULL,
    pin VARCHAR(50) NOT NULL,
    account_holder_name VARCHAR(255) NOT NULL,
    balance DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    created_date TIMESTAMP NOT NULL,
    last_transaction_date TIMESTAMP,
    failed_login_attempts INT NOT NULL
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    balance_after_transaction DOUBLE NOT NULL,
    description VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);
```

## 🔐 Test Credentials

The application comes with 3 pre-loaded test accounts:

| Account Number | PIN  | Name              | Balance      |
|---|------|-------------------|------|
| 1001           | 1234 | John Doe          | Rs. 50,000   |
| 1002           | 5678 | Jane Smith        | Rs. 75,000   |
| 1003           | 9012 | Robert Johnson    | Rs. 1,00,000 |

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation & Running

1. **Clone the repository** (if using version control):
```bash
cd c:\Users\JOHN PAUL\Desktop\a_t_m\atm
```

2. **Build the project**:
```bash
mvn clean install
```

3. **Run the application**:
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/atm-0.0.1-SNAPSHOT.jar
```

4. **Access the application**:
   - Open browser and navigate to: `http://localhost:8080`
   - You'll be redirected to the login page

5. **H2 Database Console** (for development):
   - Access at: `http://localhost:8080/h2-console`
   - URL: `jdbc:h2:mem:atmdb`
   - Username: `sa`
   - Password: (leave empty)

## 📱 User Interface

### Pages Overview

1. **Login Page** (`/auth/login`)
   - Account number and PIN authentication
   - Error messages for invalid credentials
   - Account lockout after 3 failed attempts
   - Test credentials display

2. **Dashboard** (`/atm/dashboard`)
   - Account information display
   - Current balance
   - Quick action buttons
   - Account status

3. **Withdraw Page** (`/atm/withdraw`)
   - Amount input with validation
   - Quick amount buttons (500, 1000, 2000, 5000)
   - Balance validation
   - Success/error messages

4. **Deposit Page** (`/atm/deposit`)
   - Amount input with validation
   - Quick amount buttons
   - Success/error messages

5. **Transaction History** (`/atm/history`)
   - Complete transaction table with:
     - Date and time
     - Transaction type (Withdrawal/Deposit)
     - Amount
     - Running balance
     - Transaction status

6. **Error Page** (`/error`)
   - Friendly error messages
   - Navigation back to login

## 🔧 API Endpoints

### Authentication
- `GET /auth/login` - Display login page
- `POST /auth/login` - Process login
- `GET /auth/logout` - Logout and invalidate session

### ATM Operations
- `GET /atm/dashboard` - Display dashboard
- `GET /atm/withdraw` - Display withdraw form
- `POST /atm/withdraw` - Process withdrawal
- `GET /atm/deposit` - Display deposit form
- `POST /atm/deposit` - Process deposit
- `GET /atm/history` - Display transaction history

## 🛡️ Validation & Error Handling

### Input Validation
- Account number and PIN are required for login
- Amount must be positive and in multiples of 100
- All validations use Java Bean Validation (@NotNull, @Positive, etc.)

### Error Handling
- `AccountNotFoundException` - Account not found
- `InvalidPinException` - Wrong PIN or account locked
- `InsufficientBalanceException` - Not enough balance for withdrawal
- `InvalidAmountException` - Invalid transaction amount

### Security Features
- PIN authentication
- Account lockout after 3 failed login attempts
- Session-based authentication
- Session invalidation on logout
- Account status tracking (ACTIVE, LOCKED, INACTIVE)

## 📊 Key Classes & Responsibilities

### Entity Layer
- **Account**: Represents a bank account with balance and authentication details
- **Transaction**: Records all account transactions

### Service Layer
- **AccountService**: Handles authentication, account lookups, and failed login tracking
- **ATMService**: Manages withdrawals, deposits, and transaction history

### Controller Layer
- **AuthController**: Manages login/logout flows
- **ATMController**: Handles dashboard, withdrawals, deposits, and history
- **HomeController**: Redirects root to login

### DTO Layer
- Separates entity models from API responses
- Prevents exposure of sensitive data (e.g., PIN)

## 🎨 Styling & UI

- **Bootstrap 5**: Responsive grid system and components
- **Custom CSS**: Enhanced styling with animations and transitions
- **Mobile Responsive**: Works on desktop, tablet, and mobile devices
- **Color Scheme**:
  - Primary: Blue (#007bff)
  - Success: Green (#28a745)
  - Warning: Yellow (#ffc107)
  - Danger: Red (#dc3545)

## 🔄 Transaction Flow

### Withdrawal Flow
1. User navigates to withdraw page
2. Enters amount (must be in multiples of 100)
3. System validates:
   - Amount is positive
   - Amount is multiple of 100
   - Sufficient balance exists
4. Deduct amount from balance
5. Create transaction record
6. Display success message and updated balance

### Deposit Flow
1. User navigates to deposit page
2. Enters amount (must be in multiples of 100)
3. System validates:
   - Amount is positive
   - Amount is multiple of 100
4. Add amount to balance
5. Create transaction record
6. Display success message and updated balance

## 🔐 Security Considerations

**For Production, implement:**
- PIN hashing (BCrypt, PBKDF2, or similar)
- SSL/TLS encryption for all communications
- CSRF protection
- Rate limiting
- Account lockout mechanisms
- Two-factor authentication
- Audit logging
- Data encryption at rest

## 📝 Code Quality Features

- **Dependency Injection**: Using Spring's IoC container
- **Lombok**: Reduces boilerplate with @Data, @RequiredArgsConstructor
- **Comments**: Clear javadoc and inline comments
- **Separation of Concerns**: Distinct layers (Entity, Service, Controller)
- **Transaction Management**: @Transactional annotation for data consistency
- **Exception Handling**: Custom exceptions with descriptive messages

## 🧪 Testing

Run tests with:
```bash
mvn test
```

## 📚 Technologies Used

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA**
- **Hibernate ORM**
- **H2 Database**
- **Thymeleaf 3.x**
- **Bootstrap 5**
- **Lombok**
- **Maven**

## 🤝 Best Practices Implemented

1. ✅ MVC Architecture Pattern
2. ✅ Single Responsibility Principle
3. ✅ Dependency Injection
4. ✅ DTOs for API responses
5. ✅ Global Exception Handling
6. ✅ Input Validation
7. ✅ Transaction Management
8. ✅ Session Management
9. ✅ Clean Code Principles
10. ✅ Responsive UI Design

## 🚧 Future Enhancements

- [ ] JWT-based authentication
- [ ] User registration
- [ ] Fund transfer between accounts
- [ ] PIN change functionality
- [ ] Mobile app
- [ ] Multi-currency support
- [ ] Interest calculation
- [ ] Account statements (PDF export)
- [ ] Email notifications
- [ ] Advanced analytics dashboard

## 📄 License

This project is open source and available for educational purposes.

## 👤 Author

Created as a demonstration of Spring Boot best practices and clean architecture.

---

**Happy Banking! 🏦**
