package org.example.metrics;

public class Metrics {
    private long comparisons;
    private long finds;
    private long unions;
    private long heapOps;
    private double executionTimeMs;

    public void incComparisons() { comparisons++; }
    public void incFinds() { finds++; }
    public void incUnions() { unions++; }
    public void incHeapOps() { heapOps++; }

    public void incComparisons(long n) { comparisons += n; }
    public void incFinds(long n) { finds += n; }
    public void incUnions(long n) { unions += n; }
    public void incHeapOps(long n) { heapOps += n; }

    public void setExecutionTimeMs(double ms) { this.executionTimeMs = ms; }

    public long getOperationsCount() {
        return comparisons + finds + unions + heapOps;
    }

    public long getComparisons() { return comparisons; }
    public long getFinds() { return finds; }
    public long getUnions() { return unions; }
    public long getHeapOps() { return heapOps; }
    public double getExecutionTimeMs() { return executionTimeMs; }

    public void reset() {
        comparisons = finds = unions = heapOps = 0;
        executionTimeMs = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Metrics[comparisons=%d, finds=%d, unions=%d, heapOps=%d, time=%.3f ms]",
                comparisons, finds, unions, heapOps, executionTimeMs
        );
    }
}

