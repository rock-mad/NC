import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;

 class Server {
    public static void main(String args[]) throws Exception {
        ServerSocket server = null;
        Socket socket;
        server = new ServerSocket(4000);
        System.out.println("Server Waiting for image");

        while (true) {
            socket = server.accept();
            System.out.println("Client connected.");

            // Receive image from client
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            int len = dis.readInt();
            System.out.println("Image Size: " + len / 1024 + "KB");
            byte[] data = new byte[len];
            dis.readFully(data);

            // Convert byte array to image
            ByteArrayInputStream ian = new ByteArrayInputStream(data);
            BufferedImage bImage = ImageIO.read(ian);

            // Display image in a JFrame (optional)
            JFrame f = new JFrame("Server");
            ImageIcon icon = new ImageIcon(bImage);
            JLabel label = new JLabel(icon);
            f.add(label);
            f.pack();
            f.setVisible(true);

            // Send response to client (optional)
            OutputStream out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(out, true);
            pw.println("Image received successfully.");

            out.close();
            dis.close();
            in.close();
            socket.close();
        }
    }
}
