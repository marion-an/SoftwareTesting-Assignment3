import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieStreamingManagerTest{
    @Mock
    CacheService cacheService;
    @Mock
    FileStreamService fileStreamService;

    MovieStreamingManager movieStreamingManager;

    @BeforeEach
    void setUp(){
        this.movieStreamingManager = new MovieStreamingManager(fileStreamService, cacheService);
    }

    @Test
    void streamMovieAndMovieIdIsNull(){
        Exception exception = assertThrows(Exception.class, () -> movieStreamingManager.streamMovie(null));
        assertEquals("movieId cannot be null", exception.getMessage());
    }

    @Test
    void streamMovieIdAndIdIsEmpty(){
        Exception exception = assertThrows(Exception.class, () -> movieStreamingManager.streamMovie(""));
        assertEquals("movieId cannot be empty", exception.getMessage());
    }

    @Test
    void streamMovieAndCacheHoldsInformation() throws Exception {
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");
        String movieId = "1";
        String streamToken = "token";
        StreamingDetails streamingDetails = new StreamingDetails(movieId, streamToken, metadata);

        when(cacheService.getDetails(movieId)).thenReturn(streamingDetails);

        StreamingDetails actualDetails = movieStreamingManager.streamMovie(movieId);

        assertEquals(streamingDetails, actualDetails);
    }

    @Test
    void streamMovieAndCacheDoesNotHaveInformationMovieIdExists() throws Exception {
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");
        String movieId = "1";
        String streamToken = "token";

        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(metadata);
        when(fileStreamService.generateToken(movieId)).thenReturn(streamToken);

        StreamingDetails actualDetails = movieStreamingManager.streamMovie(movieId);
        ArgumentCaptor<StreamingDetails> captor = ArgumentCaptor.forClass(StreamingDetails.class);

        verify(cacheService).cacheDetails(eq(movieId), captor.capture());

        StreamingDetails generatedDetails = captor.getValue();

        assertEquals(generatedDetails.getMetadata(), metadata);
        assertEquals(generatedDetails.getMovieId(), movieId);
        assertEquals(generatedDetails.getStreamToken(), streamToken);

        assertEquals(actualDetails.getMetadata(), metadata);
        assertEquals(actualDetails.getMovieId(), movieId);
        assertEquals(actualDetails.getStreamToken(), streamToken);
    }

    @Test
    void streamMovieAndCacheDoesNotHaveInformationMovieIdDoesNotExists() throws Exception {
        String movieId = "1";

        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, () -> movieStreamingManager.streamMovie(movieId));
        String expectedErrorMessage = "movieId does not exists";

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }

    @Test
    void streamMovieGettingMovieDetailsFromCacheFails() throws Exception {
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");
        String movieId = "1";
        String streamToken = "token";

        doThrow(new RuntimeException()).when(cacheService).getDetails(movieId);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(metadata);
        when(fileStreamService.generateToken(movieId)).thenReturn(streamToken);

        StreamingDetails actualDetails = movieStreamingManager.streamMovie(movieId);

        verify(cacheService).cacheDetails(movieId, actualDetails);

        assertEquals(actualDetails.getMetadata(), metadata);
        assertEquals(actualDetails.getMovieId(), movieId);
        assertEquals(actualDetails.getStreamToken(), streamToken);
    }

    @Test
    void streamMovieRetrievingMovieFromFileStreamServiceFails() throws Exception{
        String movieId = "1";

        when(cacheService.getDetails(movieId)).thenReturn(null);
        doThrow(new RuntimeException()).when(fileStreamService).retrieveMovie(movieId);

        Exception exception = assertThrows(RuntimeException.class, () -> movieStreamingManager.streamMovie(movieId));
        String expectedErrorMessage = "FileStreamService is currently experiencing issues";

        assertEquals(exception.getMessage(), expectedErrorMessage);
    }

    @Test
    void streamMovieGeneratingTokenWithFileStreamServiceFails() throws Exception{
        String movieId = "1";
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");

        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(metadata);

        doThrow(new RuntimeException()).when(fileStreamService).generateToken(movieId);

        Exception exception = assertThrows(RuntimeException.class, () -> movieStreamingManager.streamMovie(movieId));
        String expectedErrorMessage = "File Streaming Service failed to generate a streaming token";

        assertEquals(exception.getMessage(), expectedErrorMessage);

    }

    @Test
    void streamMovieCachingDetailsInCacheFails() throws Exception{
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");
        String movieId = "1";
        String streamToken = "token";

        when(cacheService.getDetails(movieId)).thenReturn(null);
        when(fileStreamService.retrieveMovie(movieId)).thenReturn(metadata);
        when(fileStreamService.generateToken(movieId)).thenReturn(streamToken);

        ArgumentCaptor<StreamingDetails> captor = ArgumentCaptor.forClass(StreamingDetails.class);
        doThrow(new RuntimeException()).when(cacheService).cacheDetails(eq(movieId), captor.capture());

        StreamingDetails actualDetails = movieStreamingManager.streamMovie(movieId);

        assertEquals(actualDetails.getMetadata(), metadata);
        assertEquals(actualDetails.getMovieId(), movieId);
        assertEquals(actualDetails.getStreamToken(), streamToken);
    }

    @Test
    void updateMovieMetaDataMovieIdEmptyOrNull(){
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");
        Exception exceptionMovieIdIsNull = assertThrows(Exception.class, () -> movieStreamingManager.updateMovieMetadata(null, metadata));
        assertEquals("movieId cannot be null", exceptionMovieIdIsNull.getMessage());

        Exception exceptionMovieIdIsEmptyString = assertThrows(Exception.class, () -> movieStreamingManager.updateMovieMetadata("", metadata));
        assertEquals("movieId cannot be empty", exceptionMovieIdIsEmptyString.getMessage());
    }

    @Test
    void updateMovieMetaDataMetaDataIsNull(){
        String movieId = "1";
        Exception exceptionMovieIdIsNull = assertThrows(Exception.class, () -> movieStreamingManager.updateMovieMetadata(movieId, null));
        assertEquals("metadata cannot be null", exceptionMovieIdIsNull.getMessage());
    }

    @Test
    void updateMovieMetaDataSuccessfullyServicesAreWorking() throws Exception {
        String movieId = "1";
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");

        movieStreamingManager.updateMovieMetadata(movieId, metadata);

        verify(cacheService).refreshCache(movieId, metadata);
        verify(fileStreamService).updateMetadata(movieId, metadata);

    }

    @Test
    void updateMovieMetaDataServicesFail() throws Exception {
        String movieId = "1";
        MovieMetadata metadata = new MovieMetadata("Pulp Fiction", "A classic");

        doThrow(new RuntimeException()).when(cacheService).refreshCache(movieId, metadata);


        Exception exceptionCacheServiceFails = assertThrows(RuntimeException.class, () -> movieStreamingManager.updateMovieMetadata(movieId, metadata));
        assertEquals("failed to update the movies metadata", exceptionCacheServiceFails.getMessage());

        doThrow(new RuntimeException()).when(fileStreamService).updateMetadata(movieId, metadata);


        Exception exceptionFileStreamServiceFails = assertThrows(RuntimeException.class, () -> movieStreamingManager.updateMovieMetadata(movieId, metadata));
        assertEquals("failed to update the movies metadata", exceptionCacheServiceFails.getMessage());

    }

    @Test
    void validateStreamingTokenAndTokenIsNullOrEmpty(){
        Exception exceptionNullToken = assertThrows(Exception.class, ()->movieStreamingManager.validateStreamingToken(null));
        assertEquals("token cannot be empty or null", exceptionNullToken.getMessage());

        Exception exceptionEmptyToken = assertThrows(Exception.class, ()->movieStreamingManager.validateStreamingToken(""));
        assertEquals("token cannot be empty or null", exceptionEmptyToken.getMessage());
    }

    @Test
    void validateStreamingTokenValidToken() throws Exception {
        String token = "token";

        when(fileStreamService.isValidToken(token)).thenReturn(true);

        boolean result = movieStreamingManager.validateStreamingToken(token);
        assertTrue(result);
    }

    @Test
    void validateStreamingTokenInvalidToken() throws Exception {
        String token = "token";

        when(fileStreamService.isValidToken(token)).thenReturn(false);

        boolean result = movieStreamingManager.validateStreamingToken(token);
        assertFalse(result);
    }

    @Test
    void validateStreamingTokenFileStreamServiceFails(){
        String token = "token";

        doThrow(new RuntimeException()).when(fileStreamService).isValidToken(token);

        Exception exception = assertThrows(RuntimeException.class, ()-> movieStreamingManager.validateStreamingToken(token));
        assertEquals("fileStreamService failed to validate the token", exception.getMessage());
    }
}