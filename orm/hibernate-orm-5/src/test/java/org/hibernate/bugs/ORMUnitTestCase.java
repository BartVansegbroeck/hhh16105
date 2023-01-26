/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs;

import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.test.Person;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 *
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Person.class
//				Foo.class,
//				Bar.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh16105Test() throws Exception {
		// BaseCoreFunctionalTestCase automatically creates the SessionFactory and provides the Session.
		Session session = openSession();
		Transaction tx = session.beginTransaction();
		Person person = new Person(1, "Sherlock", "Holmes", "Surely");
		session.persist(person);
		tx.commit();
		session.close();

		// Open session/transaction and set a single property, Entity becomes dirty and is persisted
		session = openSession();
		tx = session.beginTransaction();
		Person personLoaded = session.get(Person.class, 1);
		personLoaded.setLastName("Watson");
		tx.commit();
		session.close();

		// Verify entity was persisted correctly
		session = openSession();
		tx = session.beginTransaction();
		Person personReloaded = session.get(Person.class, 1);
		Assertions.assertThat(personReloaded.getLastName()).isEqualTo("Watson");
		tx.commit();
		session.close();

		// Open session/transaction and update a property in a hashmap with an AttributeConverter, Entity should become dirty and be persisted
		session = openSession();
		tx = session.beginTransaction();
		Person personReloaded2 = session.get(Person.class, 1);
		personReloaded2.getAttributes().put(Person.NICKNAME, "evil");
		tx.commit();
		session.close();

		// Verify entity was persisted correctly, but hashmap was not detected as dirty
		session = openSession();
		tx = session.beginTransaction();
		Person personReloaded3 = session.get(Person.class, 1);
		Assertions.assertThat(personReloaded3.getAttributes().get(Person.NICKNAME)).isEqualTo("evil");
		tx.commit();
		session.close();
	}
}
