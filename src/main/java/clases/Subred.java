/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author hp
 */
public class Subred {

    private String direccionRed;
    private int prefijo;
    private int totalHosts;

    private String primerHost;
    private String ultimoHost;
    private String broadcast;
    private int nivel;

    public int getNivel() {
        return nivel;
    }

    public String getDireccionRed() {
        return direccionRed;
    }

    public int getPrefijo() {
        return prefijo;
    }

    public int getTotalHosts() {
        return totalHosts;
    }

    public String getPrimerHost() {
        return primerHost;
    }

    public String getUltimoHost() {
        return ultimoHost;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public Subred(String direccionRed, int prefijo, int totalHosts, int nivel) {
        this.direccionRed = direccionRed;
        this.prefijo = prefijo;
        this.totalHosts = totalHosts;
        this.nivel = nivel;
        calcularDetalles();
    }

    private void calcularDetalles() {
        int[] ipRed = IPUtils.convertirIPaArray(direccionRed);
        int[] ipBroadcast = IPUtils.copiarIP(ipRed);
        int[] ipPrimer = IPUtils.copiarIP(ipRed);
        int[] ipUltimo = IPUtils.copiarIP(ipRed);

        ipBroadcast = IPUtils.sumarAIP(ipRed, totalHosts - 1);
        ipPrimer = IPUtils.sumarAIP(ipRed, 1);
        ipUltimo = IPUtils.sumarAIP(ipRed, totalHosts - 2);

        this.broadcast = IPUtils.convertirArrayAIP(ipBroadcast);
        this.primerHost = IPUtils.convertirArrayAIP(ipPrimer);
        this.ultimoHost = IPUtils.convertirArrayAIP(ipUltimo);
    }

}
