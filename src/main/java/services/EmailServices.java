package services;

import beans.EmailMessage;
import org.omg.CORBA.portable.ApplicationException;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

public class EmailServices {

    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static Properties props = new Properties();

    static {
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendMail(EmailMessage emailMessageDTO) throws ApplicationException {
        String sender_Email = "devwebchichoo@gmail.com";
        String sender_email_pass = "nbhdwkvvyptdreek";

        try {

            // Connection to Mail Server
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sender_Email, sender_email_pass);
                        }
                    });


            session.setDebug(true);

            // Create a message
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(sender_Email, "HousewareNLU");
            msg.setFrom(addressFrom);

            // Set TO addresses
            String[] emailIds = new String[0];

            if (emailMessageDTO.getTo() != null) {
                emailIds = emailMessageDTO.getTo().split(",");
            }

            // Set CC addresses
            String[] emailIdsCc = new String[0];

            if (emailMessageDTO.getCc() != null) {
                emailIdsCc = emailMessageDTO.getCc().split(",");
            }

            // Set BCC addresses
            String[] emailIdsBcc = new String[0];

            if (emailMessageDTO.getBcc() != null) {
                emailIdsBcc = emailMessageDTO.getBcc().split(",");
            }

            InternetAddress[] addressTo = new InternetAddress[emailIds.length];

            for (int i = 0; i < emailIds.length; i++) {
                addressTo[i] = new InternetAddress(emailIds[i]);
            }

            InternetAddress[] addressCc = new InternetAddress[emailIdsCc.length];

            for (int i = 0; i < emailIdsCc.length; i++) {
                addressCc[i] = new InternetAddress(emailIdsCc[i]);
            }

            InternetAddress[] addressBcc = new InternetAddress[emailIdsBcc.length];

            for (int i = 0; i < emailIdsBcc.length; i++) {
                addressBcc[i] = new InternetAddress(emailIdsBcc[i]);
            }

            if (addressTo.length > 0) {
                msg.setRecipients(Message.RecipientType.TO, addressTo);
            }

            if (addressCc.length > 0) {
                msg.setRecipients(Message.RecipientType.CC, addressCc);
            }

            if (addressBcc.length > 0) {
                msg.setRecipients(Message.RecipientType.BCC, addressBcc);
            }

            // Setting the Subject and Content Type
            msg.setSubject(MimeUtility.encodeText(emailMessageDTO.getSubject(), "utf-8", "B"));
            // Set message MIME type
//            switch (emailMessageDTO.getMessageType()) {
//                case EmailMessage.HTML_MSG:
                    msg.setContent(emailMessageDTO.getMessage(), "text/html; charset=UTF-8");
//                    break;
//                case EmailMessage.TEXT_MSG:
//                    msg.setContent(emailMessageDTO.getMessage(), "text/plain;charset=UTF-8");
//                    break;
//            }
            // Send the mail
            Transport.send(msg);

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}