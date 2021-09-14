package com.qingzi.system;

public class system {
	private String access_token_uri; 
	private String client_id; 
	private String client_secret; 
	private String grant_type; 
	private String isProduct; 
	private String RM_URI; 
	private String RM_port;
	private String RM_basePath;
	private String redis_URI; 
	private String redis_db_index; 
	private String sqlurl; 
	private String sqlname; 
	private String sqlpwd;
	private String mongoUrl;
	private String mongoName;
	private String mongoPwd;
	private String authDb;
	public String getMongoUrl() {
		return mongoUrl;
	}
	public void setMongoUrl(String mongoUrl) {
		this.mongoUrl = mongoUrl;
	}
	public String getMongoName() {
		return mongoName;
	}
	public void setMongoName(String mongoName) {
		this.mongoName = mongoName;
	}
	public String getMongoPwd() {
		return mongoPwd;
	}
	public void setMongoPwd(String mongoPwd) {
		this.mongoPwd = mongoPwd;
	}
	public String getAuthDb() {
		return authDb;
	}
	public void setAuthDb(String authDb) {
		this.authDb = authDb;
	}
	public String getAccess_token_uri() {
		return access_token_uri;
	}
	public void setAccess_token_uri(String access_token_uri) {
		this.access_token_uri = access_token_uri;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getClient_secret() {
		return client_secret;
	}
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	public String getGrant_type() {
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public String getIsProduct() {
		return isProduct;
	}
	public void setIsProduct(String isProduct) {
		this.isProduct = isProduct;
	}
	public String getRM_URI() {
		return RM_URI;
	}
	public void setRM_URI(String rM_URI) {
		RM_URI = rM_URI;
	}
	public String getRM_port() {
		return RM_port;
	}
	public void setRM_port(String rM_port) {
		RM_port = rM_port;
	}
	public String getRM_basePath() {
		return RM_basePath;
	}
	public void setRM_basePath(String rM_basePath) {
		RM_basePath = rM_basePath;
	}
	public String getRedis_URI() {
		return redis_URI;
	}
	public void setRedis_URI(String redis_URI) {
		this.redis_URI = redis_URI;
	}
	public String getRedis_db_index() {
		return redis_db_index;
	}
	public void setRedis_db_index(String redis_db_index) {
		this.redis_db_index = redis_db_index;
	}
	public String getSqlurl() {
		return sqlurl;
	}
	public void setSqlurl(String sqlurl) {
		this.sqlurl = sqlurl;
	}
	public String getSqlname() {
		return sqlname;
	}
	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}
	public String getSqlpwd() {
		return sqlpwd;
	}
	public void setSqlpwd(String sqlpwd) {
		this.sqlpwd = sqlpwd;
	}
	
	public system(String access_token_uri, String client_id,
			String client_secret, String grant_type, String isProduct,
			String rM_URI, String rM_port, String rM_basePath,
			String redis_URI, String redis_db_index, String sqlurl,
			String sqlname, String sqlpwd, String mongoName, String mongoPwd,
			String authDb,String mongoUrl) {
		super();
		this.access_token_uri = access_token_uri;
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.grant_type = grant_type;
		this.isProduct = isProduct;
		RM_URI = rM_URI;
		RM_port = rM_port;
		RM_basePath = rM_basePath;
		this.redis_URI = redis_URI;
		this.redis_db_index = redis_db_index;
		this.sqlurl = sqlurl;
		this.sqlname = sqlname;
		this.sqlpwd = sqlpwd;
		this.mongoName = mongoName;
		this.mongoPwd = mongoPwd;
		this.authDb = authDb;
		this.mongoUrl = mongoUrl;
	}
	public system() {
		super();
	} 
	

}
