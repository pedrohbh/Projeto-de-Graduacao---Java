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
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteDijkstra 
{
    private static Formatter output;
    private static final int NUM_RODADAS = 3;
    
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
    
    public static void escreveDados( String nomeArquivo, int numeroVertices, int numeroArestas, long tempoCanonico, long tempoHeapBinario, long tempoFibonacci )
    {
        try
        {
            String porcentagemBinario = NumberFormat.getPercentInstance().format( (double) tempoCanonico / tempoHeapBinario );
            String porcentagemFibonacci = NumberFormat.getPercentInstance().format( (double) tempoCanonico / tempoFibonacci );
            output.format("%s;%d;%d;%d;%d;%s;%d;%s%n", nomeArquivo, numeroVertices, numeroArestas, tempoCanonico, tempoHeapBinario, porcentagemBinario, tempoFibonacci, porcentagemFibonacci );
        }
        catch ( FormatterClosedException e)
        {
            System.err.println("Erro ao escrever em arquivo de saida. Termianando programa.");
            e.printStackTrace();
            System.exit( 1 );
        }
        catch ( NoSuchElementException e )
        {
            System.err.println("A varíavel não existe para ser escrita. Encerrando o programa.");
            e.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public static void closeFile()
    {
        if ( output != null )
            output.close();
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
                    
                    Instant start = Instant.now();
                    g.dijkstraCanonico( 0 );
                    Instant end = Instant.now();
                    
                    long tempoDecorridoCanonico = Duration.between(start, end).toMillis();
                    
                    // Heap Binário
                    long tempoDecorridoBinario = 0;
                    for ( int i = 0; i < NUM_RODADAS; i++ )
                    {
                        Instant heapBinStart = Instant.now();
                        g.dijkstraHeapBinario( 0 );
                        Instant heapBinEnd = Instant.now();
                        tempoDecorridoBinario += Duration.between(heapBinStart, heapBinEnd).toMillis();                        
                    }
                    
                    tempoDecorridoBinario /= NUM_RODADAS;
                    //String porcetagem = NumberFormat.getPercentInstance().format((double) tempoDecorridoCanonico / tempoDecorrido );
                    //output.format("%d;%s;", tempoDecorrido, porcetagem );
                    
                    // Heap FIbonacci
                    long tempoDecorridoFibonacci = 0;
                    for ( int i = 0; i < NUM_RODADAS; i++ )
                    {
                        Instant fibonacciStart = Instant.now();
                        g.dijkstraHeapFibonacci( 0 );
                        Instant fibonacciEnd = Instant.now();
                        tempoDecorridoFibonacci += Duration.between(fibonacciStart, fibonacciEnd).toMillis();
                    }
                    
                    tempoDecorridoFibonacci /= NUM_RODADAS;
                    
                    
                    escreveDados( filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), tempoDecorridoCanonico, tempoDecorridoBinario, tempoDecorridoFibonacci);
                }
            });
        }
        closeFile();
        
        
        
    }
    
}
