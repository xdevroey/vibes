/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.vibes.selection.dissimilar;

import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static be.vibes.selection.dissimilar.TestUtils.*;
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
public class HammingDissimilarityComputorTest {

    private static final Logger logger = LoggerFactory.getLogger(HammingDissimilarityComputorTest.class);

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
    public void testGetDistanceSame() throws Exception{
        Set<Character> allElements = Sets.newHashSet('a', 'b', 'c', 'd', 'e', 'f');
        HammingDissimilarityComputor<Set<Character>> dist = new HammingDissimilarityComputor(allElements);
        assertThat(dist.allPossibleElementsCount(), equalTo(allElements.size()));
        String str = "abc";
        double expected = 1.0;
        assertThat(dist.getDistance(toSet(str), toSet(str)), equalTo(expected));
    }
    
    @Test
    public void testGetDistanceDifferentNonIntersect() throws Exception{
        Set<Character> allElements = Sets.newHashSet('a', 'b', 'c', 'd', 'e', 'f');
        HammingDissimilarityComputor<Set<Character>> dist = new HammingDissimilarityComputor(allElements);
        assertThat(dist.allPossibleElementsCount(), equalTo(allElements.size()));
        String str1 = "abc";
        String str2 = "def";
        double expected = 0.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }
        
    @Test
    public void testGetDistanceDifferentIntersect() throws Exception{
        Set<Character> allElements = Sets.newHashSet('a', 'b', 'c', 'd', 'e', 'f');
        HammingDissimilarityComputor<Set<Character>> dist = new HammingDissimilarityComputor(allElements);
        assertThat(dist.allPossibleElementsCount(), equalTo(allElements.size()));
        String str1 = "abcde";
        String str2 = "cdef";
        double expected = 0.5;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }
        
    @Test
    public void testGetDistanceDifferentCover() throws Exception{
        Set<Character> allElements = Sets.newHashSet('a', 'b', 'c', 'd', 'e', 'f');
        HammingDissimilarityComputor<Set<Character>> dist = new HammingDissimilarityComputor(allElements);
        assertThat(dist.allPossibleElementsCount(), equalTo(allElements.size()));
        String str1 = "abcdef";
        String str2 = "cde";
        double expected = 0.5;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }
            
    @Test
    public void testGetDistanceEmpty() throws Exception{
        Set<Character> allElements = Sets.newHashSet('a', 'b', 'c', 'd', 'e', 'f');
        HammingDissimilarityComputor<Set<Character>> dist = new HammingDissimilarityComputor(allElements);
        assertThat(dist.allPossibleElementsCount(), equalTo(allElements.size()));
        String str1 = "";
        String str2 = "";
        double expected = 1.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }

}