/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.testescomputacionais;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteAEstrela 
{
    private static Formatter output;
    private static final int NUM_RODADAS = 5;
    private static final int NUM_VERTICES_ESCOLHIDOS_ALEATORIOS = 10;
    private static List<Integer> verticesSorteados = new LinkedList<>();
    
    public static void openFile()
    {
        try
        {
            output = new Formatter("ResultadosAEstrela.csv");
            output.format( "Nome Instância;Número de Vértices;Número de Arestas;Tempo médio Dijkstra;Tempo médio Dijkstra Adptado;Tempo médio A*;Tempo médio A* Manhattan%n");
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Erro ao abrir o arquivo de escrita \"ResultadosAEstrela.csv\".");
            System.exit( 1 );
        }
        catch ( SecurityException e )
        {
            System.err.println("Error de permissão de escrita no arquivo \"ResultadosAEstrela.csv\".");
            System.exit( 1 );
        }
    }
    
    public static void escreveDados( String nomeArquivo, int numeroVertices, int numeroArestas, long tempoDijkstra, long tempoDijkstraAdptado, long tempoAestrela, long tempoAManhattan )
    {
        
    }
    
    
    public static void main(String[] args) 
    {
        SecureRandom randomNumbers = new SecureRandom();
        
        try ( DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes") ) )
        {
            paths.forEach( filePath -> 
            {
                long tempoDijsktra = 0;
                
                long tempoLocalDijsktraAdpatado = 0;
                long tempoLocalAEstrela = 0;
                long tempoLocalAManhattan = 0;
                
                long tempoGlobalDijsktraAdptado = 0;
                long tempoGlobalAEstrela = 0;
                long tempoGlobalAManhattan = 0; 
                
                long verticesAbertosDijkstraAdptado = 0;
                long verticesAbertosAEstrela = 0;
                long verticesAbertosAManhattan = 0;                
                
                if ( !Files.isDirectory(filePath) )
                {
                    System.out.println("Processando: " + filePath.getFileName().toString() );
                    Grafo g = new Grafo();
                    g.leArquivoEntrada(filePath.toString());
                    
                    String arquivoDeCordenadas = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/Cordenadas/" + (filePath.getFileName().toString().replace(".gr", ".co"));
                    g.leArquivoDeCordenadas(arquivoDeCordenadas);
                    
                    // Dijkstra Canônico
                    for ( int j = 0; j < NUM_RODADAS; j++ )
                    {
                        Instant startDijkstra = Instant.now();
                        g.dijkstraHeapBinario( 0 , -1 );
                        Instant endDijkstra = Instant.now();
                        
                        tempoDijsktra += Duration.between(startDijkstra, endDijkstra).toMillis();
                    }
                    tempoDijsktra /= NUM_RODADAS;
                    
                    for ( int i = 0; i < NUM_VERTICES_ESCOLHIDOS_ALEATORIOS; i++ )
                    {
                        int verticeEscolhido;
                        do
                        {
                            verticeEscolhido = randomNumbers.nextInt( g.getNumeroVertices() );
                        } while ( verticesSorteados.contains( verticeEscolhido ) || verticeEscolhido == 0 );
                        
                        verticesSorteados.add(verticeEscolhido);
                        
                        // Dijkstra Adptado
                        for ( int j = 0; j < NUM_RODADAS; j++ )
                        {
                            Instant startDijkstraAdptado = Instant.now();
                            g.dijkstraHeapBinarioAdptado( 0 , verticeEscolhido, false );
                            Instant endDijsktraAdptado = Instant.now();
                            
                            tempoLocalDijsktraAdpatado += Duration.between(startDijkstraAdptado, endDijsktraAdptado).toMillis();
                        }
                        
                        tempoLocalDijsktraAdpatado /= NUM_RODADAS;
                        tempoGlobalDijsktraAdptado += tempoLocalDijsktraAdpatado;
                        
                        // Algortimo A*
                        for ( int j = 0; j < NUM_RODADAS; j++ )
                        {
                            Instant startAEstrela = Instant.now();
                            g.algoritmoAEstrela( 0 ,  verticeEscolhido, false );
                            Instant endAEstrela = Instant.now();
                            
                            tempoLocalAEstrela += Duration.between(startAEstrela, endAEstrela).toMillis();
                        }
                        tempoLocalAEstrela /= NUM_RODADAS;
                        tempoGlobalAEstrela += tempoLocalAEstrela;
                        
                        // Algortimo A* não-admissíveil
                        for ( int j = 0; j < NUM_RODADAS; j++ )
                        {
                            // Algoritmo A* não-admissível
                            Instant startNaoAdmissivel = Instant.now();
                            g.algoritmoAEstrelaManhattan( 0, verticeEscolhido, false );
                            Instant endNaoAdmissivel = Instant.now();
                            
                            tempoLocalAManhattan += Duration.between(startNaoAdmissivel, endNaoAdmissivel).toMillis();
                        }
                        tempoLocalAManhattan /= NUM_RODADAS;
                        tempoGlobalAManhattan += tempoLocalAManhattan;
                        
                        // Início da contagem de vértices abertos - A REVISAR
                        verticesAbertosDijkstraAdptado += g.contaNumeroDeVerticesAbertosDijkstraAdptado( 0 ,  verticeEscolhido );
                        verticesAbertosAEstrela += g.contaNumeroDeVerticesAbertosAEstrela( 0, verticeEscolhido );
                        verticesAbertosAManhattan += g.contaNumeroDeVerticesAbertosAEstrelaNaoAdmissivel( 0, verticeEscolhido );
                    }
                    verticesSorteados.clear();
                    
                    // Tirando a média dos tempos globais
                    tempoGlobalAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    tempoGlobalAManhattan /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    tempoGlobalDijsktraAdptado /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    
                    // Tirando a média do número de vértices abertos
                    verticesAbertosDijkstraAdptado /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    verticesAbertosAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    verticesAbertosAManhattan /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;                    
                }
            }
            );
            
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
        
        System.out.println("SUCESS");
        
    }
    
}
