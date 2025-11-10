# EagleBank

Endpoints Summary:

****

**ACCOUNT**

* **POST**    createAccount                   /v1/accounts
* **GET**     listAccounts                    /v1/accounts
* **GET**     fetchAccountByAccountNumber     /v1/accounts/{accountNumber}
* **PATCH**   updateAccountByAccountNumber    /v1/accounts/{accountNumber}
* **DELETE**  deleteAccountByAccountNumber    /v1/accounts/{accountNumber}

**-> CreateBankAccountRequest**
* String name
* String accountType

**-> UpdateBankAccountRequest**
* String name
* String accountType

**-> ListBankAccountsResponse**
* List<BankAccountResponse> bankAccountResponse

**-> BankAccountResponse**
* String accountNumber
* String sortCode
* String name
* String accountType
* double balance
* String currency
* String createdTimestamp
* String updatedTimestamp

****

**TRANSACTION**

* **POST**    createTransaction               /v1/accounts/{accountNumber}/transactions
* **GET**     listAccountTransaction          /v1/accounts/{accountNumber}/transactions
* **GET**     fetchAccountTransactionByID     /v1/accounts/{accountNumber}/transactions/{transactionId}

**-> CreateTransactionRequest**

* double amount
* String currency
* String type

**-> ListTransactionsResponse**

* List<TransactionResponse> transactionResponse

**-> TransactionResponse**

* String id
* double amount
* String currency
* String type
* String createdTimestamp

** **

**USER**

* **POST**    createUser                      /v1/users
* **GET**     fetchUserByID                   /v1/users/{userId}
* **PATCH**   updateUserByID                  /v1/users/{userId}
* **DELETE**  deleteUserByID                  /v1/users/{userId}

**-> CreateUserRequest**

* String name
* Address address
* String phoneNumber
* String email

**-> Address**

* String line1
* String line2
* String line3
* String town
* String county
* String postcode

**-> UpdateUserRequest**

* String name
* Address address
* String phoneNumber
* String email

**-> UserResponse**

* String id
* String name
* Address address
* String phoneNumber
* String email
* String createdTimestamp
* Address UpdatedTimestamp

** **



