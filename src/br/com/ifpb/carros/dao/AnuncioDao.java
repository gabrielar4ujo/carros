package br.com.ifpb.carros.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
	
	public List<Anuncio> listarAnuncios(String nome, Double minValue, Double maxValue) {
        EntityManager em = new JPAUtil().getEntityManager();
        try {
            em.getTransaction().begin();
            StringBuilder queryBuilder = new StringBuilder("SELECT a FROM Anuncio a");

            if (nome != null && !nome.isEmpty()) {
                queryBuilder.append(" WHERE LOWER(a.titulo) LIKE LOWER(:titulo)");
            }

            if (minValue != null) {
                queryBuilder.append(nome != null && !nome.isEmpty() ? " AND" : " WHERE");
                queryBuilder.append(" a.preco >= :minValue");
            }

            if (maxValue != null) {
                queryBuilder.append((nome != null && !nome.isEmpty() || minValue != null) ? " AND" : " WHERE");
                queryBuilder.append(" a.preco <= :maxValue");
            }

            TypedQuery<Anuncio> query = em.createQuery(queryBuilder.toString(), Anuncio.class);

            if (nome != null && !nome.isEmpty()) {
                query.setParameter("titulo", "%" + nome + "%");
            }

            if (minValue != null) {
                query.setParameter("minValue", minValue);
            }

            if (maxValue != null) {
                query.setParameter("maxValue", maxValue);
            }

            List<Anuncio> anuncios = query.getResultList();

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
