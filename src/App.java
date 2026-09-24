import services.VotingSystem;
import java.util.Scanner;

import config.DatabaseConfig;

public class App {
    public static void main(String[] args) {
        VotingSystem sys = new VotingSystem();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        }

        DatabaseConfig.initializeDatabase();

        while (choice != -1) {
            System.out.println("\n===============================");
            System.out.println("       VOTING SYSTEM MENU      ");
            System.out.println("===============================");
            System.out.println("1. Add Candidate");
            System.out.println("2. Add Voter");
            System.out.println("3. Remove Candidate");
            System.out.println("4. Remove Voter");
            System.out.println("5. Update Candidate Info");
            System.out.println("6. Update Voter Info");
            System.out.println("7. Cast Vote");
            System.out.println("8. Result Declaration");
            System.out.println("9. View All Candidates");
            System.out.println("0. View All Voters");
            System.out.println("-1. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); 
                System.out.println("Invalid choice. Please try again later.\n");
                continue;
            }

            switch (choice) {
                case 1: sys.addCandidate(); break;
                case 2: sys.addVoter(); break;
                case 3: sys.removeCandidate(); break;
                case 4: sys.removeVoter(); break;
                case 5: sys.updateCandidate(); break;
                case 6: sys.updateVoter(); break;
                case 7: sys.castVote(); break;
                case 8: sys.resultDeclaration(); break;
                case 9: sys.viewAllCandidates(); break;
                case 0: sys.viewAllVoters(); break;
                case -1: System.out.println("Exiting system. Goodbye! Take Care.\n"); break;
                default: System.out.println("Invalid choice. Please try again later.\n");
            }
        }
        
        scanner.close();
    }
}