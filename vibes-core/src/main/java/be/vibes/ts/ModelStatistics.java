package be.vibes.ts;

import java.util.Map;

/**
 * Inteface defining methods to retrieve a model statistics.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface ModelStatistics {

    /**
     * Return a string representing the model statistics.
     * @return A string representing the model statistics.
     */
    public abstract String getStatistics();

    /**
     * Returns a Map containing model statistics (key, value).
     * @return A Map containing model statistics (key, value).
     */
    public abstract Map<String, Object> getStatisticsValues();

}
