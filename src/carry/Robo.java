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

    private final double raioRobo;
    private final double raioRoda;
    private double orientacao;
    private double angularLeft;
    private double angularRight;
    private double linear;
    private double angular;
    private Posicao posicao;

    public Robo(double raioRobo, double raioRoda) {
        this.raioRobo = raioRobo;
        this.raioRoda = raioRoda;
    }

    public double getRaioRobo() {
        return raioRobo;
    }

    public double getRaioRoda() {
        return raioRoda;
    }

    public double getOrientacao() {
        return orientacao;
    }

    public void setOrientacao() {
        this.orientacao = 0; //obter orientação do robô pela visão computacional
    }

    public double getAngularLeft() {
        return angularLeft;
    }

    public void setAngularLeft(double angularLeft) {
        this.angularLeft = angularLeft;
    }

    public double getAngularRight() {
        return angularRight;
    }

    public void setAngularRight(double angularRight) {
        this.angularRight = angularRight;
    }

    public double getLinear() {
        return linear;
    }

    public void setLinear() {
        this.linear = this.raioRoda * (this.angularLeft + this.angularRight) / 2;
    }

    public double getAngular() {
        return angular;
    }

    public void setAngular() {
        this.angular = this.raioRoda * (this.angularRight - this.angularLeft) / (2 * this.raioRobo);
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao() {
        this.posicao = new Posicao(0, 0); //obter posição do robô pela visão computacional
    }

    public void atualiza() {
        setOrientacao();
        setPosicao();
        setLinear();
        setAngular();
    }

    public void controlador(Posicao objetivo, double orientacao) {
        double dx = objetivo.getX() - posicao.getX();
        double dy = objetivo.getY() - posicao.getY();
        double p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        while (p > 0 || Math.abs(orientacao - this.orientacao) > 0) {
            dx = objetivo.getX() - posicao.getX();
            dy = objetivo.getY() - posicao.getY();
            p = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

            double gama = Math.atan(dy / dx);
            double alpha = gama - this.orientacao;
            double beta = orientacao - gama;

            if (alpha < Math.PI / 2 * -1 || alpha > Math.PI / 2) {
                p *= -1;
                alpha += Math.PI;
                beta += Math.PI;

                alpha %= 2 * Math.PI;
                if (alpha > Math.PI) {
                    alpha -= 2 * Math.PI;
                }

                beta %= 2 * Math.PI;
                if (beta > Math.PI) {
                    beta -= 2 * Math.PI;
                }
            }

            atualiza();
        }
    }
}
