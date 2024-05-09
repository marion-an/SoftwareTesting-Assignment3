## Dependencies
`NotificationService` and `LogService` should both be mocked. Both are third-party services
which makes it complex to set them up. Additionally, we want to test the `createTicket` method
and not the functionality of `NotificationService` and `LogService`. So mocking them allows us to have more control and 
faster simulations.

Additionally, can we mock `TicketRepository` to verify if a ticket is saved in the repository.


## Tests With Doubles
Before writing tests to test the correct behaviour of the `createTicket` method
we have to write tests to ensure correct behaviour for invalid input.

### Invalid Input

- `T1`: Ticket is null
- `T2`: Email of ticket is null or empty
- `T3`: Issue description of ticket is null or empty
- `T4`: Priority of ticket is null

-> all the above tests fail so we have to add preconditions to the `createTicket` method to ensure correct behaviour.
-> Tests pass.

Now we can check if `createTicket` calls `logTicketCreation` `notifyCustomer` of the third-party services correctly and
if the created ticket is saved to the `TicketRepository` by calling the repositories `save` method.

### Valid Input

- `T5`: Verify that `logTicketCreation` is called with the corresponding ticket.
- `T6`: Verify that `notifyCustomer` is called with the email of the corresponding ticket and the correct message.
- `T7`: Verify that the ticket is saved by verifying if the `TicketRepository.save()`method is called

### Valid Input But Third-party Service Fails

Finally, we test if a ticket is successfully created (saved in `TicketRepository`) regardless if `LogService` or 
`NotificationService` fails.

- `T8`: Verify `TicketRepository.save()`method is called with corresponding ticket if `notifyCusotomer` throws a Runtime Exception.
- `T9`: Verify `TicketRepository.save()`method is called with corresponding ticket if `logTicketCreation` throws a Runtime Exception.

-> Both test `T8` and `T9` fail. To ensure correct successfull ticket creation we can wrap the `notifyCustomer` and `logTicketCreation`
into try catch blocks.

-> `T8` and `T9` pass.

### Disadvantages of using Doubles

By using mocks to mock `NotificationService`, `LogService` `TicketRepository` we make the tests less realistic
since we control how the dependencies should behave. In a real world setting some of those dependencies might 
behave unexpectedly which is not covered in the test. Additionally, if the implementations of the dependencies change
by their provider we have to know about it and update the tests accordingly otherwise the tests won't fail. Mocks also
increase the coupling between the production and test code, so if we want to change some behaviour of the `createTicket` 
method we are forced to change the tests.

