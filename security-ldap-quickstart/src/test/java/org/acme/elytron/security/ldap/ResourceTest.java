package org.acme.elytron.security.ldap;

import java.util.Properties;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.keycloak.util.ldap.LDAPEmbeddedServer;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ResourceTest {
	
	private static final Logger LOG = Logger.getLogger(ResourceTest.class);

	
	private static LDAPEmbeddedServer ldap = null;

	@BeforeAll
	static void init() throws Exception {
		LOG.info("Create LDAP Server.");
		
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
	void testPublic() {
		LOG.info("Test Public API.");
		given()
        .when().get("/api/public")
        .then()
           .statusCode(200)
           .body(is("public"));
	}
	
	@Test
	void testAdmin() {
		LOG.info("Test Admin API.");
		given().auth().basic("adminUser", "adminPassword")
        .when().get("/api/admin")
        .then()
           .statusCode(200)
           .body(is("admin"));
	}
	
	@Test
	void testUserForbidden() {
		LOG.info("Test User API with wrong credentials.");
		given().auth().basic("adminUser", "adminPassword")
        .when().get("/api/users/me")
        .then()
           .statusCode(403)
           .body(is(""));
	}
	
	@Test
	void testUser() {
		LOG.info("Test User API.");
		given().auth().basic("standardUser", "standardPassword")
        .when().get("/api/users/me")
        .then()
           .statusCode(200)
           .body(is("standardUser"));
	}
	
	@AfterAll
	static void end() throws Exception {
		LOG.info("Stopping LDAP Server.");
		if (ldap != null) {
			ldap.stop();
			ldap = null;
		}
	}
	

}
