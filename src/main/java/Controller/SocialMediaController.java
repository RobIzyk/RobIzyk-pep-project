package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final SocialMediaService socialMediaService;

    public SocialMediaController() {
        this.socialMediaService = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Register a new user
        app.post("/register", this::registerUser);
        // Login user
        app.post("/login", this::loginUser);
        // Post a message
        app.post("/messages", this::createMessage);

        return app;
    }

    private void registerUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        // Input validation
        if (username == null || username.trim().isEmpty() || password == null || password.length() < 5) {
            ctx.status(400).json("Invalid input data.");
            return;
        }

        Account account = new Account(username, password);
        Account registeredAccount = socialMediaService.registerAccount(account);

        if (registeredAccount == null) {
            ctx.status(400).json("Account could not be created.");
        } else {
            ctx.status(200).json(registeredAccount);
        }
    }

    private void loginUser(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        Account account = socialMediaService.loginAccount(username, password);

        if (account != null) {
            ctx.status(200).json(account);
        } else {
            ctx.status(401).json("Invalid username or password.");
        }
    }

    private void createMessage(Context ctx) {
        int postedBy = Integer.parseInt(ctx.formParam("posted_by"));
        String messageText = ctx.formParam("message_text");
        long timePostedEpoch = System.currentTimeMillis() / 1000;

        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            ctx.status(400).json("Invalid message data.");
            return;
        }

        Message message = new Message(postedBy, messageText, timePostedEpoch);
        Message createdMessage = socialMediaService.createMessage(message);

        if (createdMessage == null) {
            ctx.status(400).json("Message could not be posted.");
        } else {
            ctx.status(200).json(createdMessage);
        }
    }
}