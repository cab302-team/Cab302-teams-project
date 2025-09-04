package IntegrationTests.SQLite.DAOs;

import com.example.project.Models.sqlite.DAOs.DictionaryDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DictionaryDAOTests
{
    @Test
    void getWordDefinition_Exists()
    {
        // TODO use a copy of the production database for write tests.
        var dict = new DictionaryDAO();
        var result = dict.getWordDefinition("valid");
        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}
