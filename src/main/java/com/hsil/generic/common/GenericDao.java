package com.hsil.generic.common;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T> {
		
	  public T findById(long paramLong);
	  public T findById(Serializable paramSerializable);
	  public T save(T paramT);	  
	  public T update(T paramT);
	  public List<T> saveAll(List<T> paramList);
}
