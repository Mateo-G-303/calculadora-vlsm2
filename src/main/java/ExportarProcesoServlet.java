/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import clases.Subred;
import clases.IPUtils;

/**
 *
 * @author hp
 */
@WebServlet(urlPatterns = {"/ExportarProcesoServlet"})
public class ExportarProcesoServlet extends HttpServlet {
//Esta es una prueba para verificar si el conmit funcionó
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

        // Convertir lista de hosts en ArrayList
        ArrayList<Integer> necesidades = new ArrayList<>();
        for (String h : hostsStr.split(",")) {
            necesidades.add(Integer.parseInt(h.trim()));
        }
        necesidades.sort(Collections.reverseOrder());

        // Configuración del PDF
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

            // Llamar a la función de IPUtils (contador como array)
            int[] contadorSubred = {1};
            IPUtils.dividirYMostrar(ipBase, prefijoBase, 0, necesidades, document, font, contadorSubred);

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar PDF: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
