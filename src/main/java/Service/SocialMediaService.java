package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

import java.util.*;

public class SocialMediaService {

    private final SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        socialMediaDAO = new SocialMediaDAO();
    }
    public Account registerAccount(Account account) {
        return socialMediaDAO.createAccount(account);
    }

    // Login a user
    public Account loginAccount(String username, String password) {
        return socialMediaDAO.findAccountByUsernameAndPassword(username, password);
    }

    public Account getAccountByUsername(String username) {
        return socialMediaDAO.getAccountByUsername(username);
    }

    public Account getAccountById(int account_id) {
        return socialMediaDAO.getAccountById(account_id);
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        return socialMediaDAO.getAllAccounts();
    }

    // ------------------------------- Message Service Methods -------------------------------

    // Create a new message
    public Message createMessage(Message message) {
        Account user = socialMediaDAO.getAccountById(message.getPosted_by());
        if (user == null) {
            return null;
        }
        return socialMediaDAO.createMessage(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return socialMediaDAO.getAllMessages();
    }

    // Get a message by ID
    public Message getMessageById(int messageId) {
        return socialMediaDAO.getMessageById(messageId);
    }

    // Delete a message
    public boolean deleteMessage(int messageId) {
        return socialMediaDAO.deleteMessage(messageId);
    }

    // Update a message
    public boolean updateMessage(int messageId, String newMessageText) {
        return socialMediaDAO.updateMessage(messageId, newMessageText);
    }

    // Get all messages by a particular user
    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = socialMediaDAO.getMessagesByUser(accountId);
        return (messages == null) ? new ArrayList<>() : messages;
    }
}
