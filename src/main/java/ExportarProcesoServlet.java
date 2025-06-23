/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import clases.IPUtils;
import static clases.IPUtils.calcularHosts;
import static clases.IPUtils.convertirArrayAIP;
import static clases.IPUtils.convertirIPaArray;
import static clases.IPUtils.sumarAIP;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hp
 */
@WebServlet(urlPatterns = {"/ExportarProcesoServlet"})
public class ExportarProcesoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ipBase = request.getParameter("ipBase");
        String hostsStr = request.getParameter("hosts");
        String prefijoStr = request.getParameter("prefijoBase");

        if (ipBase == null || hostsStr == null || prefijoStr == null || hostsStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan datos para exportar.");
            return;
        }

        int prefijoBase = Integer.parseInt(prefijoStr);

        ArrayList<Integer> necesidades = new ArrayList<>();
        for (String h : hostsStr.split(",")) {
            necesidades.add(Integer.parseInt(h.trim()));
        }
        necesidades.sort(Collections.reverseOrder());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=proceso_vlsm.pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font font = FontFactory.getFont(FontFactory.COURIER, 12);

            document.add(new Paragraph("Proceso jerárquico VLSM (bit a bit)", titleFont));
            document.add(new Paragraph("IP base: " + ipBase + "/" + prefijoBase + "\n", font));

            int[] contadorSubred = {1};
            IPUtils.dividirYMostrar(ipBase, prefijoBase, 0, necesidades, document, font, contadorSubred);

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar PDF: " + e.getMessage());
        }
    }

    public static void dividirYMostrar(
            String ip, int prefijo, int nivel,
            ArrayList<Integer> necesidades,
            Document doc, Font font,
            int[] contadorSubred
    ) throws DocumentException {
        if (prefijo > 30) {
            return;
        }

        int capacidad = calcularHosts(32 - prefijo);
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
        int salto = calcularHosts(32 - nuevoPrefijo);

        int[] ipArr1 = convertirIPaArray(ip);
        int[] ipArr2 = sumarAIP(ipArr1, salto);

        String ip1 = convertirArrayAIP(ipArr1);
        String ip2 = convertirArrayAIP(ipArr2);

        dividirYMostrar(ip1, nuevoPrefijo, nivel + 1, necesidades, doc, font, contadorSubred);
        dividirYMostrar(ip2, nuevoPrefijo, nivel + 1, necesidades, doc, font, contadorSubred);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
