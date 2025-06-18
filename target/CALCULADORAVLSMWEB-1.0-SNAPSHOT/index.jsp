<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, clases.Subred" %>
<html>
    <head>
        <title>Calculadora VLSM</title>
        <title>Calculadora VLSM</title>
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background: #f4f7fa;
                margin: 0;
                padding: 20px;
                text-align: center;
            }

            h1 {
                color: #333;
            }

            .form-container {
                background: #fff;
                padding: 20px 30px;
                margin: 0 auto 30px;
                border-radius: 10px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                max-width: 500px;
            }

            input[type="text"],
            input[type="number"] {
                width: 90%;
                padding: 10px;
                margin-bottom: 15px;
                border-radius: 6px;
                border: 1px solid #ccc;
            }

            button {
                padding: 10px 20px;
                background-color: #1d72b8;
                border: none;
                border-radius: 6px;
                color: white;
                cursor: pointer;
                font-weight: bold;
                margin: 5px;
            }

            button:hover {
                background-color: #155a96;
            }

            table {
                margin: 20px auto;
                border-collapse: collapse;
                width: 90%;
                background: white;
                box-shadow: 0 0 10px rgba(0,0,0,0.05);
            }

            th, td {
                padding: 12px;
                border: 1px solid #ddd;
            }

            th {
                background-color: #1d72b8;
                color: white;
            }

            .buttons {
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <h2>Calculadora VLSM</h2>

        <form action="VLSMServlet" method="post">
            IP base: <input type="text" name="ipBase" required><br>
            Prefijo: <input type="text" name="prefijoBase" required><br>
            <label for="hosts">Cantidad de hosts por subred (separados por comas):</label><br>
            <input type="text" name="hosts" placeholder="Ej: 100,50,25" required><br><br>
            <button type="submit">Calcular</button>
        </form>

        <hr>

        <%
            List<Subred> subredes = (List<Subred>) request.getAttribute("subredes");
            if (subredes != null && !subredes.isEmpty()) {
        %>
        <h3>Resultado:</h3>
        <table border="1">
            <tr>
                <th>Subred</th>
                <th>Prefijo</th>
                <th>Primer Host</th>
                <th>Último Host</th>
                <th>Broadcast</th>
                <th>Hosts útiles</th>
            </tr>
            <%
                for (Subred s : subredes) {
            %>
            <tr>
                <td><%= s.getDireccionRed()%></td>
                <td><%= s.getPrefijo()%></td>
                <td><%= s.getPrimerHost()%></td>
                <td><%= s.getUltimoHost()%></td>
                <td><%= s.getBroadcast()%></td>
                <td><%= s.getTotalHosts() - 2%></td>
            </tr>
            <%
                }
            %>
        </table>
        <%
            }
        %>

        <%
            if (subredes != null && !subredes.isEmpty()) {
                StringBuilder hostsComas = new StringBuilder();
                for (int i = 0; i < subredes.size(); i++) {
                    hostsComas.append(subredes.get(i).getTotalHosts() - 2);
                    if (i < subredes.size() - 1) {
                        hostsComas.append(",");
                    }
                }
        %>
        <form action="ExportarPDFServlet" method="post" target="_blank">
            <input type="hidden" name="ipBase" value="<%= request.getParameter("ipBase")%>">
            <input type="hidden" name="hosts" value="<%= hostsComas.toString()%>">
            <button type="submit">Exportar a PDF</button>
        </form>

        <form action="ExportarProcesoServlet" method="post" target="_blank">
            <input type="hidden" name="ipBase" value="<%= request.getParameter("ipBase")%>">
            <input type="hidden" name="prefijoBase" value="<%= request.getParameter("prefijoBase") %>">
            <input type="hidden" name="hosts" value="<%= hostsComas.toString()%>">
            <button type="submit">Exportar a PDF (Proceso)</button>
        </form>
        <%
            }
        %>
    </body>
</html>

