/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carry;

/**
 *
 * @author Usuário
 */
public class Robo {

    private final double l;
    private final double r;
    private double o;
    private double wLeft;
    private double wRight;
    private double v;
    private double w;
    private Posicao posicao;

    public Robo(double l, double r) {
        this.l = l;
        this.r = r;
    }

    public double getL() {
        return l;
    }

    public double getR() {
        return r;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getwLeft() {
        return wLeft;
    }

    public void setwLeft(double wLeft) {
        this.wLeft = wLeft;
    }

    public double getwRight() {
        return wRight;
    }

    public void setwRight(double wRight) {
        this.wRight = wRight;
    }

    public double getV() {
        return v;
    }

    public void setV() {
        this.v = this.r * (this.wLeft + this.wRight) / 2;
    }

    public double getW() {
        return w;
    }

    public void setW() {
        this.w = this.r * (this.wRight - this.wLeft) / (2 * this.l);
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public void atualiza() {
        setV();
        setW();
    }

    public void controlador(Posicao objetivo, double orientacao) {
        double dx = objetivo.getX() - posicao.getX();
        double dy = objetivo.getY() - posicao.getY();
        double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        while (p > 0 || Math.abs(o - orientacao) > 0) {
            double gama = Math.atan(dy / dx);
            double alpha = gama - o;
            double beta = orientacao - gama;
        }
    }
}
