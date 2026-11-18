package entities;

import java.math.BigDecimal;

public class PriceRange{
    private final BigDecimal min;
    private final BigDecimal max;

    public PriceRange(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    public BigDecimal getMin() { return min; }
    public BigDecimal getMax() { return max; }

    @Override
    public String toString() {
        return String.format("$%.2f - $%.2f", min, max);
    }
}
