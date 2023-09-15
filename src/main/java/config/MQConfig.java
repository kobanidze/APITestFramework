package config;

import com.ibm.mq.MQEnvironment;

public class MQConfig {

    private String host;
    private int port;
    private String channel;
    private String manager;
    private String user;
    private String password;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MQConfig() {
    }

    public MQConfig(String host, int port, String channel, String manager, String user, String password) {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.manager = manager;
        this.user = user;
        this.password = password;
    }

    public void configureConnection() {
        // Установка параметров подключения
        MQEnvironment.hostname = host;
        MQEnvironment.port = port;
        MQEnvironment.channel = channel;
        MQEnvironment.CCSID = 1208; // Устанавливаем кодировку UTF-8 (Unicode)
        MQEnvironment.userID = user;
        MQEnvironment.password = password;
    }
}
