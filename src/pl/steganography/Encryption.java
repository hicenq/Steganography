package pl.steganography;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by Adam on 2016-12-01.
 */

public class Encryption {


    public static void encrypt(String file, String text_file) throws IOException {
        byte [] byteImage=openFile(file);
        String text=openText(text_file);
        String [] StringImage=getStringImage(byteImage);
        byte [] byteText=text.getBytes(StandardCharsets.US_ASCII);
        String [] StringText=getStringImage(byteText);

        if(!calculate(StringImage.length, StringText.length)){
            System.out.println("Za duży rozmiar teksu do zapisania na danym obrazie.");
            return;
        }

        String [] StringImage_enc=addText(StringImage, StringText);

        byte [] byteImage2=getByteImage(StringImage_enc);
        saveImage( byteImage2 ,file);
    }




    //Otwieranie obrazu i zamiana na byte[]
    private static byte[] openFile(String file) throws IOException {
        RandomAccessFile f=new RandomAccessFile( file, "r");
        byte [] b=new byte[(int)f.length()];
        f.readFully(b);
        return b;
    }

    //Konwertownie obrazu na tablice bitów
    private static String[] getStringImage(byte[] b ) {
        int [] b2=byteArrayToIntArray(b);
        String [] b3=new String[b2.length];
        int i=0;
        for(int a: b2 ) {
            String tmp=Integer.toBinaryString(a);
            while(tmp.length()!=8){
                tmp="0".concat(tmp);

            }
            b3[i] = tmp;
            i++;
        }

        return b3;
    }

    //Zamiana byte na int
    public static int[] byteArrayToIntArray(byte[] barray) {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }

    //Otwieranie pliku tekstowego
    private static String openText(String file) throws FileNotFoundException {
        String text = "Tekst zamienny";
        try (BufferedReader br = new BufferedReader(new FileReader(file + ".txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }


    //Dodawanie tekstu do obrazu
    private static String [] addText(String [] image, String [] text) {
        int txt_len=text.length;

        //długość tekstu w zapisanie binarnym
        String txt_len_bin=Integer.toBinaryString(txt_len);
        while(txt_len_bin.length()!=24){
            txt_len_bin="0".concat(txt_len_bin);
        }
        //zapisaywanie sługości tekstu
        int j=0;
        for (int i=56;i<126;i+=3){
            if(image[i].charAt(7)==txt_len_bin.charAt(j)){
                j++;
            }else{
                image[i]=image[i].substring(0,7);
                image[i] += txt_len_bin.charAt(j);
                j++;
            }

        }
        //zapisywanie głównego tekstu

        for(int i=128; i<txt_len*8+128;i+=3){
            for(int k=0; k<8; k++ ) {
                if (image[i].charAt(7) == txt_len_bin.charAt(k)) {
                    k++;
                } else {
                    image[i] = image[i].substring(0, 7);
                    image[i] += txt_len_bin.charAt(k);
                    j++;
                }
            }
        }
        return image;
    }

    //Konwertowanie String na byte
    private static byte[] getByteImage(String [] b3) {
        int bin;
        int i=0;
        byte [] b=new byte[b3.length];
        for (String s: b3){
            bin=Integer.valueOf(b3[i], 2);
            b[i]=(byte)bin;
            i++;
        }
        return b;
    }





    private static void saveImage(byte[] b , String filename ) throws IOException {
        FileOutputStream fos = new FileOutputStream("encrypted_"+ filename );
        fos.write(b);
        fos.close();
    }


    private static boolean calculate(int img, int text) {
        img=img-53-24;//odejmujemy offset i liczbe na zapisanie długości tekstu
        img=img/3;
        text =text*8;
        if(img>text) {
            return true;
        }else return false;

    }


}
