/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject
/**
 *
 * @author Mahlatse
 */
public class Message {
    
}
  Random random = new Random();
    int messageID;
    int numMessage = 0;
    String recipient;
    String message;
    String messageHash;
    ArrayList<String> allMessages = new ArrayList<>();
    
    public boolean checkMessageID(){
        String n1 = Integer.toString(messageID);
        return n1.length() <= 10; 
    }
    
    public boolean messageRequirements(String msg){
        return msg.length() <= 250;
    }
    
    public String checkRecipientCell(Scanner sc){
        String number = sc.nextLine();
    
        if(number.length() == 12 && number.startsWith("+27"))
            return "Cell phone Number Successfully captured";
        return "Cellphone Number incorrectly Formatted or does not contain international country code please correct the number and try again";
    }
    
    public String createMessageHash(String messageText, int id){
        String[] words = messageText.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length-1] : words[0];
        
        String hash = id + ":" + numMessage + ":" + firstWord.toUpperCase() + lastWord.toUpperCase();
        return hash;
    }
    
    public void sentMessage(Scanner sc){
        int choice;
        int msgAmount;
        
        System.out.println("'999' to Quit");
        System.out.print("How Many Messages: ");
        msgAmount = sc.nextInt();
        sc.nextLine(); // consume newline
        
        if(msgAmount == 999) {
            return;
        }
        
        numMessage = msgAmount;
        
        for(int i = 0; i < msgAmount; i++) {
            // Generate new message ID for each message
            messageID = random.nextInt(1000000000, 2000000000);
            
            if(checkMessageID()){
                System.out.println("\nMessage " + (i+1) + " of " + msgAmount);
                System.out.println("Messages left: " + (msgAmount - i));
                System.out.println("MessageID: " + messageID + "\n");
                
                System.out.print("Enter recipient: ");
                recipient = sc.nextLine();
                
                System.out.print("Type your message: ");
                message = sc.nextLine();
                
                while(!messageRequirements(message)){
                    System.out.println("Please enter a message of less than 250 characters");
                    System.out.print("Type: ");
                    message = sc.nextLine();
                }
                
                System.out.println("\n1. Send Message");
                System.out.println("2. Disregard Message");
                System.out.println("3. Store Message to send later");
                System.out.print("\nChoice: ");
                
                choice = sc.nextInt();
                sc.nextLine();
                
                switch(choice){
                    case 1 -> {
                        System.out.println("Message successfully sent!");
                        String hash = createMessageHash(message, messageID);
                        System.out.println("Message Hash: " + hash);
                        storeMessage(messageID + ": " + message + " (Sent to: " + recipient + ")");
                    }
                        
                    case 2 -> {
                        System.out.println("Press 0 to delete the message");
                        int deleteChoice = sc.nextInt();
                        sc.nextLine();
                        if(deleteChoice == 0) {
                            System.out.println("Message disregarded/deleted");
                        }
                    }
                        
                    case 3 -> {
                        System.out.println("Message successfully stored for later");
                        storeMessage(messageID + ": " + message + " (Stored for: " + recipient + ")");
                    }
                        
                    default -> System.out.println("Invalid Input");
                }
                System.out.println();
            }
        }
        
        System.out.println("\nAll messages processed!");
    }
    
    public void printMessages(){
        if(allMessages.isEmpty()){
            System.out.println("No messages stored.");
        } else {
            System.out.println("\n=== STORED MESSAGES ===");
            for(int i = 0; i < allMessages.size(); i++){
                System.out.println((i+1) + ". " + allMessages.get(i));
            }
        }
    }
    
    public int returnTotalMessages(){
        return numMessage;
    }
    
    public void storeMessage(String msg){
        allMessages.add(msg);
        System.out.println("Message stored. Total stored messages: " + allMessages.size());
    }

public class MessageManager {
    
    // Parallel arrays - no hard-coding in menu
    private static final ArrayList<String> messageIds = new ArrayList<>();
    private static final ArrayList<String> recipients = new ArrayList<>();
    private static final ArrayList<String> senders = new ArrayList<>();
    private static final ArrayList<String> messages = new ArrayList<>();
    private static final ArrayList<String> flags = new ArrayList<>();
    private static final ArrayList<String> hashes = new ArrayList<>();
    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadTestData();
        loadFromJSON();

        int choice;
        do {
            printMenu();
            choice = getIntInput();
            handleMenu(choice);
        } while (choice != 0);
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.println("4. Stored Messages");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void handleMenu(int choice) {
        switch (choice) {
            case 1:
                addMessage("Sent");
                break;
            case 2:
                addMessage("Disregard");
                break;
            case 3:
                addMessage("Stored");
                break;
            case 4:
                storedMessagesMenu();
                break;
            case 0:
                saveToJSON();
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void addMessage(String flag) {
        System.out.print("Enter recipient: ");
        String recipient = sc.nextLine().trim();
        System.out.print("Enter message: ");
        String content = sc.nextLine().trim();

        String id = UUID.randomUUID().toString();
        String hash = generateHash(recipient + content + flag);

        messageIds.add(id);
        recipients.add(recipient);
        senders.add("You");
        messages.add(content);
        flags.add(flag);
        hashes.add(hash);

        switch (flag) {
            case "Sent":
                sentMessages.add(content);
                break;
            case "Disregard":
                disregardedMessages.add(content);
                break;
            case "Stored":
                storedMessages.add(content);
                break;
        }

        System.out.println("Message " + flag + " successfully. Hash: " + hash);
    }

    private static void storedMessagesMenu() {
        char choice;
        do {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a. Display sender and recipient");
            System.out.println("b. Display longest message");
            System.out.println("c. Search by Message ID");
            System.out.println("d. Search by Recipient");
            System.out.println("e. Delete by Message Hash");
            System.out.println("f. Display full report");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = sc.next().charAt(0);
            sc.nextLine();

            switch (choice) {
                case 'a':
                    displaySenderRecipient();
                    break;
                case 'b':
                    displayLongestMessage();
                    break;
                case 'c':
                    searchById();
                    break;
                case 'd':
                    searchByRecipient();
                    break;
                case 'e':
                    deleteByHash();
                    break;
                case 'f':
                    displayReport();
                    break;
                case '0':
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != '0');
    }

    private static void loadTestData() {
        addTestMessage("msg1", "+27834557896", "You", "Did you get the cake?", "Sent");
        addTestMessage("msg2", "+27838884567", "You", 
            "Where are you? You are late! I have asked you to be on time.", "Stored");
        addTestMessage("msg3", "+27834484567", "You", "Yohoooo, I am at your gate.", "Disregard");
        addTestMessage("msg4", "0838884567", "You", "it is dinner time !", "Sent");
        addTestMessage("msg5", "+27838884567", "You", "Ok, I am leaving without you.", "Stored");
    }

    private static void addTestMessage(String id, String recipient, String sender, 
                                       String message, String flag) {
        String hash = generateHash(recipient + message + flag);
        messageIds.add(id);
        recipients.add(recipient);
        senders.add(sender);
        messages.add(message);
        flags.add(flag);
        hashes.add(hash);

        if ("Sent".equals(flag)) {
            sentMessages.add(message);
        } else if ("Disregard".equals(flag)) {
            disregardedMessages.add(message);
        } else if ("Stored".equals(flag)) {
            storedMessages.add(message);
        }
    }

    // 2b: Display longest message - 4-5 marks
    private static void displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        int maxIndex = 0;
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).length() > storedMessages.get(maxIndex).length()) {
                maxIndex = i;
            }
        }
        System.out.println("Longest message: " + storedMessages.get(maxIndex));
        System.out.println("Length: " + storedMessages.get(maxIndex).length());
    }

    // 2d: Search by recipient - 8-10 marks
    private static void searchByRecipient() {
        System.out.print("Enter recipient number: ");
        String recipient = sc.nextLine().trim();
        boolean found = false;
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(recipient) && "Stored".equals(flags.get(i))) {
                System.out.println("Msg: " + messages.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages found for this recipient.");
        }
    }

    // 2c: Search by Message ID - 8-10 marks
    private static void searchById() {
        System.out.print("Enter Message ID: ");
        String id = sc.nextLine().trim();
        int index = messageIds.indexOf(id);
        if (index != -1 && "Stored".equals(flags.get(index))) {
            System.out.println("Recipient: " + recipients.get(index));
            System.out.println("Msg: " + messages.get(index));
        } else {
            System.out.println("Message ID not found in stored messages.");
        }
    }

    // 2e: Delete by hash - 8-10 marks
    private static void deleteByHash() {
        System.out.print("Enter message hash: ");
        String hash = sc.nextLine().trim();
        int index = hashes.indexOf(hash);
        if (index != -1 && "Stored".equals(flags.get(index))) {
            System.out.println("Message: \"" + messages.get(index) + "\" successfully deleted.");
            messageIds.remove(index);
            recipients.remove(index);
            senders.remove(index);
            messages.remove(index);
            flags.remove(index);
            hashes.remove(index);
            storedMessages.remove(index);
        } else {
            System.out.println("Hash not found in stored messages.");
        }
    }

    // 2a: Display sender and recipient
    private static void displaySenderRecipient() {
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            if ("Stored".equals(flags.get(i))) {
                System.out.println("From: " + senders.get(i) + " | To: " + recipients.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages.");
        }
    }

    // 2f: Display report - 8-10 marks
    private static void displayReport() {
        System.out.println("\n--- Stored Messages Report ---");
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            if ("Stored".equals(flags.get(i))) {
                System.out.println("ID: " + messageIds.get(i));
                System.out.println("Hash: " + hashes.get(i));
                System.out.println("Recipient: " + recipients.get(i));
                System.out.println("Message: " + messages.get(i));
                System.out.println("----------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages.");
        }
    }

    // 8-10 marks: Read JSON file into array
    private static void loadFromJSON() {
        File file = new File("messages.json");
        if (!file.exists()) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                messageIds.add(obj.getString("id"));
                recipients.add(obj.getString("recipient"));
                senders.add(obj.getString("sender"));
                messages.add(obj.getString("message"));
                flags.add(obj.getString("flag"));
                hashes.add(obj.getString("hash"));
                if ("Stored".equals(obj.getString("flag"))) {
                    storedMessages.add(obj.getString("message"));
                }
            }
            System.out.println("Loaded " + arr.length() + " messages from JSON.");
        } catch (IOException e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    private static void saveToJSON() {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < messages.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("id", messageIds.get(i));
            obj.put("recipient", recipients.get(i));
            obj.put("sender", senders.get(i));
            obj.put("message", messages.get(i));
            obj.put("flag", flags.get(i));
            obj.put("hash", hashes.get(i));
            arr.put(obj);
        }
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(arr.toString(4));
        } catch (IOException e) {
            System.out.println("Error saving JSON: " + e.getMessage());
        }
    }

    private static String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString().substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            return "ERR";
        }
    }

    private static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            sc.next();
        }
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }
}
   // Parallel arrays - package-private for testing
    static final ArrayList<String> messageIds = new ArrayList<>();
    static final ArrayList<String> recipients = new ArrayList<>();
    static final ArrayList<String> senders = new ArrayList<>();
    static final ArrayList<String> messages = new ArrayList<>();
    static final ArrayList<String> flags = new ArrayList<>();
    static final ArrayList<String> hashes = new ArrayList<>();
    static final ArrayList<String> sentMessages = new ArrayList<>();
    static final ArrayList<String> storedMessages = new ArrayList<>();
    static final ArrayList<String> disregardedMessages = new ArrayList<>();

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadTestData();
        loadFromJSON();

        int choice;
        do {
            printMenu();
            choice = getIntInput();
            handleMenu(choice);
        } while (choice != 0);
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.println("4. Stored Messages");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void handleMenu(int choice) {
        switch (choice) {
            case 1 -> addMessage("Sent");
            case 2 -> addMessage("Disregard");
            case 3 -> addMessage("Stored");
            case 4 -> storedMessagesMenu();
            case 0 -> {
                saveToJSON();
                System.out.println("Exiting...");
            }
            default -> System.out.println("Invalid choice");
        }
    }

    private static void addMessage(String flag) {
        System.out.print("Enter recipient: ");
        String recipient = sc.nextLine().trim();
        System.out.print("Enter message: ");
        String content = sc.nextLine().trim();

        String id = UUID.randomUUID().toString();
        String hash = generateHash(recipient + content + flag);

        messageIds.add(id);
        recipients.add(recipient);
        senders.add("You");
        messages.add(content);
        flags.add(flag);
        hashes.add(hash);

        switch (flag) {
            case "Sent" -> sentMessages.add(content);
            case "Disregard" -> disregardedMessages.add(content);
            case "Stored" -> storedMessages.add(content);
        }

        System.out.println("Message " + flag + " successfully. Hash: " + hash);
    }

    private static void storedMessagesMenu() {
        char choice;
        do {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a. Display sender and recipient");
            System.out.println("b. Display longest message");
            System.out.println("c. Search by Message ID");
            System.out.println("d. Search by Recipient");
            System.out.println("e. Delete by Message Hash");
            System.out.println("f. Display full report");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            choice = sc.next().charAt(0);
            sc.nextLine();

            switch (choice) {
                case 'a' -> displaySenderRecipient();
                case 'b' -> displayLongestMessage();
                case 'c' -> searchById();
                case 'd' -> searchByRecipient();
                case 'e' -> deleteByHash();
                case 'f' -> displayReport();
                case '0' -> { }
                default -> System.out.println("Invalid choice");
            }
        } while (choice != '0');
    }

    // Load test data from rubric
    static void loadTestData() {
        addTestMessage("msg1", "+27834557896", "You", "Did you get the cake?", "Sent");
        addTestMessage("msg2", "+27838884567", "You", 
            "Where are you? You are late! I have asked you to be on time.", "Stored");
        addTestMessage("msg3", "+27834484567", "You", "Yohoooo, I am at your gate.", "Disregard");
        addTestMessage("msg4", "0838884567", "You", "it is dinner time !", "Sent");
        addTestMessage("msg5", "+27838884567", "You", "Ok, I am leaving without you.", "Stored");
    }

    private static void addTestMessage(String id, String recipient, String sender, 
                                       String message, String flag) {
        String hash = generateHash(recipient + message + flag);
        messageIds.add(id);
        recipients.add(recipient);
        senders.add(sender);
        messages.add(message);
        flags.add(flag);
        hashes.add(hash);

        switch (flag) {
            case "Sent" -> sentMessages.add(message);
            case "Disregard" -> disregardedMessages.add(message);
            case "Stored" -> storedMessages.add(message);
        }
    }

    // 4-5 marks: Display longest message
    static void displayLongestMessage() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        int maxIndex = 0;
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).length() > storedMessages.get(maxIndex).length()) {
                maxIndex = i;
            }
        }
        System.out.println("Longest message: " + storedMessages.get(maxIndex));
        System.out.println("Length: " + storedMessages.get(maxIndex).length());
    }

    // 8-10 marks: Search by recipient
    static void searchByRecipient() {
        System.out.print("Enter recipient number: ");
        String recipient = sc.nextLine().trim();
        boolean found = false;
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(recipient) && "Stored".equals(flags.get(i))) {
                System.out.println("Msg: " + messages.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages found for this recipient.");
        }
    }

    // 8-10 marks: Search by Message ID
    static void searchById() {
        System.out.print("Enter Message ID: ");
        String id = sc.nextLine().trim();
        int index = messageIds.indexOf(id);
        if (index != -1 && "Stored".equals(flags.get(index))) {
            System.out.println("Recipient: " + recipients.get(index));
            System.out.println("Msg: " + messages.get(index));
        } else {
            System.out.println("Message ID not found in stored messages.");
        }
    }

    // 8-10 marks: Delete by hash
    static void deleteByHash() {
        System.out.print("Enter message hash: ");
        String hash = sc.nextLine().trim();
        int index = hashes.indexOf(hash);
        if (index != -1 && "Stored".equals(flags.get(index))) {
            System.out.println("Message: \"" + messages.get(index) + "\" successfully deleted.");
            messageIds.remove(index);
            recipients.remove(index);
            senders.remove(index);
            messages.remove(index);
            flags.remove(index);
            hashes.remove(index);
            storedMessages.remove(index);
        } else {
            System.out.println("Hash not found in stored messages.");
        }
    }

    private static void displaySenderRecipient() {
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            if ("Stored".equals(flags.get(i))) {
                System.out.println("From: " + senders.get(i) + " | To: " + recipients.get(i));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages.");
        }
    }

    // 8-10 marks: Display report
    static void displayReport() {
        System.out.println("\n--- Stored Messages Report ---");
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            if ("Stored".equals(flags.get(i))) {
                System.out.println("ID: " + messageIds.get(i));
                System.out.println("Hash: " + hashes.get(i));
                System.out.println("Recipient: " + recipients.get(i));
                System.out.println("Message: " + messages.get(i));
                System.out.println("----------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No stored messages.");
        }
    }

    // 8-10 marks: Read JSON
    static void loadFromJSON() {
        File file = new File("messages.json");
        if (!file.exists()) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONArray arr = new JSONArray(content);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                messageIds.add(obj.getString("id"));
                recipients.add(obj.getString("recipient"));
                senders.add(obj.getString("sender"));
                messages.add(obj.getString("message"));
                flags.add(obj.getString("flag"));
                hashes.add(obj.getString("hash"));
                if ("Stored".equals(obj.getString("flag"))) {
                    storedMessages.add(obj.getString("message"));
                }
            }
            System.out.println("Loaded " + arr.length() + " messages from JSON.");
        } catch (IOException e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    static void saveToJSON() {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < messages.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("id", messageIds.get(i));
            obj.put("recipient", recipients.get(i));
            obj.put("sender", senders.get(i));
            obj.put("message", messages.get(i));
            obj.put("flag", flags.get(i));
            obj.put("hash", hashes.get(i));
            arr.put(obj);
        }
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(arr.toString(4));
        } catch (IOException e) {
            System.out.println("Error saving JSON: " + e.getMessage());
        }
    }

    private static String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString().substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            return "ERR";
        }
    }

    private static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            sc.next();
        }
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }


    