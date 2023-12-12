package br.com.ifpb.carros.dao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.ifpb.carros.util.EmailConf;

public class EmailDao {
    public boolean enviarEmail(String dest, String assunto, String corpo){
        final String username = EmailConf.getProps().getProperty("mail.username");
        final String password = EmailConf.getProps().getProperty("mail.password");

        Session session = Session.getInstance(EmailConf.getProps(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest));
            message.setSubject(assunto);
            message.setText(corpo);
            Transport.send(message);
            
            return true;
        }catch (MessagingException e){
           return false;
        }
    }

}
