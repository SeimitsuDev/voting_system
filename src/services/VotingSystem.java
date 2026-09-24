package services;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class VotingSystem {
    private Scanner scanner = new Scanner(System.in);

    public void addCandidate() {
        System.out.print("Enter Candidate ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Candidate Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Party: ");
        String party = scanner.nextLine();

        String query = "INSERT INTO candidates (id, name, party, votes) VALUES (?, ?, ?, 0)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, party);
            pstmt.executeUpdate();
            System.out.println("Candidate added successfully!\n");
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void addVoter() {
        System.out.print("Enter Voter ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Voter Name: ");
        String name = scanner.nextLine();

        String query = "INSERT INTO voters (id, name, has_voted) VALUES (?, ?, false)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Voter added successfully!\n");
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void removeCandidate() {
        System.out.print("Enter Candidate ID to remove: ");
        int id = scanner.nextInt();
        
        String query = "DELETE FROM candidates WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Candidate removed.\n");
            } else {
                System.out.println("Candidate not found.\n");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void removeVoter() {
        System.out.print("Enter Voter ID to remove: ");
        int id = scanner.nextInt();
        
        String query = "DELETE FROM voters WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Voter removed.\n");
            } else {
                System.out.println("Voter not found.\n");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void updateCandidate() {
        System.out.print("Enter Candidate ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter New Party: ");
        String party = scanner.nextLine();

        String query = "UPDATE candidates SET name = ?, party = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, party);
            pstmt.setInt(3, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Candidate updated.\n");
            } else {
                System.out.println("Candidate not found.\n");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void updateVoter() {
        System.out.print("Enter Voter ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter New Name: ");
        String name = scanner.nextLine();

        String query = "UPDATE voters SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Voter updated.\n");
            } else {
                System.out.println("Voter not found.\n");
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void castVote() {
        System.out.print("Enter Voter ID: ");
        int vId = scanner.nextInt();

        String checkVoterSql = "SELECT has_voted FROM voters WHERE id = ?";
        String checkCandidateSql = "SELECT id FROM candidates WHERE id = ?";
        String updateVoteSql = "UPDATE candidates SET votes = votes + 1 WHERE id = ?";
        String updateVoterSql = "UPDATE voters SET has_voted = true WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtVoter = conn.prepareStatement(checkVoterSql)) {
                pstmtVoter.setInt(1, vId);
                ResultSet rsVoter = pstmtVoter.executeQuery();
                
                if (!rsVoter.next()) {
                    System.out.println("Voter not found.\n");
                    return;
                }
                if (rsVoter.getBoolean("has_voted")) {
                    System.out.println("Voter has already cast a vote!\n");
                    return;
                }
            }

            System.out.print("Enter Candidate ID to vote for: ");
            int cId = scanner.nextInt();

            try (PreparedStatement pstmtCandidate = conn.prepareStatement(checkCandidateSql)) {
                pstmtCandidate.setInt(1, cId);
                ResultSet rsCandidate = pstmtCandidate.executeQuery();
                
                if (!rsCandidate.next()) {
                    System.out.println("Candidate not found. Vote not cast.\n");
                    return;
                }
            }

            try (PreparedStatement updateCandStmt = conn.prepareStatement(updateVoteSql);
                 PreparedStatement updateVotStmt = conn.prepareStatement(updateVoterSql)) {
                
                updateCandStmt.setInt(1, cId);
                updateCandStmt.executeUpdate();

                updateVotStmt.setInt(1, vId);
                updateVotStmt.executeUpdate();

                conn.commit();
                System.out.println("Vote cast successfully!\n");
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void resultDeclaration() {
        String maxVotesQuery = "SELECT MAX(votes) AS max_votes FROM candidates";
        String winnersQuery = "SELECT name, party, votes FROM candidates WHERE votes = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rsMax = stmt.executeQuery(maxVotesQuery)) {
             
            if (rsMax.next()) {
                int maxVotes = rsMax.getInt("max_votes");
                
                if (rsMax.wasNull()) {
                    System.out.println("No candidates in the system.\n");
                    return;
                }

                System.out.println("\n--- Election Results ---");
                try (PreparedStatement pstmtWinners = conn.prepareStatement(winnersQuery)) {
                    pstmtWinners.setInt(1, maxVotes);
                    ResultSet rsWinners = pstmtWinners.executeQuery();
                    
                    while (rsWinners.next()) {
                        System.out.println("Winner: " + rsWinners.getString("name") + 
                                           " (" + rsWinners.getString("party") + ") with " + 
                                           rsWinners.getInt("votes") + " votes!\n");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void viewAllCandidates() {
        String query = "SELECT id, name, party, votes FROM candidates";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            System.out.println("\n--- Candidate List ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Party: " + rs.getString("party") + 
                                   " | Votes: " + rs.getInt("votes"));
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }

    public void viewAllVoters() {
        String query = "SELECT id, name, has_voted FROM voters";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            System.out.println("\n--- Voter List ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Name: " + rs.getString("name") + 
                                   " | Has Voted: " + (rs.getBoolean("has_voted") ? "Yes" : "No"));
            }
            
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage() + "\n");
        }
    }
}