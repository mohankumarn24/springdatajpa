package com.bharath.springdata.idgenerators;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

// Create our own id generator for field annotated with @Id in @Entity class
public class CustomRandomIDGenerator implements IdentifierGenerator {

	// whatever thos method returns will be used as ID
	@Override
	public Serializable generate(SharedSessionContractImplementor si, Object obj) throws HibernateException {
		Random random = null;
		int id = 0;
		random = new Random();
		id = random.nextInt(1000000);
		return new Long(id); // ID field is long. So, returning Long as Hibernate expects Long
	}

}
