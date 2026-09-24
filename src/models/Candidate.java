package models;

public class Candidate {
    public int id;
    public String name;
    public String party;
    public int votes;

    public Candidate(int id, String name, String party, int votes) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.votes = votes;
    }
}