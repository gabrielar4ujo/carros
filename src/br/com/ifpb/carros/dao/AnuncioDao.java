package br.com.ifpb.carros.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.ifpb.carros.modelo.Anuncio;

public class AnuncioDao {
	public Boolean cadastrarAnuncio(Anuncio anuncio) {
        EntityManager em = new JPAUtil().getEntityManager();
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
        EntityManager em = new JPAUtil().getEntityManager();
        try {
        	em.getTransaction().begin();

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
