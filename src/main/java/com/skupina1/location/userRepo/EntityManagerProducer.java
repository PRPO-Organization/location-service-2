package com.skupina1.location.userRepo;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class EntityManagerProducer {

    // private static final EntityManagerFactory emf =
    //         Persistence.createEntityManagerFactory("postgisPU");

    // @Produces
    // @RequestScoped
    // @MyEM
    // public EntityManager createEntityManager() {
    //     return emf.createEntityManager();
    // }

    // // Add cleanup method if needed
    // public void close() {
    //     if (emf != null && emf.isOpen()) {
    //         emf.close();
    //     }
    // }
}