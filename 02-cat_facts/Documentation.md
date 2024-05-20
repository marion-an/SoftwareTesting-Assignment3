
further considerations and test to be implemented depending on the demands of the client:

- if longest return facts of same length, which one to choose
- what to do when {"message":"Not Found","code":404} (it doesnt have "fact" so doesn't fit format)
- test bound of limit (minimum, maximum, negative number)


We test the following cases in `CatFactsRetrieverTest`:

- T1: simple RetrieveRandom
- T2: simple RetrieveLongest
- T3: empty data for RetrieveRandom
- T4: empty data for retrieveLongest
- T5: network error for RetrieveRandom
- T6: network error for retrieveLongest


1. What are the external dependencies? Which of these dependencies should be tested using doubles and which should not? Explain your rationale.

We have 1 external dependencies:

- `HttpFetcher` - requests to an external API.

HttpFetcher should be tested using doubles. We want to isolate your tests to increase reliability and control the responses.


2. For the dependencies that should be tested using doubles, what refactoring should be done in the code? Do the refactoring and implement the tests.

adjusts `CatFactsRetriever` to accept an `HttpFetcher` through dependency injection.

3. What are the disadvantages of using test doubles in your test suite? Answer with examples from the `CatFactsRetriever` class.

- we are not testing the interaction between CatFactsRetriever and the actual HTTP service.
- we need to set up the HttpFetcher interface.
- we can get different exception (ioexception or jsonexception).
- there is significant maintenance overhead especially if the interface of the dependency changes.