package TicketBooking_application;

	import java.io.*;
	import java.text.SimpleDateFormat;
	import java.util.*;

	public class MovieTicketBooking2 {

	    static Scanner sc = new Scanner(System.in);

	    static String[] theatres = {
	        "S2 CinemaHall",
	        "Amrutha Talkies",
	        "Ashoka Theatre",
	        "Venkatrama Kalamandir"
	    };

	    static String[][] movies = {
	        {"OG-The Power Star", "Little Hearts", "Mirai", "Krish4"},
	        {"OG-The Power Star", "Little Hearts", "Mirai", "Krish4"},
	        {"OG-The Power Star", "Little Hearts", "Mirai", "Krish4"},
	        {"OG-The Power Star", "Little Hearts", "Mirai", "Krish4"}
	    };

	    public static boolean login(String role, String username, String password) {
	        String filename = "data/" + (role.equals("admin") ? "admin.txt" : "users.txt");
	        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] parts = line.split(",");
	                if (parts.length == 2) {
	                    if (parts[0].equals(username) && parts[1].equals(password)) {
	                        return true;
	                    }
	                }
	            }
	        } catch (IOException e) {
	            System.out.println("Error reading " + filename);
	        }
	        return false;
	    }

	    public static String getSeatFile(int theatreIdx, int movieIdx) {
	        String t = theatres[theatreIdx].replaceAll("\\s+", "");
	        String m = movies[theatreIdx][movieIdx].replaceAll("\\s+", "").replaceAll("-", "");
	        return "data/" + t + "_" + m + "_seats.txt";
	    }

	    public static boolean bookSeats(String seatFile, int requested) {
	        try {
	            File file = new File(seatFile);
	            if (!file.exists()) {
	                System.out.println("Seat file not found: " + seatFile);
	                return false;
	            }
	            BufferedReader reader = new BufferedReader(new FileReader(file));
	            int available = Integer.parseInt(reader.readLine());
	            reader.close();

	            if (requested > available) {
	                System.out.println("Booking failed: Only " + available + " seats left.");
	                return false;
	            }

	            int updated = available - requested;
	            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
	            writer.write(String.valueOf(updated));
	            writer.close();

	            System.out.println("Booking successful. Seats remaining: " + updated);
	            return true;

	        } catch (IOException | NumberFormatException e) {
	            System.out.println("Error processing seat file.");
	            return false;
	        }
	    }

	    public static void saveBooking(String username, String theatre, String movie, int qty) {
	        String bookingFile = "data/bookings.txt";
	        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        String record = username + "," + theatre + "," + movie + "," + qty + "," + datetime;

	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bookingFile, true))) {
	            bw.write(record);
	            bw.newLine();
	        } catch (IOException e) {
	            System.out.println("Error saving booking record.");
	        }
	    }

	    public static void showBookingsToday() {
	        String bookingFile = "data/bookings.txt";
	        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        int count = 0;

	        try (BufferedReader br = new BufferedReader(new FileReader(bookingFile))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                if (line.contains(today)) count++;
	            }
	            System.out.println("Total bookings today: " + count);
	        } catch (IOException e) {
	            System.out.println("Error reading booking records.");
	        }
	    }

	    public static void generateAndSaveInvoice(String username, String theatre, String movie, int qty) {
	        int ticketPrice = 0;
	        switch (movie) {
	            case "OG-The Power Star":
	                ticketPrice = 250;
	                break;
	            case "Little Hearts":
	                ticketPrice = 200;
	                break;
	            case "Mirai":
	                ticketPrice = 220;
	                break;
	            case "Krish4":
	                ticketPrice = 400;
	                break;
	            default:
	                ticketPrice = 250;
	        }
	        int total = ticketPrice * qty;

	        StringBuilder invoice = new StringBuilder();
	        invoice.append("------------ TICKET INVOICE ------------\n");
	        invoice.append("Username: ").append(username).append("\n");
	        invoice.append("Theatre: ").append(theatre).append("\n");
	        invoice.append("Movie: ").append(movie).append("\n");
	        invoice.append("Tickets: ").append(qty).append("\n");
	        invoice.append("Price per ticket: ₹").append(ticketPrice).append("\n");
	        invoice.append("Total: ₹").append(total).append("\n");
	        invoice.append("Booking Date and Time: ")
	               .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
	        invoice.append("----------------------------------------\n");
	        invoice.append("Thank you for booking with us!\n");

	        System.out.println(invoice.toString());

	        String filename = "invoices/" + username + "_" + System.currentTimeMillis() + "_invoice.txt";

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
	            writer.write(invoice.toString());
	            System.out.println("Invoice saved successfully as: " + filename);
	        } catch (IOException e) {
	            System.out.println("Error saving invoice file.");
	        }
	    }

	    public static void main(String[] args) {
	        System.out.println("Welcome to Movie Ticket Booking System");

	        System.out.println("Select Role:");
	        System.out.println("1. Admin");
	        System.out.println("2. User");
	        System.out.print("Enter choice: ");
	        int choice = sc.nextInt();
	        sc.nextLine(); 
	        if (choice == 1) {
	            System.out.print("Enter admin username: ");
	            String adUser = sc.nextLine();
	            System.out.print("Enter admin password: ");
	            String adPass = sc.nextLine();

	            if (login("admin", adUser, adPass)) {
	                System.out.println("Admin login successful!");
	                showBookingsToday();
	            } else {
	                System.out.println("Admin login failed.");
	            }

	        } else if (choice == 2) {
	            System.out.print("Enter username: ");
	            String usUser = sc.nextLine();
	            System.out.print("Enter password: ");
	            String usPass = sc.nextLine();

	            if (login("user", usUser, usPass)) {
	                System.out.println("User login successful!");

	                System.out.println("Select Theatre:");
	                for (int i = 0; i < theatres.length; i++) {
	                    System.out.println((i + 1) + ". " + theatres[i]);
	                }
	                System.out.print("Enter choice: ");
	                int theatreIdx = sc.nextInt() - 1;

	                if (theatreIdx < 0 || theatreIdx >= theatres.length) {
	                    System.out.println("Invalid theatre.");
	                    return;
	                }

	                System.out.println("Select Movie:");
	                for (int i = 0; i < movies[theatreIdx].length; i++) {
	                    System.out.println((i + 1) + ". " + movies[theatreIdx][i]);
	                }
	                System.out.print("Enter choice: ");
	                int movieIdx = sc.nextInt() - 1;

	                if (movieIdx < 0 || movieIdx >= movies[theatreIdx].length) {
	                    System.out.println("Invalid movie.");
	                    return;
	                }

	                System.out.print("Enter number of seats to book: ");
	                int seats = sc.nextInt();

	                String seatFile = getSeatFile(theatreIdx, movieIdx);

	                if (bookSeats(seatFile, seats)) {
	                    saveBooking(usUser, theatres[theatreIdx], movies[theatreIdx][movieIdx], seats);
	                    System.out.println("Booking recorded successfully!");
	                    generateAndSaveInvoice(usUser, theatres[theatreIdx], movies[theatreIdx][movieIdx], seats);
	                } else {
	                    System.out.println("Booking failed due to insufficient seats.");
	                }

	            } else {
	                System.out.println("User login failed.");
	            }

	        } else {
	            System.out.println("Invalid option selected.");
	        }

	        sc.close();
	    }
	}


