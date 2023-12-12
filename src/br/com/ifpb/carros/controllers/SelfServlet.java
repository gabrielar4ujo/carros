package br.com.ifpb.carros.controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import br.com.ifpb.carros.dao.UsuarioTokenDao;
import br.com.ifpb.carros.modelo.Usuario;

@WebServlet("/self")
public class SelfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UsuarioTokenDao usuarioTokenDao = new UsuarioTokenDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String authorization = request.getHeader("Authorization");
    	
    	if(authorization == null) {
    		String jsonResponse = "{\"message\": \"Usuário não está logado\"}";
    		response.setStatus(403);
        	response.getWriter().write(jsonResponse);
        	return;
    	}
    	
    	Usuario usuario = usuarioTokenDao.validateToken(authorization);
    	if(usuario == null) {
    		String jsonResponse = "{\"message\": \"Token inválido\"}";
    		response.setStatus(403);
        	response.getWriter().write(jsonResponse);
        	return;
    	}
    	
    	usuario.setSenha(null);
		Gson gson = new Gson();
		String jsonUsuario = gson.toJson(usuario);
		response.getWriter().write(jsonUsuario);
    }
}