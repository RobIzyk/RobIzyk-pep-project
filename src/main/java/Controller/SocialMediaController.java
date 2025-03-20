package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
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
            ctx.status(400).result("Invalid credentials");
        }
    }

    // Create a new message
    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message newMessage = socialMediaService.createMessage(message);
        if (newMessage == null) {
            ctx.status(400).result("User not found");
        } else {
        ctx.status(200).json(newMessage);
        }
    }

    // Get all messages
    private void getAllMessages(Context ctx) {
        ctx.json(socialMediaService.getAllMessages());
    }

    // Get a message by ID
    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        Message message = socialMediaService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(400).result("Message not found");
        }
    }

    // Delete a message
    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("id"));
        if (socialMediaService.deleteMessage(messageId)) {
            ctx.status(200).result("Message deleted");
        } else {
            ctx.status(400).result("Message not found");
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
