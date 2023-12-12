package br.com.ifpb.carros.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.ifpb.carros.modelo.Usuario;
import br.com.ifpb.carros.modelo.UsuarioToken;

public class UsuarioTokenDao {
	public Usuario validateToken(String token) {
		EntityManager em = new JPAUtil().getEntityManager();
		TypedQuery<UsuarioToken> query = em
				.createQuery("select u from UsuarioToken u where u.token = :pToken", UsuarioToken.class);

		query.setParameter("pToken",token);
		Usuario usuario;

		try {
			usuario = query.getSingleResult().getUsuario();
		} catch (NoResultException ex) {
			usuario = null;
		}

		em.close();
		return usuario;
	}
}
