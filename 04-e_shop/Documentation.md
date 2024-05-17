# Messages

## A. Number of invocation

- In the book it's stated that entities should not be mocked. Therefore, the ``Order`` objects are instantiated.
- The implementation of the ``e-shop`` follows the Hexagonal Architecture. Therefore, the ``EventPublisher`` does not depend on low-level modules, instead it depends on an abstraction, the ``EventListener``. The ``EventListener`` is a port and both the ``InventoryManager`` and the ``ÈmailNotificationService`` are adapters.
- In order to mock the ``EventListener listeners`` a constructor is added to the class where this dependency will get injected.
- 
    - T1: test to verify that ``listener.onOrderPlaced(order)`` gets called the right number of times

- a boundary of the ``publishOrderToAllListeners`` method is that ``listeners`` contains one element and the ``listener.onOrderPlaced(order)`` is called once 
    - T2: ``listeners`` is empty
    - T3: ``listeners`` contains one element 

## B. Content of invocations - ``ArgumentCaptor``
The content of ``Order`` object which is passed to ``listener.onOrderPlaced(order)``is verified by using an argumentCaptor where it was differentiated between an ``Order`` object and ``null``
- T4: one order is placed, where the content is verified
- T5: ``Order`` to be placed is ``null``
- T6: one order is placed and there are multiple listeners, where the content is verified


## C. Content of invocations—Increasing observability
The content of ``Order`` object which is passed to ``listener.onOrderPlaced(order)``is verified by adding the ``Order`` object to a list where it was differentiated between an ``Order`` object and ``null``
- T7: one order is placed, where the content of the order is verified
- T8: ``Order`` to be published is ``null``
- T9: multiple ``listeners`` where the order content is verified

It was also verified that no content is present when there is no ``listener`` present
- T10: no ``listeners`` are present, verify that no order is placed

## D. Comparison

### Advantages
### ArgumentCaptor
#### Advantages
- Verify which arguments are passed to ``onOrderPlaced(Order order)`` without needing to change the source code
- Suitable to test void method
- Increases understandability as arguments expected are explicitly stated in the test

#### Disadvantages
- Requires setup

### Increase Observability
##### Advantages
- Increases testability
- Makes test methods simpler

##### Disadvantages
- Production code has to be modified

