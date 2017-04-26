package be.unamur.transitionsystem.dsl.test.mutation;

/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import static org.junit.Assert.*;
import static be.unamur.transitionsystem.dsl.test.mutation.ConfiguredMutagen.*;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.*;

import java.io.File;

import org.junit.Test;

import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

public class ConfiguredMutagenTest {

    private static final String CONFIG_FILE = "default-config.xml";

    @Test
    public void test() throws Exception {
        FeaturedTransitionSystem fts = loadFeaturedTransitionSystem(ConfiguredMutagenTest.class
                .getClassLoader().getResourceAsStream("aerouc5.fts"));
        assertNotNull(fts);
        File config = new File(ConfiguredMutagenTest.class.getClassLoader()
                .getResource(CONFIG_FILE).toURI());
        configure(config).mutate(fts);
    }

}
