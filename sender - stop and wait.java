
import java.io.*;
import java.net.*;

class Sender {
    private Socket sender;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String packet;
    private int sequence = 0;

    public Sender() {}

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Waiting for Connection....");
            sender = new Socket("localhost", 2004);
            setupStreams();
            System.out.println("Connected to receiver.");
            packet = getInput(br, "Enter the data to send....");
            sendData();
            System.out.println("All data sent. Exiting.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void setupStreams() throws IOException, ClassNotFoundException {
        out = new ObjectOutputStream(sender.getOutputStream());
        out.flush();
        in = new ObjectInputStream(sender.getInputStream());
        try {
            System.out.println("Receiver: " + (String) in.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getInput(BufferedReader br, String message) throws IOException {
        System.out.println(message);
        return br.readLine();
    }

    private void sendData() throws IOException, ClassNotFoundException {
        int i = 0;
        int n = packet.length();
        while (i < n) {
            String msg = String.valueOf(sequence);
            msg += packet.charAt(i++);
            out.writeObject(msg);
            out.flush();
            System.out.println("Data sent: " + msg);
            String ack = (String) in.readObject();
            if (ack.equals(String.valueOf(sequence))) {
                System.out.println("Receiver: Packet received.");
                sequence ^= 1;
            } else {
                System.out.println("Timeout: Resending data.");
            }
        }
        out.writeObject("end");
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (sender != null) sender.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        new Sender().run();
    }
}

