package IntegrationTests.SQLite.DAOs;

import com.example.project.services.sqlite.dAOs.DictionaryDAO;
import org.junit.jupiter.api.Test;

import java.io.File;

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

    @Test
    void getWord_Exists()
    {
        var dict = new DictionaryDAO();
        var result = dict.isWordInDictionary("a");
        assertTrue(result);
    }

    @Test
    void getWord_DoesNotExist()
    {
        var dict = new DictionaryDAO();
        var result = dict.isWordInDictionary("x");
        assertFalse(result);
    }
}
