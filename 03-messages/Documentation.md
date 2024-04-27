# Messages

## A. Number of invocation
- In the book it's stated that entities should not be mocked. Therefore, the ``Message`` objects are instantiated.
- The ``MessageService`` depends on external infrastructure. Therefore, this class will be mocked.
To be able to mock ``MessageService`` during testing, the ``MessageProcessor`` class has to be refactored. ``MessageService`` will be injected via a constructor.
  - T1: test to verify that ``messageService.sendMessage(message.getReceiver(), message.getContent())`` gets called the right number of times

- a boundary of the ``processMessages`` method is that the input List ``messages`` contains no ``Message`` object
  - T2: ``messages`` is empty
  - T3: ``messages`` contains one ``Message`` object

  - T4: ``messages`` contains the same ``Message`` object multiple times