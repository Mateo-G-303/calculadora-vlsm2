/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import clases.IPUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;
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
        OutputStream out = null;

        try {
            out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font font = FontFactory.getFont(FontFactory.COURIER, 12);

            document.add(new Paragraph("Proceso VLSM con método de bit a bit", titleFont));
            document.add(new Paragraph("IP base: " + ipBase + "/" + prefijoBase + "\n", font));
            document.add(new Paragraph("Llamando a dividirYMostrar con IP: " + ipBase + "/" + prefijoBase, font));
            document.add(new Paragraph("Hosts: " + necesidades.toString(), font));
            int[] contadorSubred = {1};
            
            IPUtils.dividirYMostrar(ipBase, prefijoBase, 0, necesidades, document, font, contadorSubred);

        } catch (DocumentException e) {
            throw new IOException("Error al generar PDF: " + e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
