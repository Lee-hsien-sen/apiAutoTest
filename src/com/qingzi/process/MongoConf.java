package com.qingzi.process;

public class MongoConf {
	 public String host;
     public int port;
     public String authDB;
     public String username;
     public String password;

     public MongoConf() {
     }

     public MongoConf(String host, int port, String authDB, String username, String password) {
         this.host = host;
         this.port = port;
         this.authDB = authDB;
         this.username = username;
         this.password = password;
     }

     public String getHost() {
         return host;
     }

     public void setHost(String host) {
         this.host = host;
     }

     public int getPort() {
         return port;
     }

     public void setPort(int port) {
         this.port = port;
     }

     public String getAuthDB() {
         return authDB;
     }

     public void setAuthDB(String authDB) {
         this.authDB = authDB;
     }

     public String getUsername() {
         return username;
     }

     public void setUsername(String username) {
         this.username = username;
     }

     public String getPassword() {
         return password;
     }

     public void setPassword(String password) {
         this.password = password;
     }
}
