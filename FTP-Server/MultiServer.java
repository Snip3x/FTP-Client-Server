import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultiServer implements Runnable {
 public Socket s;
 private MyServer ser;
    public MultiServer(Socket s, MyServer ser){
        this.s = s;
        this.ser = ser;
    }
    public void run() {
        try{
            DataInputStream iout = new DataInputStream(s.getInputStream());
            String key = iout.readUTF();
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            if(key.equals("123")){
                dout.writeUTF("OK");
                dout.flush();
                updateFiles(dout);
                while(true){

                //main flow here
                int choice = iout.readInt();
                if(choice==1){
                    //upload
                    String fileName = iout.readUTF();
                    fileName.split("<->");
                    saveFile(s, fileName.split("<->")[0],Integer.parseInt(fileName.split("<->")[1]));
                    dout.writeBoolean(true);
                    updateFiles(dout);

                }else if(choice == 2){
                    //download
                    String fileName = iout.readUTF();
                    File f = new File("/home/propane/FTP/"+fileName);
                    System.out.println(f.length());
                    dout.writeUTF(f.length()+"");
                    dout.flush();
                    sendFile(fileName,dout);
                    
                }else{
                dout.flush();
                dout.close();
                iout.close();
                break;
                }






                }

            }else{
                dout.writeUTF("NO");
                dout.flush();
                iout.close();
            }
            
            // MyServer.threads -= MyServer.threads;
        }catch(Exception e){

        }
    }

    private void updateFiles(DataOutputStream dout) throws IOException {
        int len =ser.files.size();
                dout.writeInt(len);
                for(int i =0; i<len; i++){
                    dout.writeUTF(ser.files.get(i));
                    dout.flush();

                }
    }

        //Recieve File from Client
    private void saveFile(Socket s, String fileName, int filesize) throws IOException {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        FileOutputStream fos = new FileOutputStream("/home/propane/FTP/"+fileName);
        System.out.println(fileName+" "+filesize);
        // Send file size in separate msg
        int read = 0;
        System.out.println("waiting for file");
        byte[] buffer = new byte[8192];
        while (filesize > 0 && (read = dis.read(buffer, 0, (int)Math.min(buffer.length, filesize))) != -1)
        {
            fos.write(buffer,0,read);
            filesize -= read;
            }
        System.out.println(fileName+" uploaded");        
        ser.filelist();
    }
        //upload to Client
    public void sendFile(String file, DataOutputStream dos) throws IOException {
        File fi = new File(file);

        System.out.println(fi.length()+file);
        
        FileInputStream fis = new FileInputStream("/home/propane/FTP/"+file);
        byte[] buffer = new byte[8192];
        int count;
        while ((count = fis.read(buffer)) > 0) {
            dos.write(buffer, 0, count);
            
            
        }
       
        dos.flush();
        
        

          
    }
    


}