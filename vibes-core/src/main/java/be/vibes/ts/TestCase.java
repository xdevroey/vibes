package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestCase extends Execution{
    
    private final String id;

    public TestCase(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
}
