package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;

public class SocialMediaController {

    private final SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Register a new user
        app.post("/register", this::registerUser);
        // Login user
        app.post("/login", this::loginUser);
        // Post a message
        app.post("/messages", this::createMessage);
        // Get all messages
        app.get("/messages", this::getAllMessages);
        // Get a message by ID
        app.get("/messages/{id}", this::getMessageById);
        // Delete a message
        app.delete("/messages/{id}", this::deleteMessage);
        // Update a message
        app.patch("/messages/{id}", this::updateMessage);
        // Get messages by user
        app.get("/accounts/{id}/messages", this::getMessagesByUser);

        return app;
    }

    // Register a new user
    private void registerUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            ctx.status(400).result("");
            return;
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            ctx.status(400).result("");
            return;
        }
        Account existingAccount = socialMediaService.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            ctx.status(400).result("");
            return;
        }
        
        Account newAccount = socialMediaService.registerAccount(account);
        ctx.status(200).json(newAccount);
        
    }

    // Login user
    private void loginUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        Account account = socialMediaService.loginAccount(username, password);
        if (account != null) {
            ctx.status(200).json(account);
        } else {
            ctx.status(400).result("");
        }
    }

    // Create a new message
    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        
        if (message.getMessage_text().isEmpty()) {
            ctx.status(400).result("");
            return;
        }

        Account account = socialMediaService.getAccountById(message.getPosted_by());
        if (account == null) {
            ctx.status(400).result("");
        }
        
        Message newMessage = socialMediaService.createMessage(message);

        if (newMessage == null || newMessage.getMessage_text().isEmpty()) {
            ctx.status(400).result("");
            return;
        }
    
        if (newMessage.getMessage_text().length() > 255) {
            ctx.status(400).result("");
            return;
        }

        else {
            ctx.status(200).json(newMessage);
        }
}


    // Get all messages
    private void getAllMessages(Context ctx) {
        try {

            List<Message> messages = socialMediaService.getAllMessages();

            if (messages == null || messages.isEmpty()) {
                ctx.status(200).json(new ArrayList<>());
            } else {
                ctx.status(200).json(messages);
            }
        } catch (Exception e) {
            ctx.status(500).result("");
        }
        
    }

    // Get a message by ID
private void getMessageById(Context ctx) {
    try {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        Message message = socialMediaService.getMessageById(messageId);

        if (message != null) {
            ctx.status(200).json(message);  // Message found, return the message with status 200
        } else {
            ctx.status(200).result("");  // Message not found, return status 404 (not found)
        }
    } catch (NumberFormatException e) {
        // If the message ID is not a valid integer, return status 400 (Bad Request)
        ctx.status(400).result("");
    } catch (Exception e) {
        // Handle any other unexpected errors and return status 500 (Internal Server Error)
        ctx.status(500).result("");
    }
}


    // Delete a message
    private void deleteMessage(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("id"));
    
            
            Message messageToDelete = socialMediaService.getMessageById(messageId);
    
            if (messageToDelete != null) {
                // If the message exists, delete it and return its JSON representation
                if (socialMediaService.deleteMessage(messageId)) {
                    ctx.status(200);
                    ctx.json(messageToDelete); // Ensure JSON is returned
                    return;
                }
            }
                ctx.status(200).result("");
                return;
            
        } catch (NumberFormatException e) {
            ctx.status(400).result("");
        }
    }

    // Update a message
    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        String newMessageText = ctx.formParam("message_text");
        if (socialMediaService.updateMessage(messageId, newMessageText)) {
            ctx.status(200).result("Message updated");
        } else {
            ctx.status(400).result("Message not found");
        }
    }

    // Get messages by user
    private void getMessagesByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(socialMediaService.getMessagesByUser(accountId));
    }
}
