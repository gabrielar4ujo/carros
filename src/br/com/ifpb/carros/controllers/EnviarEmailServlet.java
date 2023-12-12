package br.com.ifpb.carros.controllers;

import br.com.ifpb.carros.dao.EmailDao;
import br.com.ifpb.carros.modelo.Email;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/entrar-contanto")
public class EnviarEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final EmailDao emailDAO = new EmailDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestData = getRequestBody(request);
        Gson gson = new Gson();
        Email email = gson.fromJson(requestData, Email.class);

        String dest = "monitoramentotempteste@gmail.com";
        String assunto = "Novo contato:  " + email.getEmail();
        String corpo = String.format(
                "Nome: %s\n" +
                "Telefone: %s\n" +
                "E-mail: %s\n" +
                "\n" +
                "--------Mensagem--------\n" +
                "\n" +
                "%s", email.getNome(), email.getTelefone(), email.getEmail(), email.getMsg());

        boolean sucesso = emailDAO.enviarEmail(dest, assunto, corpo);
        if(sucesso) {
            response.getWriter().write("E-mail enviado com sucesso!");
            return;
        }
        
        String jsonResponse = "{\"message\": \"Ocorreu um problema ao enviar e-mail.\"}";
        response.setStatus(400);
        response.getWriter().write(jsonResponse); 
    }
    
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = request.getReader();
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        
        return stringBuilder.toString();
    }  
}
