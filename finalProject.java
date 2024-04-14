import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

public class finalProject {

    private final String url = "jdbc:postgresql://localhost:5432/HealthAndFitnessClubManagementSystem";
    private final String user = "postgres";
    private final String password = "mikatoktamyssov";

    public int getUserId(String username) {
        int userId = -1;

        String SQL = "SELECT id FROM regular_member WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return userId;
    }

    public void createBill(int member_id, float payment_amount) {
        LocalDate paymentDate = LocalDate.now();
        java.sql.Date payment_date = java.sql.Date.valueOf(paymentDate);

        String sql = "INSERT INTO billing (member_id, payment_amount, payment_date) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, member_id);
            pstmt.setFloat(2, payment_amount);
            pstmt.setDate(3, payment_date);

            pstmt.executeUpdate();
            System.out.println("Billing created successfully");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void userRegistration(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String passwrd = scanner.nextLine();

        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();

        System.out.print("When you register you will be charged $60.00 as a membership fee? Do you agree to register? (yes/no) ");
        String agreement = scanner.nextLine();

        if (!(agreement.equalsIgnoreCase("yes"))) {
            return;
        }

        if (isUsernameAvailable(username)) {
            String sql = "INSERT INTO regular_member (username, password, date_of_birth) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, passwrd);
                pstmt.setDate(3, java.sql.Date.valueOf(dateOfBirth));

                pstmt.executeUpdate();
                int memberId = getUserId(username);
                System.out.println("User registered successfully with ID: " + memberId);
                createBill(memberId, 100);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Username already exists, choose a different username.");
        }
    }

    public boolean isUsernameAvailable(String username) {
        String sql = "SELECT COUNT(*) FROM regular_member WHERE username = ?";
        boolean available = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                available = count == 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return available;
    }

    public boolean validateMember(String username, String passwrd) {
        String sql = "SELECT COUNT(*) FROM regular_member WHERE username = ? AND password = ?";
        boolean isValid = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwrd);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                isValid = count > 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return isValid;
    }

    public void memberLogin(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (validateMember(username, password)) {
            System.out.println("Login successful!");
            memberFunctions(scanner, username);
        } else {
            System.out.println("Invalid username or password.");
        }
    }
    public void handleTrainerFunctions(Scanner scanner) {
        int trainerChoice;

        do {
            System.out.println("Trainer Functions");
            System.out.println("1. Trainer Register");
            System.out.println("2. Trainer Login");

            System.out.print("Enter your choice: ");
            trainerChoice = scanner.nextInt();
            scanner.nextLine();

            switch (trainerChoice) {
                case 1:
                    trainerRegistration(scanner);
                    break;
                case 2:
                    trainerLogin(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (trainerChoice != 2);
    }

    public void trainerRegistration(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String passwrd = scanner.nextLine();

        if (isTrainerUsernameAvailable(username)) {
            String sql = "INSERT INTO trainer (username, password) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, passwrd);

                pstmt.executeUpdate();
                System.out.println("Trainer registered successfully");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Username already exists, choose a different username.");
        }
    }

    public boolean isTrainerUsernameAvailable(String username) {
        String sql = "SELECT COUNT(*) FROM trainer WHERE username = ?";
        boolean available = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                available = count == 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return available;
    }

    public void trainerLogin(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (validateTrainer(username, password)) {
            System.out.println("Login successful!");
            trainerFunctions(scanner, username);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public boolean validateTrainer(String username, String passwrd) {
        String sql = "SELECT COUNT(*) FROM trainer WHERE username = ? AND password = ?";
        boolean isValid = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwrd);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                isValid = count > 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return isValid;
    }

    public void trainerFunctions(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Trainer Functions");
            System.out.println("1. Schedule Management");
            System.out.println("2. Search Member Profiles");
            System.out.println("3. Logout\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    trainerScheduleManagement(scanner, username);
                    break;
                case 2:
                    searchMemberProfiles(scanner, username);
                    break;
                case 3:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 3);
    }

    public void trainerScheduleManagement(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Trainer Schedule Management");
            System.out.println("1. View all sessions you train");
            System.out.println("2. Show all available rooms");
            System.out.println("3. Create a training session");
            System.out.println("4. Cancel a training session");
            System.out.println("5. Show all users registered in a session");
            System.out.println("6. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewTrainerSessions(username);
                    break;
                case 2:
                    showAvailableRooms();
                    break;
                case 3:
                    createTrainingSession(username, scanner);
                    break;
                case 4:
                    cancelTrainingSession(scanner);
                    break;
                case 5:
                    showRegisteredMembersInSession(scanner);
                    break;
                case 6:
                    System.out.println("Returning to Trainer Functions.");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 6);
    }

    private void viewTrainerSessions(String username) {
        System.out.println("View All Sessions You Train");

        String sql = "SELECT * FROM training_session WHERE trainer_id = (SELECT id FROM trainer WHERE username = ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int sessionId = rs.getInt("id");
                Timestamp startDateTime = rs.getTimestamp("start_date_time");
                Timestamp endDateTime = rs.getTimestamp("end_date_time");
                int maxCapacity = rs.getInt("maximum_capacity");
                int numRegistered = rs.getInt("num_users_registered");

                System.out.println("Session ID: " + sessionId);
                System.out.println("Start Date/Time: " + startDateTime);
                System.out.println("End Date/Time: " + endDateTime);
                System.out.println("Capacity: " + maxCapacity);
                System.out.println("Registered Users: " + numRegistered);
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showAvailableRooms() {
        System.out.println("Show All Available Rooms");

        String sql = "SELECT * FROM room_booking WHERE trainer_id IS NULL";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int roomId = rs.getInt("id");
                String roomNumber = rs.getString("room_number");
                Date startDate = rs.getDate("start_date_booking");
                Date endDate = rs.getDate("end_date_booking");

                System.out.println("Room ID: " + roomId);
                System.out.println("Room Number: " + roomNumber);
                System.out.println("Start Date: " + startDate);
                System.out.println("End Date: " + endDate);

                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createTrainingSession(String trainerUsername, Scanner scanner) {
        System.out.println("Create a Training Session");
        showAvailableRooms();

        System.out.print("Enter the Room ID to book: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the Start Date/Time (YYYY-MM-DD HH:mm:ss): ");
        String startDateTimeStr = scanner.nextLine();
        Timestamp startDateTime = Timestamp.valueOf(startDateTimeStr);

        System.out.print("Enter the End Date/Time (YYYY-MM-DD HH:mm:ss): ");
        String endDateTimeStr = scanner.nextLine();
        Timestamp endDateTime = Timestamp.valueOf(endDateTimeStr);

        System.out.print("Enter the Maximum Capacity: ");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine();

        String updateRoomBookingSql = "UPDATE room_booking SET trainer_id = (SELECT id FROM trainer WHERE username = ?) WHERE id = ?";
        String insertTrainingSessionSql = "INSERT INTO training_session (trainer_id, room_booking_id, start_date_time, end_date_time, maximum_capacity, num_users_registered) " +
                "VALUES ((SELECT id FROM trainer WHERE username = ?), ?, ?, ?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement updateStmt = conn.prepareStatement(updateRoomBookingSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertTrainingSessionSql)) {

            updateStmt.setString(1, trainerUsername);
            updateStmt.setInt(2, roomId);
            int updateResult = updateStmt.executeUpdate();

            if (updateResult > 0) {
                insertStmt.setString(1, trainerUsername);
                insertStmt.setInt(2, roomId);
                insertStmt.setTimestamp(3, startDateTime);
                insertStmt.setTimestamp(4, endDateTime);
                insertStmt.setInt(5, maxCapacity);

                int insertResult = insertStmt.executeUpdate();
                if (insertResult > 0) {
                    System.out.println("Training session created successfully");
                } else {
                    System.out.println("Error creating training session.");
                }
            } else {
                System.out.println("Failed to assign trainer to room");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void cancelTrainingSession(Scanner scanner) {
        System.out.println("Cancel a Training Session");

        System.out.print("Enter the Session ID to cancel: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        String deleteSql = "DELETE FROM training_session WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, sessionId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Training session with ID " + sessionId + " cancelled successfully");
            } else {
                System.out.println("Training session not found with ID " + sessionId);
            }

        } catch (SQLException ex) {
            System.out.println("Error cancelling training session: " + ex.getMessage());
        }
    }

    private void showRegisteredMembersInSession(Scanner scanner) {
        System.out.println("Show All Users Registered in a Session");

        System.out.println("Enter the Session ID: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT rm.username, rm.first_name, rm.last_name, rm.email " +
                "FROM training_session_for_members tsm " +
                "JOIN regular_member rm ON tsm.member_id = rm.id " +
                "WHERE tsm.session_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String memberUsername = rs.getString("username");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");

                System.out.println("Username: " + memberUsername);
                System.out.println("Name: " + firstName + " " + lastName);
                System.out.println("Email: " + email);
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void searchMemberProfiles(Scanner scanner, String trainerUsername) {
        System.out.println("Search Member Profiles");
        System.out.println("Enter the username of the member you want to search (or 'back' to return):");

        String searchUsername = scanner.nextLine();

        if (searchUsername.equalsIgnoreCase("back")) {
            System.out.println("Returning to Trainer Functions.");
            return;
        }

        String sql = "SELECT * FROM regular_member WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int memberId = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date dateOfBirth = rs.getDate("date_of_birth");

                System.out.println("\nMember Profile:");
                System.out.println("Username: " + searchUsername);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Date of Birth: " + dateOfBirth);
            } else {
                System.out.println("Member with username '" + searchUsername + "' not found.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void handleAdminFunctions(Scanner scanner) {
        int adminChoice;

        do {
            System.out.println("Administrator Functions");
            System.out.println("1. Administrator Register");
            System.out.println("2. Administrator Login");

            System.out.print("Enter your choice: ");
            adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    adminRegistration(scanner);
                    break;
                case 2:
                    adminLogin(scanner);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (adminChoice != 2);
    }

    public void adminRegistration(Scanner scanner) {
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String passwrd = scanner.nextLine();

        if (isAdminUsernameAvailable(username)) {
            String sql = "INSERT INTO administrator (username, password) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, passwrd);

                pstmt.executeUpdate();
                System.out.println("Administrator registered successfully!");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
    }

    public boolean isAdminUsernameAvailable(String username) {
        String sql = "SELECT COUNT(*) FROM administrator WHERE username = ?";
        boolean available = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                available = count == 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return available;
    }

    public void adminLogin(Scanner scanner) {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (validateAdmin(username, password)) {
            System.out.println("Login successful!");
            adminFunctions(scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public boolean validateAdmin(String username, String passwrd) {
        String sql = "SELECT COUNT(*) FROM administrator WHERE username = ? AND password = ?";
        boolean isValid = false;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwrd);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                isValid = count > 0;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return isValid;
    }

    public void adminFunctions(Scanner scanner) {
        int choice;

        do {
            System.out.println("Administrator Functions");
            System.out.println("1. Room Booking Management");
            System.out.println("2. Equipment Maintenance Logs");
            System.out.println("3. Class Schedule Updating");
            System.out.println("4. Billing Management");
            System.out.println("5. Logout\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    roomBookingManagement(scanner);
                    break;
                case 2:
                    equipmentMaintenanceLogs(scanner);
                    break;
                case 3:
                    classScheduleUpdating(scanner);
                    break;
                case 4:
                    billingManagement(scanner);
                    break;
                case 5:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    public void dashboardDisplay(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("\nDashboard Display - " + username);
            System.out.println("1. View Health Goals");
            System.out.println("2. View Fitness Goals");
            System.out.println("3. View Achievements");
            System.out.println("4. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewHealthGoals(scanner, username);
                    break;
                case 2:
                    viewFitnessGoals(scanner, username);
                    break;
                case 3:
                    viewAchievements(scanner, username);
                    break;
                case 4:
                    System.out.println("Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    public void viewAchievements(Scanner scanner, String username) {
        System.out.println("View Achievements");

        int memberId = getUserId(username);
        String sql = "SELECT id, starting_date, completion_date, goal FROM health_and_fitness_achievements WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Starting Date: " + rs.getDate("starting_date"));
                System.out.println("Completion Date: " + rs.getDate("completion_date"));
                System.out.println("Goal: " + rs.getString("goal"));
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter '1' to go back");
        scanner.nextLine();
    }

    public void memberFunctions(Scanner scanner, String username) {
        int choice;
        do {
            System.out.println("MEMBER FUNCTIONS");
            System.out.println("1. Profile Management");
            System.out.println("2. Dashboard Display");
            System.out.println("3. Schedule Management");
            System.out.println("4. Logout\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    profileManagement(scanner, username);
                    break;
                case 2:
                    dashboardDisplay(scanner, username);
                    break;
                case 3:
                    scheduleManagement(scanner, username);
                    break;
                case 4:
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    public void scheduleManagement(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("SCHEDULE MANAGEMENT");
            System.out.println("1. View All Open Sessions");
            System.out.println("2. View Sessions You Are Registered In");
            System.out.println("3. Join a Session");
            System.out.println("4. Cancel a Session");
            System.out.println("5. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllOpenSessions();
                    break;
                case 2:
                    viewRegisteredSessions(username);
                    break;
                case 3:
                    joinSession(scanner, username);
                    break;
                case 4:
                    cancelSession(scanner, username);
                    break;
                case 5:
                    System.out.println("Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 5);
    }

    public void viewAllOpenSessions() {
        System.out.println("View All Open Sessions");

        String sql = "SELECT ts.id, rb.room_number, ts.start_date_time, ts.end_date_time, ts.maximum_capacity, ts.num_users_registered " +
                "FROM training_session ts " +
                "JOIN room_booking rb ON ts.room_booking_id = rb.id " +
                "WHERE ts.num_users_registered < ts.maximum_capacity";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int sessionId = rs.getInt("id");
                String roomNumber = rs.getString("room_number");
                Timestamp startDateTime = rs.getTimestamp("start_date_time");
                Timestamp endDateTime = rs.getTimestamp("end_date_time");
                int maxCapacity = rs.getInt("maximum_capacity");
                int numRegistered = rs.getInt("num_users_registered");

                System.out.println("Session ID: " + sessionId);
                System.out.println("Room Number: " + roomNumber);
                System.out.println("Start Date/Time: " + startDateTime);
                System.out.println("End Date/Time: " + endDateTime);
                System.out.println("Capacity: " + maxCapacity);
                System.out.println("Registered Users: " + numRegistered);
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Press any key to go back");
        new Scanner(System.in).nextLine();
    }

    public void viewRegisteredSessions(String username) {
        System.out.println("View Registered Sessions");

        int memberId = getUserId(username);
        String sql = "SELECT ts.id, rb.room_number, ts.start_date_time, ts.end_date_time " +
                "FROM training_session ts " +
                "JOIN training_session_for_members tsm ON ts.id = tsm.session_id " +
                "JOIN room_booking rb ON ts.room_booking_id = rb.id " +
                "WHERE tsm.member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int sessionId = rs.getInt("id");
                String roomNumber = rs.getString("room_number");
                Timestamp startDateTime = rs.getTimestamp("start_date_time");
                Timestamp endDateTime = rs.getTimestamp("end_date_time");

                System.out.println("Session ID: " + sessionId);
                System.out.println("Room Number: " + roomNumber);
                System.out.println("Start Date/Time: " + startDateTime);
                System.out.println("End Date/Time: " + endDateTime);
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Press any key to go back");
        new Scanner(System.in).nextLine();
    }

    public void joinSession(Scanner scanner, String username) {
        System.out.println("Join a Session");

        System.out.print("Enter the Session ID to join: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        String capacitySql = "SELECT maximum_capacity, num_users_registered FROM training_session WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(capacitySql)) {

            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int maxCapacity = rs.getInt("maximum_capacity");
                int numRegistered = rs.getInt("num_users_registered");

                if (numRegistered < maxCapacity) {
                    int memberId = getUserId(username);
                    String registerSql = "INSERT INTO training_session_for_members (session_id, member_id) VALUES (?, ?)";

                    try (PreparedStatement registerStmt = conn.prepareStatement(registerSql)) {
                        registerStmt.setInt(1, sessionId);
                        registerStmt.setInt(2, memberId);
                        registerStmt.executeUpdate();

                        String updateSql = "UPDATE training_session SET num_users_registered = num_users_registered + 1 WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, sessionId);
                            updateStmt.executeUpdate();
                        }

                        System.out.println("Successfully joined the session!");
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    System.out.println("Session is already at maximum capacity. Unable to join.");
                }
            } else {
                System.out.println("Session not found.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Press any key to go back");
        new Scanner(System.in).nextLine();
    }

    public void cancelSession(Scanner scanner, String username) {
        System.out.println("Cancel a Session");

        viewRegisteredSessions(username);

        System.out.print("Enter the Session ID to cancel registration: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        int memberId = getUserId(username);
        String deleteSql = "DELETE FROM training_session_for_members WHERE session_id = ? AND member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, memberId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                String updateSql = "UPDATE training_session SET num_users_registered = num_users_registered - 1 WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, sessionId);
                    updateStmt.executeUpdate();
                }

                System.out.println("Successfully cancelled registration for the session.");
            } else {
                System.out.println("You are not registered for the specified session.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Press any key to go back");
        new Scanner(System.in).nextLine();
    }

    public void handleMemberFunctions(Scanner scanner) {
        System.out.println("\nMember Functions Selected:");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Enter your choice: ");
        int memberChoice = scanner.nextInt();
        scanner.nextLine();

        switch (memberChoice) {
            case 1:
                userRegistration(scanner);
                break;
            case 2:
                memberLogin(scanner);
                break;
            default:
                System.out.println("Invalid choice for member functions.");
                break;
        }
    }

    public void profileManagement(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Profile Management");
            System.out.println("1. View Profile");
            System.out.println("2. Update Personal Information");
            System.out.println("3. Update Health Stats");
            System.out.println("4. Update Fitness Stats");
            System.out.println("5. Update Health Goals");
            System.out.println("6. Update Fitness Goals");
            System.out.println("7. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewProfile(scanner, username);
                    break;
                case 2:
                    updatePersonalInfo(scanner, username);
                    break;
                case 3:
                    updateHealthMetrics(scanner, username);
                    break;
                case 4:
                    updateFitnessMetrics(scanner, username);
                    break;
                case 5:
                    updateHealthGoals(scanner, username);
                    break;
                case 6:
                    updateFitnessGoals(scanner, username);
                    break;
                case 7:
                    System.out.println("Returning to Member Functions.");
                    memberFunctions(scanner, username);
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 7);
    }

    public void viewProfile(Scanner scanner, String username) {
        String sql = "SELECT id, username, first_name, last_name, email, date_of_birth FROM regular_member WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("\nUser Profile:");
                System.out.println("User ID: " + rs.getInt("id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("First Name: " + rs.getString("first_name"));
                System.out.println("Last Name: " + rs.getString("last_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Date of Birth: " + rs.getDate("date_of_birth"));
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nEnter '1' to go back.");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Returning to Profile Management.");
            return;
        } else {
            System.out.println("Invalid choice. Returning to Profile Management.");
        }
    }

    public void updatePersonalInfo(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Update Personal Information");
            System.out.println("1. Edit First Name");
            System.out.println("2. Edit Last Name");
            System.out.println("3. Edit Email");
            System.out.println("4. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    editFirstName(scanner, username);
                    break;
                case 2:
                    editLastName(scanner, username);
                    break;
                case 3:
                    editEmail(scanner, username);
                    break;
                case 4:
                    System.out.println("Returning to Profile Management.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }
    public void editFirstName(Scanner scanner, String username) {
        System.out.print("Enter your new First Name: ");
        String newFirstName = scanner.nextLine();

        String sql = "UPDATE regular_member SET first_name = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newFirstName);
            pstmt.setString(2, username);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("First Name updated successfully.");
            } else {
                System.out.println("Failed to update First Name.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void editLastName(Scanner scanner, String username) {
        System.out.print("Enter your new Last Name: ");
        String newLastName = scanner.nextLine();

        String sql = "UPDATE regular_member SET last_name = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newLastName);
            pstmt.setString(2, username);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Last Name updated successfully.");
            } else {
                System.out.println("Failed to update Last Name.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void editEmail(Scanner scanner, String username) {
        System.out.print("Enter your new Email: ");
        String newEmail = scanner.nextLine();

        String sql = "UPDATE regular_member SET email = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newEmail);
            pstmt.setString(2, username);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Email updated successfully.");
            } else {
                System.out.println("Failed to update Email.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void updateHealthMetrics(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Update Health Metrics");
            System.out.println("1. View Health Stats");
            System.out.println("2. Add Health Stats");
            System.out.println("3. Delete Health Stats");
            System.out.println("4. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewHealthStats(scanner, username);
                    break;
                case 2:
                    addHealthStats(scanner, username);
                    break;
                case 3:
                    deleteHealthStats(scanner);
                    break;
                case 4:
                    System.out.println("Returning to Profile Management.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }

    public void updateFitnessMetrics(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Update Fitness Metrics");
            System.out.println("1. View Fitness Stats");
            System.out.println("2. Add Fitness Stats");
            System.out.println("3. Delete Fitness Stats");
            System.out.println("4. Back");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewFitnessStats(scanner, username);
                    break;
                case 2:
                    addFitnessStats(scanner, username);
                    break;
                case 3:
                    deleteFitnessStats(scanner);
                    break;
                case 4:
                    System.out.println("Returning to Profile Management.");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);
    }
    public void viewFitnessStats(Scanner scanner, String username) {
        System.out.println("View Fitness Stats");

        int memberId = getUserId(username);
        String sql = "SELECT id, date, bench_press, squat, deadlift, barbell_row, military_press FROM fitness_stats WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Date: " + rs.getDate("date"));
                System.out.println("Bench Press: " + rs.getFloat("bench_press"));
                System.out.println("Squat: " + rs.getFloat("squat"));
                System.out.println("Deadlift: " + rs.getFloat("deadlift"));
                System.out.println("Barbell Row: " + rs.getFloat("barbell_row"));
                System.out.println("Military Press: " + rs.getFloat("military_press"));
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter '1' to go back.");
        scanner.nextLine();
    }

    public void addFitnessStats(Scanner scanner, String username) {
        System.out.println("Add Fitness Stats");

        int memberId = getUserId(username);

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Enter Bench Press: ");
        float benchPress = scanner.nextFloat();

        System.out.print("Enter Squat: ");
        float squat = scanner.nextFloat();

        System.out.print("Enter Deadlift: ");
        float deadlift = scanner.nextFloat();

        System.out.print("Enter Barbell Row: ");
        float barbellRow = scanner.nextFloat();

        System.out.print("Enter Military Press: ");
        float militaryPress = scanner.nextFloat();

        String sql = "INSERT INTO fitness_stats (member_id, date, bench_press, squat, deadlift, barbell_row, military_press) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setFloat(3, benchPress);
            pstmt.setFloat(4, squat);
            pstmt.setFloat(5, deadlift);
            pstmt.setFloat(6, barbellRow);
            pstmt.setFloat(7, militaryPress);

            pstmt.executeUpdate();
            System.out.println("Fitness stats added successfully");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void deleteFitnessStats(Scanner scanner) {
        System.out.println("Delete Fitness Stats");

        System.out.print("Enter the ID of the fitness stat to delete: ");
        int statId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM fitness_stats WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, statId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Fitness stat with ID " + statId + " deleted successfully!");
            } else {
                System.out.println("No fitness stat found with ID " + statId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void viewHealthStats(Scanner scanner, String username) {
        System.out.println("Displaying Health Statistics:");

        int memberId = getUserId(username);
        String sql = "SELECT * FROM health_stats WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("\nHealth Stat ID: " + rs.getInt("id"));
                System.out.println("Date: " + rs.getDate("date"));
                System.out.println("Height: " + rs.getFloat("height") + " cm");
                System.out.println("Weight: " + rs.getFloat("weight") + " lbs");
                System.out.println("Body Fat Percentage: " + rs.getFloat("body_fat_percentage") + "%");
                System.out.println("Muscle Mass Percentage: " + rs.getFloat("muscle_mass_percentage") + "%");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nEnter '1' to go back.");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Returning to Profile Management.");
        }
    }

    public void addHealthStats(Scanner scanner, String username) {
        System.out.println("Adding new Health Statistics:");

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Enter Height (in cm): ");
        float height = scanner.nextFloat();

        System.out.print("Enter Weight (in lbs): ");
        float weight = scanner.nextFloat();

        System.out.print("Enter Body Fat Percentage: ");
        float bodyFatPercentage = scanner.nextFloat();

        System.out.print("Enter Muscle Mass Percentage: ");
        float muscleMassPercentage = scanner.nextFloat();

        int memberId = getUserId(username);
        String sql = "INSERT INTO health_stats (member_id, date, height, weight, body_fat_percentage, muscle_mass_percentage) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setFloat(3, height);
            pstmt.setFloat(4, weight);
            pstmt.setFloat(5, bodyFatPercentage);
            pstmt.setFloat(6, muscleMassPercentage);

            pstmt.executeUpdate();
            System.out.println("Health Statistics added successfully!");

        } catch (SQLException ex) {
            System.out.println("Error adding health stats: " + ex.getMessage());
        }

        System.out.println("\nEnter any key to go back.");
        scanner.nextLine();
    }

    public void deleteHealthStats(Scanner scanner) {
        System.out.print("Enter Health Stat ID to delete: ");
        int healthStatId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM health_stats WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, healthStatId);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Health Stat with ID " + healthStatId + " deleted successfully.");
            } else {
                System.out.println("No health stat found with ID " + healthStatId + ".");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nEnter any key to go back.");
        scanner.nextLine();
    }

    public void updateHealthGoals(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("\nUpdate Health Goals\n");
            System.out.println("1. View Health Goals");
            System.out.println("2. Add Health Goals");
            System.out.println("3. Delete Health Goals");
            System.out.println("4. Mark Health Goal Complete");
            System.out.println("5. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewHealthGoals(scanner, username);
                    break;
                case 2:
                    addHealthGoal(scanner, username);
                    break;
                case 3:
                    deleteHealthGoal(scanner);
                    break;
                case 4:
                    markHealthGoalComplete(scanner, username);
                    break;
                case 5:
                    System.out.println("Returning to Profile Management.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    public void viewHealthGoals(Scanner scanner, String username) {
        System.out.println("View Health Goals");

        int memberId = getUserId(username);
        String sql = "SELECT id, date, goal FROM health_goals WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Date: " + rs.getDate("date"));
                System.out.println("Goal: " + rs.getString("goal"));
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter '1' to go back.");
        scanner.nextLine();
    }

    public void addHealthGoal(Scanner scanner, String username) {
        System.out.println("Add Health Goal");

        int memberId = getUserId(username);

        System.out.print("Enter goal description: ");
        String goalDescription = scanner.nextLine();

        LocalDate currentDate = LocalDate.now();
        String sql = "INSERT INTO health_goals (member_id, date, goal) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            pstmt.setDate(2, java.sql.Date.valueOf(currentDate));
            pstmt.setString(3, goalDescription);

            pstmt.executeUpdate();
            System.out.println("Health goal added successfully!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void deleteHealthGoal(Scanner scanner) {
        System.out.println("Delete Health Goal");

        System.out.print("Enter the ID of the health goal to delete: ");
        int goalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM health_goals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, goalId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Health goal with ID " + goalId + " deleted successfully!");
            } else {
                System.out.println("No health goal found with ID " + goalId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }
    public void markHealthGoalComplete(Scanner scanner, String username) {
        System.out.println("Mark Health Goal Complete");

        System.out.print("Enter the ID of the health goal to mark as complete: ");
        int goalId = scanner.nextInt();
        scanner.nextLine();

        String selectSql = "SELECT goal FROM health_goals WHERE id = ?";
        String insertSql = "INSERT INTO health_and_fitness_achievements (member_id, starting_date, completion_date, goal) " +
                "VALUES (?, ?, ?, ?)";
        String deleteSql = "DELETE FROM health_goals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            selectStmt.setInt(1, goalId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String goalDescription = rs.getString("goal");

                // Insert into achievements
                int memberId = getUserId(username);
                LocalDate currentDate = LocalDate.now();
                insertStmt.setInt(1, memberId);
                insertStmt.setDate(2, java.sql.Date.valueOf(currentDate));
                insertStmt.setDate(3, java.sql.Date.valueOf(currentDate));
                insertStmt.setString(4, goalDescription);

                insertStmt.executeUpdate();

                // Delete from goals
                deleteStmt.setInt(1, goalId);
                deleteStmt.executeUpdate();

                System.out.println("Health goal marked as complete and moved to achievements!");
            } else {
                System.out.println("No health goal found with ID " + goalId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void updateFitnessGoals(Scanner scanner, String username) {
        int choice;

        do {
            System.out.println("Update Fitness Goals");
            System.out.println("1. View Fitness Goals");
            System.out.println("2. Add Fitness Goals");
            System.out.println("3. Delete Fitness Goals");
            System.out.println("4. Mark Fitness Goal Complete");
            System.out.println("5. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewFitnessGoals(scanner, username);
                    break;
                case 2:
                    addFitnessGoal(scanner, username);
                    break;
                case 3:
                    deleteFitnessGoal(scanner);
                    break;
                case 4:
                    markFitnessGoalComplete(scanner, username);
                    break;
                case 5:
                    System.out.println("Returning to Profile Management.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    public void viewFitnessGoals(Scanner scanner, String username) {
        System.out.println("View Fitness Goals");

        int memberId = getUserId(username);
        String sql = "SELECT id, date, goal FROM fitness_goals WHERE member_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Date: " + rs.getDate("date"));
                System.out.println("Goal: " + rs.getString("goal"));
                System.out.println();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter '1' to go back.");
        scanner.nextLine();
    }

    public void addFitnessGoal(Scanner scanner, String username) {
        System.out.println("\nAdd Fitness Goal\n");

        System.out.print("Enter goal description: ");
        String goalDescription = scanner.nextLine();

        LocalDate currentDate = LocalDate.now();
        String sql = "INSERT INTO fitness_goals (member_id, date, goal) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int memberId = getUserId(username);
            pstmt.setInt(1, memberId);
            pstmt.setDate(2, java.sql.Date.valueOf(currentDate));
            pstmt.setString(3, goalDescription);

            pstmt.executeUpdate();
            System.out.println("Fitness goal added successfully!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void deleteFitnessGoal(Scanner scanner) {
        System.out.println("Delete Fitness Goal");

        System.out.print("Enter the ID of the fitness goal to delete: ");
        int goalId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM fitness_goals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, goalId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Fitness goal with ID " + goalId + " deleted successfully!");
            } else {
                System.out.println("No fitness goal found with ID " + goalId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }

    public void markFitnessGoalComplete(Scanner scanner, String username) {
        System.out.println("Mark Fitness Goal Complete");

        System.out.print("Enter the ID of the fitness goal to mark as complete: ");
        int goalId = scanner.nextInt();
        scanner.nextLine();

        String selectSql = "SELECT goal FROM fitness_goals WHERE id = ?";
        String insertSql = "INSERT INTO health_and_fitness_achievements (member_id, starting_date, completion_date, goal) " +
                "VALUES (?, ?, ?, ?)";
        String deleteSql = "DELETE FROM fitness_goals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            selectStmt.setInt(1, goalId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String goalDescription = rs.getString("goal");

                int memberId = getUserId(username);
                LocalDate currentDate = LocalDate.now();
                insertStmt.setInt(1, memberId);
                insertStmt.setDate(2, java.sql.Date.valueOf(currentDate));
                insertStmt.setDate(3, java.sql.Date.valueOf(currentDate));
                insertStmt.setString(4, goalDescription);

                insertStmt.executeUpdate();

                deleteStmt.setInt(1, goalId);
                deleteStmt.executeUpdate();

                System.out.println("Fitness goal marked as complete and moved to achievements!");
            } else {
                System.out.println("No fitness goal found with ID " + goalId);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Enter any key to go back.");
        scanner.nextLine();
    }


    public void equipmentMaintenanceLogs(Scanner scanner) {
        int choice;

        do {
            System.out.println("Equipment Maintenance Logs");
            System.out.println("1. View Maintenance Logs");
            System.out.println("2. Add Maintenance Log");
            System.out.println("3. Delete Maintenance Log");
            System.out.println("4. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMaintenanceLogs();
                    break;
                case 2:
                    addMaintenanceLog(scanner);
                    break;
                case 3:
                    deleteMaintenanceLog(scanner);
                    break;
                case 4:
                    System.out.println("Returning to Administrator Functions.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 4);
    }

    public void viewMaintenanceLogs() {
        System.out.println("\nViewing Maintenance Logs...");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM equipment_maintenance";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("date_last_maintained");
                String description = rs.getString("description");

                System.out.println("Maintenance Log ID: " + id);
                System.out.println("Maintenance Date: " + date);
                System.out.println("Description: " + description);
                System.out.println();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addMaintenanceLog(Scanner scanner) {
        System.out.println("\nAdding Maintenance Log...");

        System.out.print("Enter maintenance date (YYYY-MM-DD): ");
        String maintenanceDateStr = scanner.nextLine();

        System.out.print("Enter maintenance description: ");
        String description = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO equipment_maintenance (date_last_maintained, description) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setDate(1, Date.valueOf(maintenanceDateStr));
            pstmt.setString(2, description);

            pstmt.executeUpdate();
            System.out.println("Maintenance log added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteMaintenanceLog(Scanner scanner) {
        System.out.println("\nDeleting Maintenance Log...");

        System.out.print("Enter maintenance log ID to delete: ");
        int logId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM equipment_maintenance WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, logId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Maintenance log deleted successfully!");
            } else {
                System.out.println("Maintenance log not found with ID: " + logId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void billingManagement(Scanner scanner) {
        int choice;

        do {
            System.out.println("Billing Management");
            System.out.println("1. View All Bills");
            System.out.println("2. Edit Bill Amount");
            System.out.println("3. Delete a Bill");
            System.out.println("4. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllBills();
                    break;
                case 2:
                    editBillAmount(scanner);
                    break;
                case 3:
                    deleteBill(scanner);
                    break;
                case 4:
                    System.out.println("Returning to Administrator Functions.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 4);
    }

    public void viewAllBills() {
        System.out.println("\nViewing All Bills...");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM billing";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int memberId = rs.getInt("member_id");
                double paymentAmount = rs.getDouble("payment_amount");
                Date paymentDate = rs.getDate("payment_date");

                System.out.println("Bill ID: " + id);
                System.out.println("Member ID: " + memberId);
                System.out.println("Payment Amount: " + paymentAmount);
                System.out.println("Payment Date: " + paymentDate);
                System.out.println();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void editBillAmount(Scanner scanner) {
        System.out.println("\nEditing Bill Amount...");

        System.out.print("Enter bill ID to edit: ");
        int billId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new payment amount: ");
        double newPaymentAmount = scanner.nextDouble();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE billing SET payment_amount = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, newPaymentAmount);
            pstmt.setInt(2, billId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bill amount updated successfully!");
            } else {
                System.out.println("Bill not found with ID: " + billId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteBill(Scanner scanner) {
        System.out.println("\nDeleting a Bill...");

        System.out.print("Enter bill ID to delete: ");
        int billId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM billing WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, billId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bill deleted successfully!");
            } else {
                System.out.println("Bill not found with ID: " + billId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void classScheduleUpdating(Scanner scanner) {
        int choice;

        do {
            System.out.println("\nClass Schedule Updating\n");
            System.out.println("1. View All Sessions");
            System.out.println("2. Change Session Time");
            System.out.println("3. Change Session Capacity");
            System.out.println("4. Cancel Session");
            System.out.println("5. Back\n");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllSessions();
                    break;
                case 2:
                    changeSessionTime(scanner);
                    break;
                case 3:
                    changeSessionCapacity(scanner);
                    break;
                case 4:
                    cancelSession(scanner);
                    break;
                case 5:
                    System.out.println("Returning to Administrator Functions.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 5);
    }

    public void viewAllSessions() {
        System.out.println("\nViewing All Sessions...");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM training_session";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int sessionId = rs.getInt("id");
                int trainerId = rs.getInt("trainer_id");
                int roomBookingId = rs.getInt("room_booking_id");
                Timestamp startTime = rs.getTimestamp("start_date_time");
                Timestamp endTime = rs.getTimestamp("end_date_time");
                int maxCapacity = rs.getInt("maximum_capacity");
                int numUsersRegistered = rs.getInt("num_users_registered");

                System.out.println("Session ID: " + sessionId);
                System.out.println("Trainer ID: " + trainerId);
                System.out.println("Room Booking ID: " + roomBookingId);
                System.out.println("Start Time: " + startTime);
                System.out.println("End Time: " + endTime);
                System.out.println("Maximum Capacity: " + maxCapacity);
                System.out.println("Users Registered: " + numUsersRegistered);
                System.out.println();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void changeSessionTime(Scanner scanner) {
        System.out.println("\nChanging Session Time...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.print("Enter session ID to update: ");
            int sessionId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new start date and time (YYYY-MM-DD HH:mm:ss): ");
            String newStartDateTimeStr = scanner.nextLine();
            Timestamp newStartDateTime = Timestamp.valueOf(newStartDateTimeStr);

            System.out.print("Enter new end date and time (YYYY-MM-DD HH:mm:ss): ");
            String newEndDateTimeStr = scanner.nextLine();
            Timestamp newEndDateTime = Timestamp.valueOf(newEndDateTimeStr);

            String sql = "UPDATE training_session SET start_date_time = ?, end_date_time = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, newStartDateTime);
            pstmt.setTimestamp(2, newEndDateTime);
            pstmt.setInt(3, sessionId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Session time updated successfully!");
            } else {
                System.out.println("Session not found with ID: " + sessionId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void changeSessionCapacity(Scanner scanner) {
        System.out.println("\nChanging Session Capacity...");

        System.out.print("Enter session ID to update: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new maximum capacity: ");
        int newCapacity = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE training_session SET maximum_capacity = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, newCapacity);
            pstmt.setInt(2, sessionId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Session capacity updated successfully!");
            } else {
                System.out.println("Session not found with ID: " + sessionId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void cancelSession(Scanner scanner) {
        System.out.println("\nCanceling Session...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.print("Enter session ID to cancel: ");
            int sessionId = scanner.nextInt();

            String deleteMembersSQL = "DELETE FROM training_session_for_members WHERE session_id = ?";
            PreparedStatement deleteMembersStmt = conn.prepareStatement(deleteMembersSQL);
            deleteMembersStmt.setInt(1, sessionId);
            deleteMembersStmt.executeUpdate();

            String deleteSessionSQL = "DELETE FROM training_session WHERE id = ?";
            PreparedStatement deleteSessionStmt = conn.prepareStatement(deleteSessionSQL);
            deleteSessionStmt.setInt(1, sessionId);

            int rowsAffected = deleteSessionStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Session canceled successfully!");
            } else {
                System.out.println("Session not found with ID: " + sessionId);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void roomBookingManagement(Scanner scanner) {
        System.out.println("\nRoom Booking Management");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            int choice;

            do {
                System.out.println("\n1. View Booked Rooms");
                System.out.println("2. View Available Rooms");
                System.out.println("3. Book a New Room");
                System.out.println("4. Cancel a Booked Room");
                System.out.println("5. Back");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewBookedRooms();
                        break;
                    case 2:
                        viewAvailableRooms();
                        break;
                    case 3:
                        bookNewRoom(scanner);
                        break;
                    case 4:
                        cancelRoom(scanner);
                        break;
                    case 5:
                        System.out.println("Returning to main menu.");
                        return;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } while (choice != 5);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void viewBookedRooms() {
        System.out.println("\nBooked Rooms:");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM room_booking WHERE trainer_id IS NOT NULL";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println("Booking ID: " + rs.getInt("id"));
                    System.out.println("Room Number: " + rs.getString("room_number"));
                    System.out.println("Start Date: " + rs.getDate("start_date_booking"));
                    System.out.println("End Date: " + rs.getDate("end_date_booking"));
                    System.out.println("Trainer ID: " + rs.getInt("trainer_id"));
                    System.out.println("----------------------");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM room_booking WHERE trainer_id IS NULL";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println("Booking ID: " + rs.getInt("id"));
                    System.out.println("Room Number: " + rs.getString("room_number"));
                    System.out.println("Start Date: " + rs.getDate("start_date_booking"));
                    System.out.println("End Date: " + rs.getDate("end_date_booking"));
                    System.out.println("----------------------");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void bookNewRoom(Scanner scanner) {
        System.out.println("\nBooking a New Room");

        System.out.print("Enter Room Number: ");
        String roomNumber = scanner.nextLine();

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String insertSql = "INSERT INTO room_booking (room_number, start_date_booking, end_date_booking) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, roomNumber);
                pstmt.setDate(2, Date.valueOf(startDate));
                pstmt.setDate(3, Date.valueOf(endDate));
                pstmt.executeUpdate();
                System.out.println("Room booked successfully!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void cancelRoom(Scanner scanner) {
        System.out.println("\nCanceling a Room Booking");

        System.out.print("Enter Room Booking ID to cancel: ");
        int bookingId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String deleteSessionSql = "DELETE FROM training_session WHERE room_booking_id = ?";
            try (PreparedStatement deleteSessionStmt = conn.prepareStatement(deleteSessionSql)) {
                deleteSessionStmt.setInt(1, bookingId);
                int sessionRowsAffected = deleteSessionStmt.executeUpdate();
                System.out.println("Deleted " + sessionRowsAffected + " related training_session entries.");
            }

            String deleteBookingSql = "DELETE FROM room_booking WHERE id = ?";
            try (PreparedStatement deleteBookingStmt = conn.prepareStatement(deleteBookingSql)) {
                deleteBookingStmt.setInt(1, bookingId);
                int bookingRowsAffected = deleteBookingStmt.executeUpdate();
                if (bookingRowsAffected > 0) {
                    System.out.println("Room booking canceled successfully!");
                } else {
                    System.out.println("No room booking found with the given ID: " + bookingId);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void main(String[] args) {
        finalProject dbOps = new finalProject();
        Scanner scanner = new Scanner(System.in);

        int choice;

        System.out.println("Welcome to the Health and Fitness Club Management System!");

        do {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Member Functions");
            System.out.println("2. Trainer Functions");
            System.out.println("3. Administrative Staff Functions");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Member Functions
                    dbOps.handleMemberFunctions(scanner);
                    break;
                case 2:
                    // Trainer Functions
                    dbOps.handleTrainerFunctions(scanner);
                    break;
                case 3:
                    // Administrative Functions
                    dbOps.handleAdminFunctions(scanner);
                    break;
                case 4:
                    // Exit program
                    System.out.println("Exiting Health and Fitness Club Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);

        scanner.close();
    }
}