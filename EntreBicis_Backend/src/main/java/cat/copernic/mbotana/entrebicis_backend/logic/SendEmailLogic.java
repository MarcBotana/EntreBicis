package cat.copernic.mbotana.entrebicis_backend.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmailLogic {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmailPassword(String toEmail, String token) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("EntreBicis - Password Reset");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center; "
                +
                "box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border: 2px solid #01ECE9;'>" +
                "<h2 style='color: #50E820; font-size: 24px;'>🔐 Contrasenya</h2>" +
                "<p style='color: #333; font-size: 16px;'>Has sol·licitat restablir la teva contrasenya. Fes servir el següent codi:</p>"
                +
                "<div style='display: inline-block; padding: 10px 20px; background: #DDCB01; color: white; font-size: 22px;"
                +
                "font-weight: bold; border-radius: 5px; letter-spacing: 2px;'>" +
                "<span>" + token + "</span>" +
                "</div>" +
                "<p style='color: #555; margin-top: 15px; font-size: 14px;'>Si no has sol·licitat aquest canvi, ignora aquest correu.</p>"
                +
                "</div>" +
                "</body></html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendEmailGreetings(String toEmail, String name, String surname) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("EntreBicis - Benvingut!");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center; "
                +
                "box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border: 2px solid #01ECE9;'>" +
                "<h2 style='color: #50E820; font-size: 24px;'>🎉 Benvingut a EntreBicis!</h2>" +
                "<p style='color: #333; font-size: 16px;'>Benvolgut/da: " + name + " " + surname + "</p>" +
                "<p style='color: #333; font-size: 16px;'>Gràcies per registrarte en el programa EntreBicis!</p>" +
                "<p style='color: #333; font-size: 16px;'>Per raons de seguretat, hauràs de canviar la teva contrasenya en el primer inici de sessió.</p>"
                +
                "<p style='color: #555; margin-top: 15px; font-size: 14px;'>Si tens qualsevol dubte o problema, no dubtis en contactar-nos.</p>"
                +
                "<p style='color: #555; margin-top: 15px; font-size: 14px;'>Gràcies per confiar en nosaltres!</p>" +
                "</div>" +
                "</body></html>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
