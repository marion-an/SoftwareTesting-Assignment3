# Messages

## A. Number of invocation

- In the book it's stated that entities should not be mocked. Therefore, the ``Message`` objects are instantiated.
- The ``MessageService`` depends on external infrastructure. Therefore, this class will be mocked.
  To be able to mock ``MessageService`` during testing, the ``MessageProcessor`` class has to be
  refactored. ``MessageService`` will be injected via a constructor.
    - T1: test to verify that ``messageService.sendMessage(message.getReceiver(), message.getContent())`` gets called
      the right number of times

- a boundary of the ``processMessages`` method is that the input List ``messages`` contains no ``Message`` object
    - T2: ``messages`` is empty
    - T3: ``messages`` contains one ``Message`` object

    - T4: ``messages`` contains the same ``Message`` object multiple times

## B. Content of invocations - ``ArgumentCaptor``
The content of ``Message`` objects which are passed to ``sendMessage(String receiver, String message)``is verified by using an argumentCaptor
- T5: one message is sent, where the content is verified
- T6: ``Message`` to be sent has ``null`` as receiver and content
- T7: multiple messages are sent, where the content is verified


## C. Content of invocations—Increasing observability
The content of ``Message`` objects which are passed to ``sendMessage(String receiver, String message)``is verified by adding the ``Message`` objects to a list which is returned
- T8: one message is sent, where the content is verified
- T9: ``Message`` to be sent has ``null`` as receiver and content
- T10: multiple messages are sent, where the content is verified
- T11: ``messages`` is empty

## D. Comparison

### ArgumentCaptor
#### Advantages
- Verify which arguments are passed to ``sendMessage(String receiver, String message)`` without needing to change the source code
- Increases understandability as arguments expected are explicitly stated in the test

#### Disadvantages
- Requires setup

### Increase Observability
##### Advantages
- Makes it easier to assert the behaviour of ``sendMessage(String receiver, String message)``
- Increases testability

##### Disadvantages
- Production code has to be modified

