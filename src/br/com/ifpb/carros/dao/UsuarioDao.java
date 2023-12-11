package br.com.ifpb.carros.dao;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.ifpb.carros.modelo.Usuario;
import br.com.ifpb.carros.modelo.UsuarioToken;

public class UsuarioDao {
	final EntityManager  em = new JPAUtil().getEntityManager();

	public boolean existe(Usuario usuario) {
		TypedQuery<Usuario> query = em
				.createQuery("select u from Usuario u where u.email = :pEmail and u.senha = :pSenha", Usuario.class);

		query.setParameter("pEmail", usuario.getEmail());
		query.setParameter("pSenha", usuario.getSenha());
		
		System.out.println("pEmail: " +  usuario.getEmail());
		System.out.println("pSenha: " +  usuario.getSenha());

		try {
			query.getSingleResult();
		} catch (NoResultException ex) {
			return false;
		}

		em.close();
		return true;
	}
	
	   public Usuario autenticarUsuario(String email, String senha) {
		   Usuario usuario = null;
	       em.getTransaction().begin();
	       TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
	       query.setParameter("email", email);
	       query.setParameter("senha", senha);
           
	   		System.out.println("pEmail: TESTE" + email);
	   		System.out.println("pSenha: " +  senha);
	        try{

	            usuario = query.getSingleResult();
	        } catch (NoResultException ex) {
				return null;
			}
	        em.close();
	        return usuario;
	    }
	
	  public Usuario retornaUsuario(String email) {
	       Usuario usuario = null;
	        try{
	            em.getTransaction().begin();
	            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
	            query.setParameter("email", email);
	            usuario = query.getSingleResult();
	            em.getTransaction().commit();
	            em.clear();
	        }catch (Exception e){
	            e.printStackTrace();
	        }

	        return usuario;
	    }
	
	public Boolean cadastrarUsuario(Usuario usuario) {
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
	
	public boolean jaExisteAdmin() {
		TypedQuery<Usuario> query = em
				.createQuery("select u from Usuario u where u.admin = 1", Usuario.class);

		try {
			query.getSingleResult();
		} catch (NoResultException ex) {
			return false;
		}
		
		em.close();
		return true;
	}
 
   public boolean salvarSecao(UsuarioToken secao) {
	   EntityManager em = new JPAUtil().getEntityManager();
	   try{
		   em.getTransaction().begin();
           em.persist(secao);
           em.getTransaction().commit();
       } catch (Exception e) {
           if (em.getTransaction().isActive())
               em.getTransaction().rollback();
           e.printStackTrace();
           return false;
       }
	   em.close();
	   return true;
   }
}
