/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import java.time.Duration;
import java.time.Instant;
import pedro.pg.grafo.Grafo;
import pedro.pg.utilitario.GraphViz;

/**
 *
 * @author administrador
 */
public class Principal 
{
    public static void main(String[] args) 
    {
        String caminho = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/";
        String nomeEntrada = "USA-road-d.NY.gr";
        String nomeCordenadas = caminho + "Cordenadas/" + "USA-road-d.NY.co";
        caminho = caminho + nomeEntrada;
        
        
        
        Grafo novoGrafo = new Grafo();
        novoGrafo.leArquivoEntrada(caminho);
        novoGrafo.leArquivoDeCordenadas(nomeCordenadas);
        novoGrafo.dijkstraHeapBinario( 0, 180 );
        //Instant start1 = Instant.now();
        novoGrafo.algoritmoAEstrela( 0, 180 );
        novoGrafo.dijkstraHeapBinarioAdptado(0, 180);
        //System.out.println("Número de vértices abertos: " + novoGrafo.contaNumeroDeVerticesAbertosAEstrela(0, 1));
        //Instant end1 = Instant.now();
        //long tempo1 = Duration.between(start1, end1).toMillis();
        //Instant start2 = Instant.now();
        //novoGrafo.algoritmoAEstrelaBeta(0, 180);
        //Instant end2 = Instant.now();
        
        
        //long tempo2 = Duration.between(start2, end2).toMillis();
        //System.out.printf("Tempo 1: %d ms%nTempo Beta: %d ms%n", tempo1, tempo2 );
        //novoGrafo.dijkstraCanonico( 0 );
        
        //novoGrafo.dijkstraHeapBinario(0);
        //novoGrafo.dijkstraHeapFibonacci( 0 );
        
        /*Grafo g = new Grafo();
        g.leArquivoEntrada(caminho);
        Instant strat = Instant.now();
        g.dijkstraHeapBinario( 0  );
        Instant end = Instant.now();
        long tempoBin = Duration.between(strat, end ).toMillis();
        
        Instant fibStart = Instant.now();
        g.dijkstraHeapFibonacci( 0 );
        Instant fibEnd = Instant.now();
        long tempoFib = Duration.between( fibStart , fibEnd ).toMillis();
        
        System.out.println("Tempos totais");
        System.out.println("Heap binario: " + tempoBin + " ms." );
        System.out.println("Fibonacci: " + tempoFib + " ms." );*/
        
        
        //novoGrafo.dijkstraHeapFibonacciPrototipo(2 );
        //GraphViz.desenhaGrafo(caminho, novoGrafo);
                
        System.out.println("SUCESS");
    }
    
}
