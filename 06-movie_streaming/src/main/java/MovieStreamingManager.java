public class MovieStreamingManager {
    private FileStreamService fileStreamService;
    private CacheService cacheService;

    // Constructor to inject the file stream and cache services
    public MovieStreamingManager(FileStreamService fileStreamService, CacheService cacheService) {
        this.fileStreamService = fileStreamService;
        this.cacheService = cacheService;
    }

    // Method to stream a movie by its ID
    public StreamingDetails streamMovie(String movieId) throws Exception {
        assertValidMovieId(movieId);
        StreamingDetails details = this.getDetails(movieId);
        if (details == null) {
            MovieMetadata metadata = this.retrieveMovie(movieId);
            assertValidMetadata(metadata);
            String streamToken = this.generateToken(movieId);  // Assume there's a method to generate a streaming token
            details = new StreamingDetails(movieId, streamToken, metadata);
            this.cacheDetails(movieId, details);
        }
        return details;
    }

    // Additional methods can be added here for other functionalities
    public void updateMovieMetadata(String movieId, MovieMetadata metadata) throws Exception {
        assertValidMovieId(movieId);
        assertNotNullMetadata(metadata);

        try {
            fileStreamService.updateMetadata(movieId, metadata);
            cacheService.refreshCache(movieId, metadata);
        }catch (Exception e){
            throw new RuntimeException("failed to update the movies metadata");
        }
    }

    public boolean validateStreamingToken(String token) throws Exception {
        if(token == null || token.isEmpty()){
            throw new Exception("token cannot be empty or null");
        }
        try{
            return fileStreamService.isValidToken(token);
        }catch (Exception e){
            throw new RuntimeException("fileStreamService failed to validate the token");
        }
    }

    private void assertNotNullMetadata(MovieMetadata metadata){
        if(metadata == null){
            throw new NullPointerException("metadata cannot be null");
        }
    }


    private void assertValidMovieId(String movieId) throws Exception {
        if(movieId == null){
            throw new NullPointerException("movieId cannot be null");
        }
        if(movieId.isEmpty()){
            throw new Exception("movieId cannot be empty");
        }
    }

    private void assertValidMetadata(MovieMetadata metadata){
        if(metadata == null){
            throw new NullPointerException("movieId does not exists");
        }
    }

    private StreamingDetails getDetails(String movieId){
        try {
            StreamingDetails details = cacheService.getDetails(movieId);
            return details;
        }catch (Exception e){
            return null;
        }
    }

    private MovieMetadata retrieveMovie(String movieId){
        try {
            MovieMetadata metadata = fileStreamService.retrieveMovie(movieId);
            return metadata;
        }catch (Exception e){
            throw new RuntimeException("FileStreamService is currently experiencing issues");
        }
    }

    private String generateToken(String movieId){
        try {
            String streamToken = fileStreamService.generateToken(movieId);
            return streamToken;
        }catch (Exception e){
            throw new RuntimeException("File Streaming Service failed to generate a streaming token");
        }
    }

    private void cacheDetails(String movieId, StreamingDetails details){
        try {
            cacheService.cacheDetails(movieId, details);
        }catch (Exception e){
            System.out.println("Caching failed for movie with id " + movieId);
        }
    }


}
