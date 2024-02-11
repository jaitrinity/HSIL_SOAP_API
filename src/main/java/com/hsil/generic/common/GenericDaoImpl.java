package com.hsil.generic.common;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class GenericDaoImpl<T> implements GenericDao<T>{
  
	
	  @PersistenceContext
      private EntityManager em;
      private Class<T> persistentClass;
  
	  public Class<T> getPersistentClass()
	  {
	    return this.persistentClass;
	  }
	  
	  public void setPersistentClass(Class<T> persistentClass)
	  {
	    this.persistentClass = persistentClass;
	  }
	  
	  public EntityManager getEm(){
	    return this.em;
	  }
	  
	  public void setEm(EntityManager em){
	    this.em = em;
	  }

	  @Transactional
	  public T save(T paramType){
	    paramType = this.em.merge(paramType);
	    return paramType;
	  }
	  
	  @Transactional
	  public T update(T paramType){
	    paramType = this.em.merge(paramType);
	    return paramType;
	  }
	  
	  public T findById(long id){
	    T result = this.em.find(this.persistentClass, Long.valueOf(id));
	    return result;
	  }
	  
	  public T findById(Serializable id){
	    T result = this.em.find(this.persistentClass, id);
	    return result;
	  }
	  
	  @Transactional
	  public List<T> saveAll(List<T> entityList){
	    for (T entity : entityList) {
	      entity = this.em.merge(entity);
	    }
	    return entityList;
	  }
  
}
