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
        String nomeEntrada = "teste2.txt";
        caminho = caminho + nomeEntrada;
        
        
        
        Grafo novoGrafo = new Grafo();
        novoGrafo.leArquivoEntrada(caminho);
        //GraphViz.desenhaGrafo("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/teste2.dot", novoGrafo);
        
        Instant antes = Instant.now();
        novoGrafo.dijkstraCanonico( 4 );
        Instant depois = Instant.now();
        long tempoTotal = Duration.between(antes, depois).toMillis();
        System.out.println("Canônico: " + tempoTotal + " ms");
        
        antes = Instant.now();
        novoGrafo.dijkstraHeapBinario( 4 );
        depois = Instant.now();
        tempoTotal = Duration.between(antes, depois).toMillis();
        System.out.println("Heap: " + tempoTotal + " ms");
        
        
        //System.out.println("Tempo total: " + tempoTotal + " ms");
        //novoGrafo.imprimeGrafo();
        System.out.println("SUCESS");
        
    }
    
}
