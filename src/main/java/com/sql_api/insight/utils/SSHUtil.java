package com.sql_api.insight.utils;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

public class SSHUtil {
    private ChannelSftp channelSftp;
    private ChannelExec channelExec;
    private Session session = null;
    private int timeout = 600000;
    private String host;

    public SSHUtil(Map<String, Object> conf) throws JSchException {

        JSch jSch = new JSch(); //创建JSch对象
        String username = (String) conf.get("username");
        String host = (String) conf.get("host");
        this.host = host;
        int port = Integer.parseInt((String) conf.get("port"));
        String password = (String) conf.get("password");
        int timeout = Integer.parseInt((String) conf.get("timeout"));
        this.timeout = timeout;
        session = jSch.getSession(username, host, port);//根据用户名，主机ip和端口获取一个Session对象
        session.setPassword(password); //设置密码
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);//为Session对象设置properties
        session.setTimeout(this.timeout);//设置超时
        session.connect();//通过Session建立连接
    }


    public String execute(String shell) {
        String shell_result = "";
        Channel channel = null;
        try {
            StringBuilder sb = new StringBuilder();
            channel = session.openChannel("exec");

            ((ChannelExec) channel).setCommand(shell);

            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = null;

            in = channel.getInputStream();
            channel.connect();
            int c = -1;
            while ((c = in.read()) != -1) {
                sb.append((char) c);
                System.out.println(c);
            }
            System.out.println(sb.toString());
            shell_result = sb.toString();
            channel.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shell_result;

    }

    public String execute_shell(String shell) {


        ChannelShell channel = null;
        try {

            channel = (ChannelShell) session.openChannel("shell");
            InputStream in = channel.getInputStream();
            channel.setPty(true);
            channel.connect();
            OutputStream os = channel.getOutputStream();
            os.write((shell + "\r\nexit\n").getBytes());
            os.flush();

            byte[] tmp = new byte[1024];
            StringBuilder result = new StringBuilder();
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String s = new String(tmp, 0, i);
                    if (s.indexOf("--More--") >= 0) {
                        os.write((" ").getBytes());
                        os.flush();
                    }
                    System.out.print(s);
                    result.append(s);

                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());

                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            os.close();
            in.close();
            channel.disconnect();
            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }


    }

    public void download(String src, String dst) throws JSchException, SftpException {
        //src linux服务器文件地址，dst 本地存放地址
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.get(src, dst);
        channelSftp.quit();
    }

    public void upLoad(String src, String dst) throws JSchException, SftpException {
        //src 本机文件地址。 dst 远程文件地址
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.put(src, dst);
        channelSftp.quit();
    }


    public void close() {
        session.disconnect();
    }

    public String getHost() {
        return host;
    }

//    public static void main(String[] args) {
//        SshConfiguration configuration = new SshConfiguration();
//        configuration.setHost("172.17.1.232");
//        configuration.setUserName("root");
//        configuration.setPassword("root275858");
//        configuration.setPort(22);
//        try {
////            SshUtil sshUtil=new SshUtil(configuration);
////            sshUtil.download("/home/cafintech/Logs/metaData/meta.log","D://meta.log");
////            sshUtil.close();
////            System.out.println("文件下载完成");
//            SshUtil sshUtil = new SshUtil(configuration);
//            sshUtil.upLoad("D://meta.log", "/home/cafintech/");
//            sshUtil.close();
//            System.out.println("文件上传完成");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
