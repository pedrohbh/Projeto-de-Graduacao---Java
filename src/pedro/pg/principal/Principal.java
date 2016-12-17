/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.principal;

import java.time.Duration;
import java.time.Instant;
import pedro.pg.grafo.Grafo;

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
        caminho = caminho + nomeEntrada;
        
        
        Grafo novoGrafo = new Grafo();
        novoGrafo.leArquivoEntrada(caminho);
        
        Instant antes = Instant.now();
        novoGrafo.dijkstraCanonico(0);
        Instant depois = Instant.now();
        long tempoTotal = Duration.between(antes, depois).toMillis();
        System.out.println("Canônico: " + tempoTotal + " ms");
        
        antes = Instant.now();
        novoGrafo.dijkstraHeapBinario( 0 );
        depois = Instant.now();
        tempoTotal = Duration.between(antes, depois).toMillis();
        System.out.println("Heap: " + tempoTotal + " ms");
        
        
        //System.out.println("Tempo total: " + tempoTotal + " ms");
        //novoGrafo.imprimeGrafo();
        System.out.println("SUCESS");
        
    }
    
}
