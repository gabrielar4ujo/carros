package br.com.ifpb.carros.controllers;

import com.google.gson.Gson;

import br.com.ifpb.carros.dao.UsuarioDao;
import br.com.ifpb.carros.modelo.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UsuarioDao userDAO = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
       
        String requestData = getRequestBody(request);
        Gson gson = new Gson();
        Usuario user = gson.fromJson(requestData, Usuario.class);
        
        boolean admin =  user.getAdmin();
        if(admin && userDAO.jaExisteAdmin()) {
        	 String jsonResponse = "{\"message\": \"Já existe um usuário admin\"}";
        	 response.setStatus(400);
             response.getWriter().write(jsonResponse);
             return;
        }
        
        boolean success = userDAO.cadastrarUsuario(user);
        if (success) {
            String jsonResponse = "{\"message\": \"Usuário registrado com sucesso\"}";
            response.getWriter().write(jsonResponse);
        } else {
            String jsonResponse = "{\"message\": \"Falha ao registrar usuário\"}";
            response.setStatus(400);
            response.getWriter().write(jsonResponse);
        }
        response.getWriter().write(user.getEmail());
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
