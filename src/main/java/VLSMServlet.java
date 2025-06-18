/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import clases.Subred;
import clases.IPUtils;
import java.util.List;

/**
 *
 * @author hp
 */
@WebServlet(urlPatterns = {"/VLSMServlet"})
public class VLSMServlet extends HttpServlet {

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
            response.sendRedirect("index.jsp?error=Faltan+datos");
            return;
        }

        List<Integer> hostsPorSubred = new ArrayList<>();
        for (String parte : hostsStr.split(",")) {
            hostsPorSubred.add(Integer.parseInt(parte.trim()));
        }

        List<Subred> subredes = IPUtils.calcularSubredes(ipBase, hostsPorSubred);

        request.setAttribute("ipBase", ipBase);
        request.setAttribute("hosts", hostsStr);
        request.setAttribute("subredes", subredes);

        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
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
