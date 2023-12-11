package br.com.ifpb.carros.controllers;

import com.google.gson.Gson;

import br.com.ifpb.carros.dao.AnuncioDao;
import br.com.ifpb.carros.dao.UsuarioDao;
import br.com.ifpb.carros.modelo.Anuncio;
import br.com.ifpb.carros.modelo.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/anuncio")
public class CriarAnuncioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AnuncioDao anuncioDao = new AnuncioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("application/json");
    	
        String requestData = getRequestBody(request);
        Gson gson = new Gson();
        Anuncio anuncio = gson.fromJson(requestData, Anuncio.class);
        Usuario usuario = new UsuarioDao().retornaUsuario("test@gmail.com");
        
        if(usuario == null) {
        	String jsonResponse = "{\"message\": \"Falha ao registrar anúncio\"}";
        	response.getWriter().write(jsonResponse);
        	return;
        }
       
        anuncio.setAnunciante(usuario);
        boolean success = anuncioDao.cadastrarAnuncio(anuncio);
        if (success) {
            String jsonResponse = "{\"message\": \"Anúncio registrado com sucesso\"}";
            response.getWriter().write(jsonResponse);
            return;
        }
        
        String jsonResponse = "{\"message\": \"Falha ao registrar anúncio\"}";
        response.getWriter().write(jsonResponse);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        List<Anuncio> anuncios = anuncioDao.listarAnuncios();

        if (anuncios != null) {
            Gson gson = new Gson();
            String jsonAnuncios = gson.toJson(anuncios);
            response.getWriter().write(jsonAnuncios);
        } else {
            response.getWriter().write("{\"message\": \"Falha ao listar anúncios\"}");
        }
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
