
import java.util.*;

/**
 * Classe responsavel pela execucao de um AG auxiliar na resolucao do PCV
 *
 * @author Gabriel Casemiro Barbosa
 * @since 2018
 */
public class AlgoritmoGenetico {

    private final static int GENES = 6;
    private final static int LIMITEMINIMO = 0;
    private final static int LIMITEMAXIMO = GENES - 1;
    private final static int INDIVIDUOS = 100;
    private final static int GERACOES = 50;
    private final static int TAXAMUTACAO = 8;
    private final static double CUSTO[][] = new double[GENES][GENES];
    private final static Random SORTEIA = new Random();

    private static class Coordenada {

        private final static Random SORTEIA = new Random(0);
        private final static int XMAXIMO = 100;
        private final static int YMAXIMO = 100;

        private int ponto; //Chave
        private int x;
        private int y;

        private Coordenada() {

        }

        private Coordenada(int ponto, int x, int y) {
            this.ponto = ponto;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return ponto + "(" + x + ", " + y + ")";
        }

    }

    private static class Individuo {

        private int[] cromossomo;
        private double fitness;

        private Individuo() {
            this.cromossomo = new int[GENES];
        }

        @Override
        public String toString() {
            return "Rota: " + Arrays.toString(cromossomo) + " | Custo: " + fitness;
        }

    }

    private static void defineCusto() {
        //Exemplo com 5 cidades
        Coordenada[] coordenadas = new Coordenada[GENES];

        for (int i = 0; i < coordenadas.length; i++) {
            //Armazenar coordenadas em um BD
            coordenadas[i] = new Coordenada(i + 1, Coordenada.SORTEIA.nextInt(Coordenada.XMAXIMO), Coordenada.SORTEIA.nextInt(Coordenada.YMAXIMO));
        }

        for (int i = 0; i < CUSTO.length; i++) {
            for (int j = 0; j < CUSTO[i].length; j++) {
                //Calculo de distancia
                CUSTO[i][j] = Math.sqrt(Math.pow(coordenadas[i].x - coordenadas[j].x, 2) + Math.pow(coordenadas[i].y - coordenadas[j].y, 2));
            }
        }

        for (int i = 0; i < CUSTO.length; i++) {
            for (int j = 0; j < CUSTO[i].length; j++) {
                System.out.println("C(" + i + ", " + j + ") = " + CUSTO[i][j]);
            }
            System.out.println("");
        }
    }

    private static Individuo[] geraPopulacao() {
        Individuo[] populacaoInicial = new Individuo[INDIVIDUOS];

        for (int i = 0; i < populacaoInicial.length; i++) {
            populacaoInicial[i] = new Individuo();
            populacaoInicial[i].cromossomo = geraCromossomo();
        }

        return populacaoInicial;
    }

    private static int[] geraCromossomo() {
        int[] cromossomo = new int[GENES];

        for (int i = 0; i < cromossomo.length; i++) {
            cromossomo[i] = sorteiaIntervalo(LIMITEMINIMO, LIMITEMAXIMO);
        }

        return cromossomo;
    }

    private static int sorteiaIntervalo(int minimo, int maximo) {
        return SORTEIA.nextInt(maximo) + minimo;
    }

    private static void avalia(Individuo[] populacao) {
        for (Individuo individuo : populacao) {
            individuo.fitness = funcaoObjetivo(individuo.cromossomo);
        }
        
        ordena(populacao);
    }

    private static void imprime(Individuo[] populacao) {
        for (Individuo individuo : populacao) {
            System.out.println(individuo);
        }
        System.out.println("");
    }

    private static void ordena(Individuo[] populacao) {
        for (int i = 0; i < populacao.length; i++) {
            for (int j = i + 1; j < populacao.length; j++) {
                if (populacao[j].fitness < populacao[i].fitness) {
                    Individuo auxiliar = populacao[i];
                    populacao[i] = populacao[j];
                    populacao[j] = auxiliar;
                }
            }
        }
    }

    private static int funcaoObjetivo(int[] cromossomo) {
        int funcaoAptidao = 0;
        int violacao = 0;

        //Restricao: nao repetir cidade
        for (int i = 1; i < cromossomo.length - 1; i++) {
            if (cromossomo[i] == cromossomo[0] || cromossomo[i] == cromossomo[cromossomo.length - 1]) {
                violacao += 10;
            }
            for (int j = i + 1; j < cromossomo.length - 1; j++) {
                if (cromossomo[i] == cromossomo[j]) {
                    violacao += 10;
                }
            }
        }

        //Restricao: inicio e fim na mesma cidade
        if (cromossomo[0] != cromossomo[cromossomo.length - 1]) {
            violacao += 10;
        }

        //Calculo de custo de viagem
        for (int i = 0; i < cromossomo.length - 1; i++) {
            funcaoAptidao += CUSTO[cromossomo[i]][cromossomo[i + 1]];
        }
        
        int penalizacao = 10;
        return funcaoAptidao + penalizacao * violacao;
    }

    private static Individuo[] selecao(Individuo[] populacao) {
        double[] fitness = new double[populacao.length];
        int somaFitness = 0;
        //System.out.println("Antes");
        for (int i = 0; i < populacao.length; i++) {
            fitness[i] = populacao[i].fitness;
            //System.out.println(fitness[i]);
            somaFitness += fitness[i];
        }
        //System.out.println(somaFitness);
        //System.exit(0);
        //System.out.println("Depois");
        for (int i = 0; i < fitness.length; i++) {
            fitness[i] /= somaFitness;
            //System.out.println(fitness[i]);
            fitness[i] = 1 - fitness[i];

        }
        //System.out.println(fitness[0]+"  "+fitness[1]+"  "+fitness[2]);
        //System.out.println("Depois depois");
        //for (int i = 0; i < fitness.length / 2; i++) {
        //double auxiliar = fitness[i];
        //fitness[i] = fitness[fitness.length - (i + 1)];
        //fitness[fitness.length - (i + 1)] = auxiliar;
        //}
        //for (int i = 0; i < fitness.length; i++) 
        //System.out.println(fitness[i]);
        //System.exit(0);
        //System.out.println(fitness[0]+"  "+fitness[1]+"  "+fitness[2]);
        //System.exit(0);

        double[][] intervalo = new double[populacao.length][2];
        double maximo = 0;
        //System.out.println(intervalo.length);
        //System.exit(0);
        for (int i = 0; i < intervalo.length; i++) {
            if (i == 0) {
                intervalo[i][0] = 0;
            } else {
                intervalo[i][0] = intervalo[i - 1][1];
            }
            //System.out.println("Antes: "+intervalo[i][0]+" "+intervalo[i][1]);
            intervalo[i][1] = intervalo[i][0] + fitness[i];
            //System.out.println("Depois: "+intervalo[i][0]+" "+intervalo[i][1]);

            if (i + 1 == intervalo.length) {
                maximo = intervalo[i][1];
            }
        }
        //System.out.println(maximo);
        //System.exit(0);
        Individuo[] reprodutores = new Individuo[populacao.length];
        //System.out.println(reprodutores.length);
        //System.exit(0);
        for (int i = 1; i < reprodutores.length; i += 2) {
            reprodutores[i] = roleta(populacao, intervalo, maximo);
            reprodutores[i - 1] = roleta(populacao, intervalo, maximo);

            while (Objects.equals(reprodutores[i], reprodutores[i - 1])) {
                reprodutores[i - 1] = roleta(populacao, intervalo, maximo);
            }
        }
        //for (int i = 0; i < reprodutores.length; i ++) 
        //System.out.println(reprodutores[i]);
        //System.exit(0);
        return reprodutores;
    }

    private static Individuo roleta(Individuo[] populacao, double[][] intervalo, double maximo) {
        double escolhido = SORTEIA.nextDouble() * maximo;
        //System.out.println("Escolhido: "+escolhido);
        //System.exit(0);
        for (int i = 0; i < populacao.length; i++) {
            if (escolhido >= intervalo[i][0] && escolhido < intervalo[i][1]) {
                //System.out.println(populacao[i].getFitness());
                return populacao[i];
            }
        }
        //System.exit(0);

        return populacao[0];
    }

    private static Individuo[] cruzamento(Individuo[] reprodutores, Individuo[] populacaoFinal) {

        //Individuo[] populacaoFinal = new Individuo[INDIVIDUOS];
        //int gerados = populacaoFinal.length * TAXACRUZAMENTO / 100;
        //System.out.println("Gerados = "+gerados);        
        //System.exit(0);
        for (int i = 3; i < populacaoFinal.length; i += 2) {
            //System.out.println(indicePais[i][0]);        
            //System.out.println(indicePais[i][1]);        
            //System.out.println("");

            int pontoCorte = sorteiaPonto();

            populacaoFinal[i] = new Individuo();
            populacaoFinal[i].cromossomo = crossover(reprodutores, i, 0, pontoCorte);

            populacaoFinal[i - 1] = new Individuo();
            populacaoFinal[i - 1].cromossomo = crossover(reprodutores, i, 1, pontoCorte);
        }

        /*for (int i = gerados; i < populacaoFinal.length; i++) {
            populacaoFinal[i] = new Individuo();
            populacaoFinal[i].setCromossomo(geraCromossomo());
            indicePais[i][0] = -1;
            indicePais[i][1] = -1;
            populacaoFinal[i].setIndicesPais(indicePais[i]);
        }*/
        //imprime(populacaoFinal);
        //System.exit(0);
        return populacaoFinal;
    }

    private static int[] crossover(Individuo[] reprodutores, int i, int troca, int pontoCorte) {
        int[] cromossomo1 = reprodutores[i].cromossomo;
        int[] cromossomo2 = reprodutores[i - 1].cromossomo;
        //for (int j = 0; j < cromossomo1.length; j++)
        //System.out.println(cromossomo1[j]);
        //System.out.println("");
        //for (int j = 0; j < cromossomo2.length; j++)
        //System.out.println(cromossomo2[j]);

        int resto = GENES - pontoCorte;
        //System.out.println(resto);
        //System.out.println(pontoDeCorte);

        int[][] parte1 = new int[2][pontoCorte];
        int[][] parte2 = new int[2][resto];

        cortaCromossomo(cromossomo1, parte1[0], parte2[0], pontoCorte, resto);
        cortaCromossomo(cromossomo2, parte1[1], parte2[1], pontoCorte, resto);

        int[] cromossomoFilho = new int[GENES];

        switch (troca) {
            case 0:
                trocaGenes(cromossomoFilho, parte1[0], parte2[1], pontoCorte, resto);
                break;

            case 1:
                trocaGenes(cromossomoFilho, parte1[1], parte2[0], pontoCorte, resto);
                break;
        }
        //for (int j = 0; j < cromossomo1.length; j++)
        //System.out.println(cromossomo1[j]);
        //System.out.println("");
        //for (int j = 0; j < cromossomo2.length; j++)
        //System.out.println(cromossomo2[j]);
        //System.exit(0);
        return cromossomoFilho;
    }

    private static void trocaGenes(int[] cromossomoFilho, int[] parte1, int[] parte2, int pontoDeCorte, int resto) {
        System.arraycopy(parte1, 0, cromossomoFilho, 0, pontoDeCorte);

        for (int i = 0, j = pontoDeCorte; i < resto && j < GENES; i++, j++) {
            cromossomoFilho[j] = parte2[i];
        }
    }

    private static int sorteiaPonto() {
        if ((GENES - 1) - 1 == 0) {
            return 1;
        } else {
            return SORTEIA.nextInt((GENES - 1) - 1) + 1;
        }
    }

    private static void cortaCromossomo(int[] cromossomo, int[] parte1, int[] parte2, int pontoDeCorte, int resto) {
        System.arraycopy(cromossomo, 0, parte1, 0, pontoDeCorte);

        for (int i = 0, j = pontoDeCorte; i < resto && j < GENES; i++, j++) {
            parte2[i] = cromossomo[j];
        }
    }

    private static void mutacao(Individuo[] populacaoFinal) {
        //int cont = 0;
        for (int i = 2; i < populacaoFinal.length; i++) {
            //cont++;
            //System.out.println(SORTEIA.nextInt(100));
            int[] cromossomo = populacaoFinal[i].cromossomo;
            for (int j = 0; j < cromossomo.length; j++) {
                //cromossomo[i] = Mutacao.mutacao(cromossomo[i]);
                if (SORTEIA.nextInt(100) < TAXAMUTACAO) {
                    cromossomo[j] = sorteiaIntervalo(LIMITEMINIMO, LIMITEMAXIMO);
                }
            }
            populacaoFinal[i].cromossomo = cromossomo;
        }
        //System.out.println("\nCont = "+cont);
        //System.exit(0);
    }

    private static void elitismo(Individuo[] populacaoInicial, Individuo[] populacaoFinal) {
        populacaoFinal[0] = populacaoInicial[0];
        populacaoFinal[1] = populacaoInicial[1];
        //imprime(populacaoFinal);
        //System.exit(0);
    }

    private static void novaPopulacao(Individuo[] populacaoInicial, Individuo[] populacaoFinal) {
        System.arraycopy(populacaoFinal, 0, populacaoInicial, 0, populacaoFinal.length);
    }

    public static void main(String[] args) {
        defineCusto();

        Individuo[] populacaoInicial = geraPopulacao();
        avalia(populacaoInicial);
        Individuo[] populacaoFinal = new Individuo[INDIVIDUOS];

        int geracoes = GERACOES;
        while (geracoes > 0) {
            //System.exit(0);
            //System.out.println("Populacao Inicial");
            //imprime(populacaoInicial);
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            elitismo(populacaoInicial, populacaoFinal);
            //System.out.println("Populacao Inicial");
            //imprime(populacaoInicial);
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            //System.exit(0);
            populacaoFinal = cruzamento(selecao(populacaoInicial), populacaoFinal);
            //System.out.println("");
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            //System.exit(0);
            mutacao(populacaoFinal);
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            //System.exit(0);
            avalia(populacaoFinal);
            //System.out.println("Populacao Inicial");
            //imprime(populacaoInicial);
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            //System.exit(0);
            novaPopulacao(populacaoInicial, populacaoFinal);
            //System.out.println("Populacao Inicial");
            //imprime(populacaoInicial);
            //System.out.println("Populacao Final");
            //imprime(populacaoFinal);
            //System.exit(0);
            //for (int i = 0; i < INDIVIDUOS; i++)
            //System.out.println(populacaoInicial[GERACOES].getFitness());
            //System.out.println("");
            //System.out.println(populacaoInicial[GERACOES]);
            geracoes--;
        }
        imprime(populacaoInicial);
        System.out.println(populacaoInicial[0]);
    }
}
