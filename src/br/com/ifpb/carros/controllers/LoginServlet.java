package br.com.ifpb.carros.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.util.Base64;
import java.security.SecureRandom;

import br.com.ifpb.carros.dao.UsuarioDao;
import br.com.ifpb.carros.modelo.Usuario;
import br.com.ifpb.carros.modelo.UsuarioToken;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UsuarioDao userDAO = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String requestData = getRequestBody(request);
        Gson gson = new Gson();
        Usuario user = gson.fromJson(requestData, Usuario.class);
        
        Usuario usuario = userDAO.autenticarUsuario(user.getEmail(), user.getSenha());

        if (usuario == null) {
        	String jsonResponse = "{\"message\": \"Erro na autenticação com as credenciais utilizadas.\"}";
        	response.setStatus(400);
            response.getWriter().write(jsonResponse);
            return;
        }

        String token = generateToken();
        UsuarioToken secao = new UsuarioToken(token, usuario);
        boolean success = userDAO.salvarSecao(secao);

        if (!success) {
        	String jsonResponse = "{\"message\": \"Erro na autenticação.\"}";
        	response.setStatus(400);
            response.getWriter().write(jsonResponse);
            return;
        }
        String jsonResponse = "{\"message\": \"Login successful\", \"token\": \"" + token + "\"}";
        response.getWriter().write(jsonResponse);
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().encodeToString(randomBytes);
        return token;
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