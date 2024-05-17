
### A. Get high-rated books

The instructions were short, further considerations and test to be implemented depending on the demands of the client:

- book of the same title with different ratings
- book with empty title
- duplicate books
- we assume that the books are correctly set, that all ratings are integers between 1 and 5 only (e.g. not 0 or 6)
- what happens with the database connection if it fails to connect (what kind of exception to send)

We test the following cases in `HighRatedBooksTest`:

- T1: List of Books is empty (no ratings)
- T2: There is only low rated books
- T3: There is only high rated books
- T4: There are mixed value rated books

1. What are the external dependencies? Which of these dependencies should be tested using doubles and which should not? Explain your rationale.

We have 3 external dependencies:

- `BookRatingsFetcher`
- `BookAuthorsFetcher` (created for part B)
- `DataBaseConnection`

We need to mock `BookRatingsFetcher` as it interact with the database, and we want to control the output, making it predictable.
There is no need for the `DatabaseConnection` class to be injected, as `BookRatingsFetcher` does not need it.
we mock `BookAuthorsFetcher` to fulfill the required parameter condition, but don't serve any purpose for this part

2. For the dependencies that should be tested using doubles, what refactoring should be done in the code? Do the refactoring and implement the tests.

 `BookManager` needs to refractor dependency injection, so that `BookRatingFetcher` and `BookAuthorsFetcher` can be passed instead of being instantiated
 
3. What are the disadvantages of using test doubles in your test suite? Answer with examples from the `BookManager` class.

- Mocks may not represent the real contract of the class.
- We trust that `DataBaseConnection` works correctly, we don't know in the real environments if it would work
- We had to restructure the `BookManager` code so that we could inject `BookRatingFetcher` into the code


### B. Get list of all authors

further considerations and test to be implemented depending on the demands of the client:

- What if there is Authors with the same name?
- empty field as name, what to do?
- allowed symbols in the name of Authors (letters, numbers, symbols)
- max character/ min character in name

We set up the authors, with modifications to `Book` and implementing `BookAuthorsFetcher` and we modify as well `BookManager` to handle to authors as well

we adjust as well all previous test to include authors

We test the following cases in `HighRatedBooksTest`:

- T1: List of Books is empty (no authors)
- T2: There is one unique author
- T3: There are multiple authors
- T4: There is a duplicate author

1. What are the external dependencies? Which of these dependencies should be tested using doubles and which should not? Explain your rationale.

We have 3 external dependencies:

- `BookRatingsFetcher`
- `BookAuthorsFetcher`
- `DataBaseConnection`

We need to mock `BookAuthorsFetcher` as it interact with the database, and we want to control the output, making it predictable.
There is no need for the `DatabaseConnection` class to be injected, as `BookRatingsFetcher` does not need it.
we mock `BookRatingsFetcher` to fulfill the required parameter condition, but don't serve any purpose for this part

2. For the dependencies that should be tested using doubles, what refactoring should be done in the code? Do the refactoring and implement the tests.

`BookManager` needs to refractor dependency injection, so that `BookRatingFetcher` and `BookAuthorsFetcher` can be passed instead of being instantiated

3. What are the disadvantages of using test doubles in your test suite? Answer with examples from the `BookManager` class.

- Mocks may not represent the real contract of the class.
- We trust that `DataBaseConnection` works correctly, we don't know in the real environments if it would work
- We had to restructure the `BookManager` code so that we could inject `BookRatingFetcher` into the code
- modification were needed in `HighRatedBooksTest` as we needed to change the format of `Books`