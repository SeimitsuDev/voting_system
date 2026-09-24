package models;

public class Voter {
    public int id;
    public String name;
    public boolean hasVoted;

    public Voter(int id, String name, boolean hasVoted) {
        this.id = id;
        this.name = name;
        this.hasVoted = hasVoted;
    }
}