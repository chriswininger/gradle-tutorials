/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package musicservice;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class MusicCollectionServiceTest {
    @Test public void getMusicCollection_shoudWorkCorrectly() {
        final MusicCollectionService classUnderTest = new MusicCollectionService();
        final List<String> result = classUnderTest.getMusicCollection();

        assertTrue("someLibraryMethod should return 'true'", result.size() == 2);
    }
}