package br.com.ifpb.carros.dao;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.ifpb.carros.modelo.Usuario;

public class UsuarioDao {

	public boolean existe(Usuario usuario) {
		EntityManager em = new JPAUtil().getEntityManager();
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
	
	  public Usuario retornaUsuario(String email) {
		  EntityManager em = new JPAUtil().getEntityManager();
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
		EntityManager em = new JPAUtil().getEntityManager();
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
		EntityManager em = new JPAUtil().getEntityManager();
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

}
