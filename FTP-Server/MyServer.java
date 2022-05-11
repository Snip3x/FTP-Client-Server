import java.net.*;
import java.util.*;
import java.io.File;

public class MyServer {
    
    private ArrayList<Socket> list = new ArrayList<Socket>();
    public List<String> files = new ArrayList<String>(); 

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(3000);
            System.out.println("Server is running on port: 3000");
            MyServer ms = new MyServer();
            ms.filelist();
            while (true) {

                Socket s = ss.accept();// establishes connection
                ms.list.add(s);
                Thread t = new Thread(new MultiServer(s,ms));
                t.start();

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void filelist(){
        File folder = new File("/home/propane/FTP");
        File[] listOfFiles = folder.listFiles();
        List<String> file = new ArrayList<String>(); 
        for (int i = 0; i < listOfFiles.length; i++) {
             
            if (listOfFiles[i].isFile()) {
                 file.add(listOfFiles[i].getName() + "/"+((listOfFiles[i].length()/1024)+ "KB"));
            } 
            
        }
        files = file;

    }



}
