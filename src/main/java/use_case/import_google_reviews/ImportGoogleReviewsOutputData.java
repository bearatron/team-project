package use_case.import_google_reviews;

/**
 * The output data for the Import Google Maps Reviews use case.
 */
public class ImportGoogleReviewsOutputData {
    private final String truckName;
    private final int importedCount;
    private final int skippedCount;

    /**
     * Creates the output data for an import.
     *
     * @param truckName the name of the truck the reviews were imported to
     * @param importedCount how many new reviews were imported
     * @param skippedCount how many reviews were skipped because they were
     *     already imported or were missing or invalid data
     */
    public ImportGoogleReviewsOutputData(String truckName, int importedCount,
                                         int skippedCount) {
        this.truckName = truckName;
        this.importedCount = importedCount;
        this.skippedCount = skippedCount;
    }

    public String getTruckName() {
        return truckName;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }
}
