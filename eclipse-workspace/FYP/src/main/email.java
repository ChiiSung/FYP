package main;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//"calendarsystemfsktm@gmail.com","calendarsystem123++"

public class email {

    public static void main(String[] args) {
    	 String userEmail = "lingchiisung@gmail.com";
         String verificationCode = "1234";

         // Store the verificationCode and userEmail in the database

         sendVerificationEmail(userEmail, verificationCode);
    }
    
    private static void sendVerificationEmail(String userEmail, String verificationCode) {
        // Your email configuration
        final String username = "lingchiisung@gmail.com";
        final String password = "owhkkwycvshstykd";

        Properties props = new Properties();
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Email Verification Code");
            message.setText("Your verification code is: " + verificationCode);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}