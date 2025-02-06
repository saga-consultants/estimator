/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 *
 * @author kwang
 */
@Named("pojoaccessor")
@Dependent
public class POJOaccessor implements  Serializable{
    private static final long serialVersionUID = 1L;
    
    @PersistenceContext    private EntityManager em;
    
    public POJOaccessor (){}
    
    public void saveStudInfo(Student stud){ 
        try {
            //em = getEntityManager();

            em.getTransaction().begin();
            em.persist(stud); // the only interesting method
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            //if (findManufacturer(manufacturer.getManufacturerId()) != null) {
           //     throw new PreexistingEntityException("Manufacturer " + manufacturer + " already exists.", ex);
            //}
            throw ex;
        } finally {
            //if (em != null) {
            //    em.close();
            //}
        }
    }
    
}

/*
 private FEntityManagerFactory emf = null;

    public ManufacturerJpaController() {

        emf = Persistence.createEntityManagerFactory("WithOrWithoutEJBPU");

    }

    public EntityManager getEntityManager() {

        return emf.createEntityManager();

    }
 */