package br.com.ifpb.carros.controllers;

import com.google.gson.Gson;

import br.com.ifpb.carros.dao.AnuncioDao;
import br.com.ifpb.carros.dao.UsuarioTokenDao;
import br.com.ifpb.carros.modelo.Anuncio;
import br.com.ifpb.carros.modelo.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
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
    	
    	String authorization = request.getHeader("Authorization");
    	if(authorization == null) {
    		String jsonResponse = "{\"message\": \"Usuário não está logado\"}";
    		response.setStatus(403);
        	response.getWriter().write(jsonResponse);
        	return;
    	}
    	
    	Usuario usuario = new UsuarioTokenDao().validateToken(authorization);
    	
    	if(usuario == null) {
    		String jsonResponse = "{\"message\": \"Token inválido\"}";
    		response.setStatus(403);
        	response.getWriter().write(jsonResponse);
        	return;
    	}
    	
    	if(usuario.getAdmin() == false) {
    		String jsonResponse = "{\"message\": \"Somentes usuários admin podem cadastrar anúncios\"}";
    		response.setStatus(403);
        	response.getWriter().write(jsonResponse);
        	return;
    	}
    	
        String requestData = getRequestBody(request);
        Gson gson = new Gson();
        Anuncio anuncio = gson.fromJson(requestData, Anuncio.class);
        anuncio.setAnunciante(usuario);
        
        boolean success = anuncioDao.cadastrarAnuncio(anuncio);
        if (success) {
            String jsonResponse = "{\"message\": \"Anúncio registrado com sucesso\"}";
            response.getWriter().write(jsonResponse);
            return;
        }
        
        String jsonResponse = "{\"message\": \"Falha ao registrar anúncio\"}";
        response.setStatus(400);
        response.getWriter().write(jsonResponse);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");

            String nome = request.getParameter("name");
            String minValueStr = request.getParameter("minValue");
            String maxValueStr = request.getParameter("maxValue");

            Double minValue = null;
            Double maxValue = null;

            if (minValueStr != null && !minValueStr.isEmpty()) {
                minValue = Double.parseDouble(minValueStr);
            }

            if (maxValueStr != null && !maxValueStr.isEmpty()) {
                maxValue = Double.parseDouble(maxValueStr);
            }

            List<Anuncio> anuncios = anuncioDao.listarAnuncios(nome, minValue, maxValue);

            if (anuncios != null) {
                Gson gson = new Gson();
                String jsonAnuncios = gson.toJson(anuncios);
                response.getWriter().write(jsonAnuncios);
            } else {
                response.getWriter().write("{\"message\": \"Falha ao listar anúncios\"}");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
