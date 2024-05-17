import java.util.List;

public class BookAuthorsFetcher {
    private DatabaseConnection dbConnection;

    public BookAuthorsFetcher(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<String> all() {
    // This would normally fetch data from a database...The implementation is skipped.
    // ...
        return null;
    }

    public void close() {
        dbConnection.close();
    }
}
