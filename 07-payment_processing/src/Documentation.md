# 07 Payment Processing

## Event Publisher
the `EventPublisher` class lets `AuditService` subscribe, so they can get Informatin sent to them. The Information is about transactions, once they are validated and processed they get loged. It does this by uing the `publishTransactionComplete` method. Furthermore a getter and setter for listeners was added to enhance testability.  

## A Number of invocations
The `onTransactionComplete` method takes a transaction as parameter. Then it distributes the transaction to all the listeners. This gives us the test cases where the list of listeners is: 
 - `T1` the list of listeners is empty
 - `T2` the list of listeners contains exactly one element
 - `T3` the list of listeners contains more than one element

## B Content of invocations
With the `ArgumentCaptor` the arguments which are passed to a function can be captured. By adding a name to a transaction it can be verified that the transaction was correctly passed to the `onTransactionComeplete` method. Furthermore we can add attributes to the Transaction such as `verified` and `processed. These are great for testabiltiy and migth help when the code gets expanded in the future.
- `T4` input transaction is null
- `T5` call with transaction name

## Payment Processor
the `PaymentProcessor` class has a constructor which allows to inject the dependencies `EventPublisher`, `TransactionService` and `FraudDetectionService`. Furthermore it can process payments which start by using the `FrauDetectionService` to block transactions which are recognized as fraud. After the check it uses the `TransactionService` to handle the transactions and finally publishes it over the `EventPublisher`

## C Content of invocations—increasing observability
By adding getter methods to the `PaymentProcessor` class the observability can be increased. Furthermore the attributes `processCallCounter`, `calledWith` and `fraudCounter` can help verify testing.
- `T6` check if function got called
- `T7` arguments get passed properly
- `T8` null as argument
- `T9` multiple calls
- `T10` multiple calls - some frauds

## D Comparison
- B
  - Advantages
    - Easy way to verify Method calls
    - Can be setup quickly
  - Disadvantages
    - coupling to production code
    - readability of code decreases
- C
  - Advantages
    - can improve overall design
    - less coupling
  - Disadvantages
    - need to take some time to decide design
    - need to adapt source code