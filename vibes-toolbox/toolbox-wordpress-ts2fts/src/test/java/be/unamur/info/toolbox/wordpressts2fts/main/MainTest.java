package be.unamur.info.toolbox.wordpressts2fts.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
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
public class MainTest {

    private static final Logger LOG = LoggerFactory.getLogger(MainTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testPLUGIN_REGEXP() {
        Pattern p = Pattern.compile(Main.PLUGIN_REGEXP);
        Matcher m = p.matcher("GET /wp-content/plugins/tell-a-friend/tell-a-friend.php");
        assertThat(m.matches(), is(true));
        assertThat(m.group(1), equalTo("tell-a-friend"));
        
        m = p.matcher("GET /affiliation-de-lassemblee-generale-des-etudiants-de-namur-a-lunion-des-etudiants-en-communaute-francaise-unecof/");
        assertThat(m.matches(), is(false));
        
        m = p.matcher("GET /wp-content/plugins.php");
        assertThat(m.matches(), is(false));
    }

    @Test
    public void testTHEME_REGEXP() {
        Pattern p = Pattern.compile(Main.THEME_REGEXP);
        Matcher m = p.matcher("POST /%3fpage_id=173/wp-content/themes/u-design/scripts/admin/uploadify/uploadify.php");
        assertThat(m.matches(), is(true));
        assertThat(m.group(1), equalTo("u-design"));
        
        m = p.matcher("GET /affiliation-de-lassemblee-generale-des-etudiants-de-namur-a-lunion-des-etudiants-en-communaute-francaise-unecof/");
        assertThat(m.matches(), is(false));
        
        m = p.matcher("GET /wp-content/themes.php");
        assertThat(m.matches(), is(false));
    }    

    @Test
    public void testADMIN_REGEXP() {
        Pattern p = Pattern.compile(Main.ADMIN_REGEXP);
        Matcher m = p.matcher("POST /wp-admin/plugins.php");
        assertThat(m.matches(), is(true));
        
        m = p.matcher("GET /politique/mandats/conseil-dadministration/");
        assertThat(m.matches(), is(false));
        
        m = p.matcher("POST /wp-content/themes/u-design/scripts/admin/uploadify/uploadify.php");
        assertThat(m.matches(), is(false));
    }

}