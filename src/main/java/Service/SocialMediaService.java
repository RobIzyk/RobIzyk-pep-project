package Service;


import Model.Account;
import Model.Message;
import DAO.SocialMediaDAO;

public class SocialMediaService {

    private final SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        this.socialMediaDAO = new SocialMediaDAO();
    }

    public Account registerAccount(Account account) {
        return socialMediaDAO.registerAccount(account);
    }

    public Account loginAccount(String username, String password) {
        return socialMediaDAO.loginAccount(username, password);
    }

    public Message createMessage(Message message) {
        return socialMediaDAO.createMessage(message);
    }
}