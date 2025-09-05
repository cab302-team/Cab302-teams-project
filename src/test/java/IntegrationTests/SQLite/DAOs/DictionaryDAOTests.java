package IntegrationTests.SQLite.DAOs;

import com.example.project.models.sqlite.dAOs.DictionaryDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * tests for DicitonaryDAO
 */
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
