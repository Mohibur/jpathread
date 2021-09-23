package simple.mind.jpathread.configs;

public class Config {

	public String getDriver() {
		return "org.mariadb.jdbc.Driver";
	}

	public String getUrl() {
		return "jdbc:mariadb://192.168.56.22:3306/stackexchange?autoReconnect=true";
	}

	public String getUser() {
		return "johny";
	}

	public String getPass() {
		return "1";
	}

	public String getMaxPoolSize() {
		return "100";
	}

	public String getAutoDdl() {
		return "none";
	}

	public String getDBDialect() {
		return "org.hibernate.dialect.MariaDBDialect";
	}

	public Boolean showSQL() {
		return false;
	}

}
