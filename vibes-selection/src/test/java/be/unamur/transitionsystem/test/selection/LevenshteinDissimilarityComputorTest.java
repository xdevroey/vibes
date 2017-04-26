package be.unamur.transitionsystem.test.selection;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static be.unamur.transitionsystem.test.selection.TestUtils.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LevenshteinDissimilarityComputorTest {

    private static final Logger logger = LoggerFactory.getLogger(LevenshteinDissimilarityComputorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testGetLevenshteinDistanceEmpty() {
        String str1 = "";
        String str2 = "";
        int expected = 0;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceOneEmpty() {
        String str1 = "";
        String str2 = "abc";
        int expected = 3;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceOtherEmpty() {
        String str1 = "abcdef";
        String str2 = "";
        int expected = 6;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceSameNonEmpty() {
        String str1 = "abcdef";
        String str2 = "abcdef";
        int expected = 0;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceFullDifferentWithSameSize() {
        String str1 = "abcdef";
        String str2 = "ghijkl";
        int expected = 6;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceFullDifferentWithSecondSmaller() {
        String str1 = "abcdef";
        String str2 = "gh";
        int expected = 6;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceFullDifferentWithFirstSmaller() {
        String str1 = "ab";
        String str2 = "ghijkl";
        int expected = 6;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

    @Test
    public void testGetLevenshteinDistanceOther() {
        String str1 = "meilenstein";
        String str2 = "levenshtein";
        int expected = 4;
        LevenshteinDissimilarityComputor<List<Character>> levenshtein = new LevenshteinDissimilarityComputor();
        assertThat(levenshtein.getDistance(toList(str1), toList(str2)), equalTo(expected));
    }

}
