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
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteAEstrela 
{
    private static Formatter output;
    private static final int NUM_RODADAS = 5;
    
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
        
        try ( DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes") ) )
        {
            paths.forEach( filePath -> 
            {
                if ( !Files.isDirectory(filePath) )
                {
                    System.out.println("Processando: " + filePath.getFileName().toString() );
                    Grafo g = new Grafo();
                    g.leArquivoEntrada(filePath.toString());
                    
                    String arquivoDeCordenadas = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/Cordenadas/" + (filePath.getFileName().toString().replace(".gr", ".co"));
                    g.leArquivoDeCordenadas(arquivoDeCordenadas);
                    
                    //System.out.printf("Caminho Grafo: %s%nCaminho Cordenadas: %s%n%n", filePath.toString(), arquivoDeCordenadas );
                    
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
