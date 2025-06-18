/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clases.Subred;
import clases.IPUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
@WebServlet(urlPatterns = {"/ExportarPDFServlet"})
public class ExportarPDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ipBase = request.getParameter("ipBase");
        String hostsStr = request.getParameter("hosts");

        if (ipBase == null || hostsStr == null || hostsStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan datos para generar el PDF.");
            return;
        }

        List<Integer> hostsPorSubred = new ArrayList<>();
        for (String parte : hostsStr.split(",")) {
            hostsPorSubred.add(Integer.parseInt(parte.trim()));
        }

        List<Subred> subredes = IPUtils.calcularSubredes(ipBase, hostsPorSubred);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=resultado_vlsm.pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Resumen del cálculo VLSM", titleFont));
            document.add(new Paragraph("Dirección IP base: " + ipBase + "\n\n", textFont));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.addCell("Subred");
            table.addCell("Prefijo");
            table.addCell("Primer Host");
            table.addCell("Último Host");
            table.addCell("Broadcast");
            table.addCell("Hosts útiles");

            for (Subred s : subredes) {
                table.addCell(s.getDireccionRed());
                table.addCell("/" + s.getPrefijo());
                table.addCell(s.getPrimerHost());
                table.addCell(s.getUltimoHost());
                table.addCell(s.getBroadcast());
                table.addCell(String.valueOf(s.getTotalHosts() - 2));
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF: " + e.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
