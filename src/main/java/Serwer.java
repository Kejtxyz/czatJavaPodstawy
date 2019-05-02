import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Serwer {

    ArrayList klientArrayList;
    PrintWriter printWriter;
  
    public static void main(String[] args) {
        // uruchomienie serwera
        Serwer s = new Serwer();
        s.startSerwer();

    }

    // przechwytywanie wyjatkow gdy jakis port, albo host bedzie nie dostepny
    // START SERWERA - GLOWNA METODA
    public void startSerwer(){
        klientArrayList = new ArrayList();

        try {//nasluchiwanie serwera -server socet
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("slucha serwera na konkretnym porcie i ip" + serverSocket);
                printWriter = new PrintWriter(socket.getOutputStream());  // lista klientow ktorzy odbieraja komunikat do klientow
                klientArrayList.add(printWriter);

                Thread t = new Thread(new SerwerKlient(socket));
                t.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // KLASA WEWNETRZNA
    class SerwerKlient implements Runnable{

            Socket socket;
            BufferedReader bufferedReader;
            // konstruktor
            public SerwerKlient(Socket socketKlient) {
                try {
                    System.out.println("Polaczonoy klient");
                    socket = socketKlient;
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // odczyt wejscia
                } catch (Exception ex){
                    ex.printStackTrace();
            }}
                @Override
                public void run() {
                    String str = null;
                    PrintWriter pw = null;  // rozsylania watkow, na poczatku musi byc pusty
                    try {
                        while (bufferedReader.readLine() != null){
                            System.out.println("Odebrano>>" );

                            Iterator it = klientArrayList.iterator();
                            while (it.hasNext()){
                                pw = (PrintWriter) it.next();
                                pw.println(str);
                                pw.flush();  // oproznia strumien
                            }
                        }
                    }catch (Exception e){

                    }
            }
        }
    }


// serwer nasluchuje tego co przychodzi od klienta
// drogi serwer wysyla to co przyszlo od klienta