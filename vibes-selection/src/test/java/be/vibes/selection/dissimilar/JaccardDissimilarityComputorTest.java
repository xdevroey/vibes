/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.vibes.selection.dissimilar;

/*-
 * #%L
 * VIBeS: test case selection
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
public class JaccardDissimilarityComputorTest {

    private static final Logger logger = LoggerFactory.getLogger(JaccardDissimilarityComputorTest.class);

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
    public void testGetDistanceNoIntersect() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str1 = "abc";
        String str2 = "def";
        double expected = 0.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }    

    @Test
    public void testGetDistanceIntersect() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str1 = "abcde";
        String str2 = "def";
        double expected = 2.0 / 6.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }
    
    @Test
    public void testGetDistanceIntersect2() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str1 = "abc";
        String str2 = "abe";
        double expected = 2.0 / 4.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }
    
    @Test
    public void testGetDistanceSame() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str = "abcde";
        double expected = 1.0;
        assertThat(dist.getDistance(toSet(str), toSet(str)), equalTo(expected));
    }
    
    @Test
    public void testGetDistanceEmpty() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str = "";
        double expected = 1.0;
        assertThat(dist.getDistance(toSet(str), toSet(str)), equalTo(expected));
    }
    
    @Test
    public void testGetDistanceOneEmpty() {
        JaccardDissimilarityComputor<Set<Character>> dist = new JaccardDissimilarityComputor();
        String str1 = "";
        String str2 = "abcdef";
        double expected = 0.0;
        assertThat(dist.getDistance(toSet(str1), toSet(str2)), equalTo(expected));
    }


}
