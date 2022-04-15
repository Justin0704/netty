package cn.enjoyedu.ch01.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static cn.enjoyedu.ch01.Constants.DEFAULT_PORT;
import static cn.enjoyedu.ch01.Constants.DEFAULT_SERVER_IP;

public class BioClient {

    public static void main(String[] args) throws IOException{
        //通过构造函数创建Socket，并且连接指定地址和端口的服务端
        Socket socket = new Socket(DEFAULT_SERVER_IP,DEFAULT_PORT);
        System.out.println("请输入请求消息：");
        new ReadMsg(socket).start();
        PrintWriter out;
        while(true){
            out = new PrintWriter(socket.getOutputStream());
            out.println(new Scanner(System.in).next());
            out.flush();
        }
    }


    private static class ReadMsg extends  Thread{
        Socket socket;
        public ReadMsg(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while((line = br.readLine()) != null){
                    System.out.printf("%s\n",line);
                }
            }catch (Exception ex){
                System.out.printf("%s\n", "服务器断开了你的连接");
            }finally {
                clear();
            }
        }
        //必要的资源清理工作
        private void clear() {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
