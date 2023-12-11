package br.com.ifpb.carros.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.ifpb.carros.modelo.Anuncio;
import br.com.ifpb.carros.modelo.Usuario;

public class AnuncioDao {
	
	final EntityManager em = new JPAUtil().getEntityManager();

	public Boolean cadastrarAnuncio(Anuncio anuncio) {
        try {
            em.getTransaction().begin();
            em.merge(anuncio);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
        return true;
    }
	
	public List<Anuncio> listarAnuncios() {
        try {
        	em.getTransaction().begin();

            // Consulta para obter todos os anúncios
            List<Anuncio> anuncios = em.createQuery("SELECT a FROM Anuncio a", Anuncio.class).getResultList();

            em.getTransaction().commit();
            return anuncios;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
            	em.getTransaction().rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
}
