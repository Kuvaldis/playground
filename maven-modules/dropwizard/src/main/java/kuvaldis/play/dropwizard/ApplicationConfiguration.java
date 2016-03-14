package kuvaldis.play.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Dropwizard configuration
 */
public class ApplicationConfiguration extends Configuration {

    /**
     * Database support
     */
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @JsonProperty("cleanupOldRecordsJobEnabled")
    private boolean cleanupOldRecordsJobEnabled = false;

    @Valid
    @JsonProperty("cleanupOldRecordsJobThreads")
    private int cleanupOldRecordsJobThreads = 10;

    @Valid
    @JsonProperty("cleanupOldRecordsJobInterval")
    private int cleanupOldRecordsJobInterval = 5;

    @Valid
    @JsonProperty("cleanupUserViewLimitJobEnabled")
    private boolean cleanupUserViewLimitJobEnabled = false;

    @Valid
    @JsonProperty("cleanupUserViewLimitJobThreads")
    private int cleanupUserViewLimitJobThreads = 10;

    @Valid
    @JsonProperty("cleanupUserViewLimitJobInterval")
    private int cleanupUserViewLimitJobInterval = 20;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public boolean isCleanupOldRecordsJobEnabled() {
        return cleanupOldRecordsJobEnabled;
    }

    public int getCleanupOldRecordsJobThreads() {
        return cleanupOldRecordsJobThreads;
    }

    public int getCleanupOldRecordsJobInterval() {
        return cleanupOldRecordsJobInterval;
    }

    public boolean isCleanupUserViewLimitJobEnabled() {
        return cleanupUserViewLimitJobEnabled;
    }

    public int getCleanupUserViewLimitJobThreads() {
        return cleanupUserViewLimitJobThreads;
    }

    public int getCleanupUserViewLimitJobInterval() {
        return cleanupUserViewLimitJobInterval;
    }
}
