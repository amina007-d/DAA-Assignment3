package org.example.io.dto;

public class EdgeDTO {
    private String from;
    private String to;
    private int weight;

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public int getWeight() { return weight; }

    public void setFrom(String from) { this.from = from; }
    public void setTo(String to) { this.to = to; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String toString() {
        return String.format("%s-%s(%.2f)", from, to, weight);
    }
}
