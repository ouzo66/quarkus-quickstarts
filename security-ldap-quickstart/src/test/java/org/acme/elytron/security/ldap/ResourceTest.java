package org.acme.elytron.security.ldap;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.keycloak.util.ldap.LDAPEmbeddedServer;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ResourceTest {
	
	
	private LDAPEmbeddedServer ldap = null;

	@BeforeClass
	void init() throws Exception {
		
		Properties defaultProperties = new Properties();
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_DSF, LDAPEmbeddedServer.DSF_INMEMORY);
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_LDIF_FILE, "classpath:ldap-example-users.ldif");
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_BIND_HOST, "localhost");
        defaultProperties.setProperty(LDAPEmbeddedServer.PROPERTY_BASE_DN, "o=YourCompany,dc=de");
       
        LDAPEmbeddedServer ldapEmbeddedServer = new LDAPEmbeddedServer(defaultProperties);
		ldapEmbeddedServer.init();
        ldapEmbeddedServer.start();
        ldap = ldapEmbeddedServer;
        
	}
	
	@Test
	void testAdmin() {
		given().auth().basic("admin", "adminPassword")
        .when().get("/api/admin")
        .then()
           .statusCode(200)
           .body(is("admin"));
	}
	
	@AfterClass
	void end() throws Exception {
		if (ldap != null) {
			ldap.stop();
			ldap = null;
		}
	}
	

}
