## Dependencies

External Dependencies are the `CacheService` and `FileStreamService`.

## Tests

`MovieStreamingManager` has the method `streamMovie(String movieId)` which interacts with the methods 
`CacheService.getDetails(movieId)`, `FileStreamService.retrieveMovie(movieId)`, `FileStreamService.generateToken(movieId)`
and `CacheService.cacheDetails(movieId, details)`.

So to test `MovieStreamingManager` we will test the `streamMovie` method while mocking the two 
external dependencies identified.

### Invalide Input

- `T1`: `movieId` is null.
- `T2`: `movieId` is empty.

-> both tests fail -> preconditions with appropriate error messages have to be added
-> tests pass

### Valide Input

- `T3`: `CacheService` has the details saved about the movie with id `movieId`.
- `T4`: `CacheService` does not have the details saved about the movie with id `movieId` and `movieId` exists in
`fileStreamService`.
- `T5`: `CacheService` does not have the details saved about the movie with id `movieId` and `movieId` does not exist in
  `fileStreamService`.

-> `T5` fails. We add a check if `movieId` exists in `fileStreamService` and if not return an appropriate error message.
-> `T5` passes.

### Stimulating Failure

- `T6` `cacheService.getDetails` fails.
- `T7` `cacheService.cacheDetails` fails.
- `T8` `fileStreamService.retrieveMovie` fails.
- `T9` `fileStreamService.generateToken` fails.

-> all four tests fail. So we have to handle the failures. We can do this by extracting the methods of the 
services into private methods and if they fail we throw an appropriate error. If the cacheService fails the streaming token 
and the metadata of `movieId` should still be returned. If fileStreamService fails a new error with an appropriate error message
should be thrown.
-> Tests pass


## Testing Remaining Methods of `MovieStreamingManager`

As described in the `README.md` file `MovieStreamManager` also contains the two methods `updateMovieMetadata(String movieId, MovieMetadata metadata`
and `validateStreamingToken(String token)`. 
To separate infrastructure from domain code we implement the method `isValidToken` in `FileStreamService`.

For the `updateMovieMetadata` method an error is thrown is either `cacheService` or `fileStreamService` fails.
For the `validateStreamingToken` method an error is thrown if `fileStreamService` does not work.

### Tests for `updateMovieMetadata`

- `T10`: `movieId` is null or empty.
- `T11`: `metadata` is null.
- `T12`: Valid input and `fileStreamService` and `cacheService` work.
- `T13`: Valid input but `fileStreamService` and `cacheService` do not work.

### Tests for `validateStreamingToken`

- `T14`: `token` is null or empty.
- `T15`: Valid input and token is valid.
- `T16`: Valid input and token is not valid.
- `T17`: Valid input and `fileStreamService` fails.

