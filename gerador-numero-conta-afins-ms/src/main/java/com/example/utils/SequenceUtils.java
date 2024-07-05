package com.example.utils;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class SequenceUtils {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Transactional
    public void criaSequence(String nomeSequence, long startWith, long incrementBy, long minValue, long maxValue, long cache) {
        String createSequenceSQL = "CREATE SEQUENCE " + nomeSequence +
                " START WITH " + startWith + 
                " INCREMENT BY " + incrementBy + 
                " MINVALUE " + minValue + 
                " MAXVALUE " + maxValue + 
                " CACHE " + cache;

        entityManager.createNativeQuery(createSequenceSQL).executeUpdate();
    }
	
	public boolean existeSequence(String nomeSequence) {
		Query query = entityManager
				.createNativeQuery("SELECT s FROM pg_sequences s WHERE s.sequencename = :nomeSequence");
	    query.setParameter("nomeSequence", nomeSequence);

	    return !query.getResultList().isEmpty();
	}

    public Long obtemProximoValorDaSequence(String nomeSequence) {
        return (Long) entityManager.createNativeQuery("SELECT nextval(:nomeSequence)")
                                  .setParameter("nomeSequence", nomeSequence)
                                  .getSingleResult();
    }
}
