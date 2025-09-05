package com.bharath.springdata.idgenerators;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

// Create our own id generator for field annotated with @Id in @Entity class
public class CustomRandomIDGenerator implements IdentifierGenerator {

	// whatever this method returns will be used as ID
	@Override
	public Serializable generate(SharedSessionContractImplementor si, Object obj) throws HibernateException {
		Random random = new Random();
		int id = random.nextInt(1000000);
		return Long.valueOf(id); // ID field is long. So, returning Long as Hibernate expects Long
	}
}