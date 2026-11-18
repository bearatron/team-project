package entities;

/**
 * A {@link Category#WAITING_TIME} rating whose score maps to an approximate wait.
 *
 * <p>Higher scores mean shorter waits. The labels are a business rule, so they live
 * here rather than in the UI, and every view shows the same wording.
 */
public class WaitingTimeRating extends Rating {

    private static final String[] LABELS = {
        "20+ min",
        "about 15 min",
        "about 10 min",
        "5-10 min",
        "under 5 min",
    };

    /**
     * Creates a waiting-time rating.
     *
     * @param score the score, from {@value Rating#MIN_SCORE} (longest wait) to
     *     {@value Rating#MAX_SCORE} (shortest wait)
     * @throws IllegalArgumentException if {@code score} is out of range
     */
    public WaitingTimeRating(int score) {
        super(Category.WAITING_TIME, score);
    }

    /**
     * Returns the approximate wait for a score.
     *
     * @param score the score, from {@value Rating#MIN_SCORE} to {@value Rating#MAX_SCORE}
     * @return the label, such as {@code "under 5 min"}
     * @throws IllegalArgumentException if {@code score} is out of range
     */
    public static String labelFor(int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException(
                    "score must be between " + MIN_SCORE + " and " + MAX_SCORE + ", was " + score);
        }
        return LABELS[score - MIN_SCORE];
    }

    /** Returns the approximate wait this rating describes, such as {@code "under 5 min"}. */
    public String getLabel() {
        return labelFor(getScore());
    }

    @Override
    public String toString() {
        return super.toString() + " (" + getLabel() + ")";
    }
}
