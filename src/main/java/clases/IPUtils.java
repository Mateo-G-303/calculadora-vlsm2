/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author hp
 */
public class IPUtils {

    public static int[] convertirIPaArray(String ip) {
        String[] partes = ip.split("\\.");
        int[] numeros = new int[4];
        for (int i = 0; i < 4; i++) {
            numeros[i] = Integer.parseInt(partes[i]);
        }
        return numeros;
    }

    public static String convertirArrayAIP(int[] ip) {
        return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
    }

    public static int[] copiarIP(int[] ip) {
        int[] copia = new int[4];
        for (int i = 0; i < 4; i++) {
            copia[i] = ip[i];
        }
        return copia;
    }

    public static int[] sumarAIP(int[] ip, int cantidad) {
        int[] nuevaIP = copiarIP(ip);
        nuevaIP[3] += cantidad;

        for (int i = 3; i > 0; i--) {
            if (nuevaIP[i] >= 256) {
                int exceso = nuevaIP[i] / 256;
                nuevaIP[i] = nuevaIP[i] % 256;
                nuevaIP[i - 1] += exceso;
            }
        }

        if (nuevaIP[0] >= 256) {
            nuevaIP[0] = 255;
            nuevaIP[1] = 255;
            nuevaIP[2] = 255;
            nuevaIP[3] = 255;
        }

        return nuevaIP;
    }

    public static int calcularHosts(int bits) {
        int resultado = 1;
        for (int i = 0; i < bits; i++) {
            resultado *= 2;
        }
        return resultado;
    }

    public static int bitsNecesarios(int hosts) {
        int bits = 0;
        int total = 0;

        while (total < hosts + 2) {
            bits++;
            total = calcularHosts(bits);
        }

        return bits;
    }

    public static List<Subred> calcularSubredes(String ipBase, List<Integer> hostsPorSubred) {
        List<Subred> subredes = new ArrayList<>();
        Collections.sort(hostsPorSubred, Collections.reverseOrder());

        int[] ipActual = convertirIPaArray(ipBase);
        int nivel = 1;

        for (int i = 0; i < hostsPorSubred.size(); i++) {
            int hostsNecesarios = hostsPorSubred.get(i);
            int bits = bitsNecesarios(hostsNecesarios);
            int totalHosts = calcularHosts(bits);
            int nuevoPrefijo = 32 - bits;

            String ipSubred = convertirArrayAIP(ipActual);
            Subred s = new Subred(ipSubred, nuevoPrefijo, totalHosts, nivel);
            subredes.add(s);

            ipActual = sumarAIP(ipActual, totalHosts);
        }

        return subredes;
    }

    public static void dividirYMostrar(
            String ip, int prefijo, int nivel,
            List<Integer> necesidades,
            Document doc, Font font,
            int[] contadorSubred
    ) throws DocumentException {

        doc.add(new Paragraph("Debug: " + ip + "/" + prefijo, font));

        if (prefijo > 30) {
            return;
        }

        int capacidad = IPUtils.calcularHosts(32 - prefijo);
        String linea = " ".repeat(nivel * 2) + ip + "/" + prefijo;

        if (!necesidades.isEmpty() && capacidad == necesidades.get(0) + 2) {
            linea += " ==> Subred " + contadorSubred[0]++;
            necesidades.remove(0);
            doc.add(new Paragraph(linea, font));
            return;
        }

        doc.add(new Paragraph(linea, font));

        if (necesidades.isEmpty()) {
            return;
        }

        int nuevoPrefijo = prefijo + 1;
        int salto = IPUtils.calcularHosts(32 - nuevoPrefijo);

        int[] ipArr1 = IPUtils.convertirIPaArray(ip);
        int[] ipArr2 = IPUtils.sumarAIP(ipArr1, salto);

        String ip1 = IPUtils.convertirArrayAIP(ipArr1);
        String ip2 = IPUtils.convertirArrayAIP(ipArr2);

        dividirYMostrar(ip1, nuevoPrefijo, nivel + 1, necesidades, doc, font, contadorSubred);
        dividirYMostrar(ip2, nuevoPrefijo, nivel + 1, necesidades, doc, font, contadorSubred);
    }

}
