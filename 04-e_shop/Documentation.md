# Messages

## A. Number of invocation

- In the book it's stated that entities should not be mocked. Therefore, the ``Order`` objects are instantiated.
- The implementation of the ``e-shop`` follows the Hexagonal Architecture. Therefore, the ``EventPublisher`` does not depend on low-level modules, instead it depends on an abstraction, the ``EventListener``. The ``EventListener`` is a port and both the ``InventoryManager`` and the ``ÈmailNotificationService`` are adapters.
- In order to mock the `EventListener listeners`` a constructor is added to the class where this dependency will get injected.
- 
    - T1: test to verify that ``messageService.sendMessage(message.getReceiver(), message.getContent())`` gets called
      the right number of times

- a boundary of the ``publishOrderToAllListeners`` method is that ``listeners`` contains one element
    - T2: ``listeners`` is empty
    - T3: ``listeners`` contains one element 

## B. Content of invocations - ``ArgumentCaptor``

- 


## C. Content of invocations—Increasing observability
- 

## D. Comparison

### Advantages

| ArgumentCaptor                                                                                                   | Increase Observability |
|------------------------------------------------------------------------------------------------------------------|------------------| 
|  |                  |


### Disadvantages
| ArgumentCaptor | Increase Observability             |
|-------|------------------------------------| 
|  | |


---> Screenshot
--> Assets folder in each exercise