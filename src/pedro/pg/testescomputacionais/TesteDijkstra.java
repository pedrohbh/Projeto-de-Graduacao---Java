/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.testescomputacionais;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Formatter;
import java.util.stream.Stream;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteDijkstra 
{
    private static Formatter output;
    
    public static void openFile()
    {
        try
        {
            output = new Formatter("ResultadosDijkstra.csv");
            output.format("Nome Instância;Número de Vértices;Número de Arestas;Tempo Canônico (em milissegundos);Tempo Heap Binário (em milissegundos);Speed-up Heap Binário;Tempo Heap Fibonacci (em milissegundos);Speed-up Heap Fibonacci%n");
        }
        catch ( SecurityException e )
        {
            System.err.println("Permissão de escrita negada. Encerrando o programa.");
            System.exit( 1 );
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Error ao abrir o arquivo. Encerrando o programa.");
            System.exit( 1 );
        }
    }
    
    
    
    
    public static void main(String[] args) throws IOException 
    {
        String nomeArquivo;
        //tempoDecorrido;
        
        // Abre arquivo onde os resultados serão escritos
        openFile();
        
        // No caso de testar em outras máquinas troque o endereço em "Paths.get("Endereço")" pelo endereço correto
        try ( Stream<Path> paths = Files.walk(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes")))
        {
            paths.forEach(filePath -> 
            {
                if (Files.isRegularFile(filePath)) 
                {
                    System.out.println("Processando: " + filePath.toString() );
                    Grafo g = new Grafo();
                    g.leArquivoEntrada(filePath.toString());
                    
                    output.format("%s;%d;%d;", filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas() );
                    
                    Instant start = Instant.now();
                    g.dijkstraCanonico( 0 );
                    Instant end = Instant.now();
                    
                    long tempoDecorridoCanonico = Duration.between(start, end).toMillis();
                    output.format("%d", tempoDecorridoCanonico );
                    
                    // Heap Binário
                    long tempoDecorrido = 0;
                    for ( int i = 0; i < 3; i++ )
                    {
                        Instant heapBinStrart = Instant.now();
                        g.dijkstraHeapBinario( 0 );
                        Instant heapBinEnd = Instant.now();
                        tempoDecorrido += Duration.between(start, end).toMillis();                        
                    }
                    
                    tempoDecorrido /= 3;
                    String porcetagem = NumberFormat.getPercentInstance().format((double) tempoDecorridoCanonico / tempoDecorrido );
                    output.format("%d;%s;", tempoDecorrido, porcetagem );
                    
                    // Heap FIbonacci
                    tempoDecorrido = 0;
                    for ( int i = 0; i < 3; i++ )
                    {
                        Instant fibonacciStart = Instant.now();
                        g.dijkstraHeapFibonacci( 0 );
                        Instant fibonacciEnd = Instant.now();
                        tempoDecorrido += Duration.between(start, end).toMillis();
                    }
                    
                    tempoDecorrido /= 3;
                    NumberFormat.getPercentInstance().format((double) tempoDecorridoCanonico / tempoDecorrido );
                    output.format("%d;%s;%n", tempoDecorrido, porcetagem );
                    
                }
            });
        }
        
        
        
    }
    
}
