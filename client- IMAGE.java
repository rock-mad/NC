import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class Client {
    public static void main(String args[]) throws Exception {
        Socket soc;
        BufferedImage img = null;
        soc = new Socket("localhost", 4000);
        System.out.println("Client is running.");

        try {
            System.out.println("Reading image from disk.");
            // Adjust the file path here
            img = ImageIO.read(new File("C:/NC/12.jpeg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();
            System.out.println("Sending image to server.");

            OutputStream out = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);
            System.out.println("Image sent to server.");

            // Receive response from server (optional)
            InputStream in = soc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String response = br.readLine();
            System.out.println("Server response: " + response);

            dos.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            soc.close();
        }
    }
}
