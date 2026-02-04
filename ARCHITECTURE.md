# ATM System - Architecture Documentation

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                             │
│                   (Web Browser - Thymeleaf)                     │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  Login Page │ Dashboard │ Withdraw │ Deposit │ History     ││
│  │         (Responsive Bootstrap UI)                           ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                              ↕ HTTP/HTTPS
┌─────────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                            │
│              (Spring MVC - Controllers)                         │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ AuthController  │ ATMController │ HomeController        │  │
│  │  - Login        │  - Dashboard  │  - Root Redirect      │  │
│  │  - Logout       │  - Withdraw   │                       │  │
│  │  - Validation   │  - Deposit    │                       │  │
│  │                │  - History    │                       │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│           (Business Logic & Core Functionality)                 │
│  ┌──────────────────────┐      ┌──────────────────────────┐    │
│  │  AccountService      │      │  ATMService             │    │
│  │  ─────────────────   │      │  ────────────────────   │    │
│  │  • authenticate()    │      │  • withdraw()           │    │
│  │  • getAccount()      │      │  • deposit()            │    │
│  │  • validatePin()     │      │  • getTransactions()    │    │
│  │  • lockAccount()     │      │  • calculateBalance()   │    │
│  └──────────────────────┘      └──────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER                             │
│         (Spring Data JPA - Repositories)                        │
│  ┌────────────────────────┐     ┌────────────────────────────┐ │
│  │ AccountRepository      │     │ TransactionRepository      │ │
│  │ ─────────────────────  │     │ ──────────────────────────│ │
│  │ • findByNumber()       │     │ • findByAccount()         │ │
│  │ • save()               │     │ • findByType()            │ │
│  │ • existsByNumber()     │     │ • save()                  │ │
│  └────────────────────────┘     └────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
                              ↕ Hibernate ORM
┌─────────────────────────────────────────────────────────────────┐
│                   DATA LAYER                                    │
│            (H2 In-Memory Database)                              │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │                                                              ││
│  │  ┌─────────────────┐        ┌─────────────────────────┐    ││
│  │  │  ACCOUNTS TABLE │        │  TRANSACTIONS TABLE    │    ││
│  │  │  ──────────────│        │  ─────────────────────│    ││
│  │  │  ID            │        │  ID                   │    ││
│  │  │  ACCOUNT_NO    │        │  ACCOUNT_ID  (FK)     │    ││
│  │  │  PIN           │        │  TYPE                 │    ││
│  │  │  NAME          │        │  AMOUNT               │    ││
│  │  │  BALANCE       │        │  BALANCE_AFTER        │    ││
│  │  │  STATUS        │        │  DESCRIPTION          │    ││
│  │  │  EMAIL         │        │  STATUS               │    ││
│  │  │  CREATED_DATE  │        │  TRANSACTION_DATE     │    ││
│  │  │  LAST_TXN_DATE │        │                       │    ││
│  │  │  FAILED_LOGINS │        │                       │    ││
│  │  └─────────────────┘        └─────────────────────────┘    ││
│  │                                                              ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
```

## Component Interaction Flow

### 1. Login Flow
```
User → Login Page
       ↓
   Enters Credentials (Account Number + PIN)
       ↓
  AuthController.login()
       ↓
  AccountService.authenticate()
       ↓
  AccountRepository.findByAccountNumber()
       ↓
  PIN Validation & Account Lock Check
       ↓
  Session Creation
       ↓
  Redirect to Dashboard
```

### 2. Withdrawal Flow
```
User → Withdraw Page
       ↓
   Enters Amount
       ↓
  ATMController.withdraw()
       ↓
  Validation (Amount > 0, multiple of 100)
       ↓
  ATMService.withdraw()
       ↓
  AccountRepository.save() (Update Balance)
       ↓
  TransactionRepository.save() (Create Record)
       ↓
  Session Update & Success Message
```

### 3. Transaction History Flow
```
User → History Page
       ↓
  ATMController.showTransactionHistory()
       ↓
  TransactionRepository.findByAccountOrderByDate()
       ↓
  Map to TransactionDTOs
       ↓
  Render History Table
```

## Design Patterns Used

### 1. **MVC Pattern**
   - Model: Entity classes (Account, Transaction)
   - View: Thymeleaf templates (HTML)
   - Controller: Spring controllers

### 2. **Repository Pattern**
   - Abstraction for data access
   - Spring Data JPA provides implementation

### 3. **Service/Business Logic Pattern**
   - Encapsulates business rules
   - AccountService, ATMService

### 4. **DTO Pattern**
   - Data Transfer Objects for API responses
   - Hides internal entity details

### 5. **Dependency Injection**
   - Spring's IoC container manages dependencies
   - @RequiredArgsConstructor with Lombok

### 6. **Global Exception Handler**
   - @ControllerAdvice for centralized error handling
   - Custom exceptions with meaningful messages

## Session Management

```
┌─────────────────┐
│  HTTP Request   │
└────────┬────────┘
         │
    ┌────▼────────────┐
    │ AuthController  │
    │  .login()       │
    └────┬────────────┘
         │
    ┌────▼───────────────────────┐
    │ Create Session Attributes: │
    │ • account (AccountDTO)      │
    │ • accountNumber             │
    └────┬───────────────────────┘
         │
    ┌────▼──────────────┐
    │ Subsequent        │
    │ Requests have     │
    │ access to session │
    └───────────────────┘
         │
    ┌────▼──────────────┐
    │ AuthController    │
    │ .logout()         │
    │ (invalidate)      │
    └────┬──────────────┘
         │
    ┌────▼──────────────┐
    │ Session cleared   │
    │ Redirect to login │
    └───────────────────┘
```

## Data Flow Diagram

```
┌─────────────────────────────────────────────────┐
│ USER INTERFACE (Thymeleaf Templates)            │
│ • login.html                                    │
│ • dashboard.html                                │
│ • withdraw.html                                 │
│ • deposit.html                                  │
│ • transaction-history.html                      │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ Form Submission / HTTP Request
┌──────────────────────────────────────────────────┐
│ CONTROLLERS (Request Handling)                  │
│ • @PostMapping("/auth/login")                  │
│ • @PostMapping("/atm/withdraw")                │
│ • @PostMapping("/atm/deposit")                 │
│ • @GetMapping("/atm/history")                  │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ Delegate to Service Layer
┌──────────────────────────────────────────────────┐
│ SERVICES (Business Logic)                       │
│ • validate input                                │
│ • check account status                          │
│ • verify balance                                │
│ • perform transactions                          │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ Perform CRUD Operations
┌──────────────────────────────────────────────────┐
│ REPOSITORIES (Data Access)                      │
│ • AccountRepository                             │
│ • TransactionRepository                         │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ SQL Queries via Hibernate
┌──────────────────────────────────────────────────┐
│ DATABASE (H2)                                   │
│ • ACCOUNTS table                                │
│ • TRANSACTIONS table                            │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ Data Response
┌──────────────────────────────────────────────────┐
│ DTOs & Entities (Convert to JSON/Objects)      │
│ • AccountDTO                                    │
│ • TransactionDTO                                │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ Model Attributes
┌──────────────────────────────────────────────────┐
│ THYMELEAF VIEW (Render HTML)                   │
│ • Update page with data                         │
│ • Display balance, transactions, messages       │
└──────────────────┬──────────────────────────────┘
                   │
                   ↓ HTTP Response
┌──────────────────────────────────────────────────┐
│ BROWSER (Display to User)                       │
└──────────────────────────────────────────────────┘
```

## Security Flow

```
LOGIN REQUEST
    ↓
[Validate Input]
    ↓
[Find Account in DB]
    ├─ Not found → AccountNotFoundException
    │
[Check Account Status]
    ├─ LOCKED → InvalidPinException
    │
[Validate PIN]
    ├─ Mismatch → Increment Failed Attempts
    │             ├─ Count >= 3 → Lock Account → Exception
    │             └─ Count < 3 → Show Remaining Attempts
    │
[PIN Match] → Reset Failed Attempts
    ↓
[Create Session]
    ↓
[Redirect to Dashboard]
```

## Exception Handling Architecture

```
┌──────────────────────────────────────────────┐
│ Method throws Exception                      │
└────────────────┬─────────────────────────────┘
                 │
        ┌────────▼────────────┐
        │ Spring catches and  │
        │ routes to          │
        │ @ControllerAdvice  │
        └────────┬────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
    ▼            ▼            ▼
[Account]  [InvalidPin]  [Insufficient]  [InvalidAmount]
[NotFound] [Exception]   [Balance]       [Exception]
[...]      [...]        [Exception]     [...]
                        [...]
    │            │            │
    └────────────┼────────────┘
                 │
        ┌────────▼───────────┐
        │ Set model attrs:   │
        │ • errorTitle       │
        │ • errorMessage     │
        └────────┬───────────┘
                 │
        ┌────────▼────────────┐
        │ Render error.html   │
        └─────────────────────┘
```

## Validation Chain

```
User Input → DTOs
    ↓
@NotBlank Validation
@NotNull Validation
@Positive Validation
    ↓
Custom Business Logic Validation
    ├─ Account exists?
    ├─ Amount in multiples of 100?
    ├─ Sufficient balance?
    └─ Valid transaction?
    ↓
Process or Reject
```

## Technology Stack Integration

```
┌─────────────────────────────────────┐
│  Spring Boot 4.0.2 (Core)           │
│  ├─ Spring Web (MVC Controllers)    │
│  ├─ Spring Data JPA (Persistence)   │
│  └─ Spring Validation               │
└─────────────────────────────────────┘
         ↓        ↓        ↓
    ┌────┴────┐   ├────┐   ├──────┐
    │Hibernate│   │H2  │   │Lombok│
    │ (ORM)   │   │DB  │   │      │
    └─────────┘   └────┘   └──────┘

┌─────────────────────────────────────┐
│  Thymeleaf (Template Engine)        │
│  ├─ HTML5 templates                 │
│  ├─ Server-side rendering           │
│  └─ Expression language support     │
└─────────────────────────────────────┘
         ↓
┌─────────────────────────────────────┐
│  Bootstrap 5 (Frontend Framework)   │
│  ├─ Responsive Grid                 │
│  ├─ Components (Buttons, Forms)     │
│  └─ CSS utilities                   │
└─────────────────────────────────────┘
         ↓
┌─────────────────────────────────────┐
│  Custom CSS (style.css)             │
│  ├─ Theme colors                    │
│  ├─ Animations                      │
│  └─ Responsive design               │
└─────────────────────────────────────┘
```

---

This architecture ensures:
- **Scalability**: Modular design allows easy extension
- **Maintainability**: Clear separation of concerns
- **Testability**: Each layer can be tested independently
- **Security**: Centralized validation and exception handling
- **Performance**: Optimized database queries through JPA
