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
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteDinamicos 
{
    private static Formatter arquivoTestesARA;
    private static Formatter arquivoVertices;
    private static Formatter arquivoSolucao;
    private static final int NUM_RODADAS = 5;
    private static final int NUM_VERTICES_ESCOLHIDOS_ALEATORIOS = 10;
    private static final double EPISOLON_INICIAL = 3.0;
    private static final double FATOR_DE_CORTE = 0.5;
    private static final List<Integer> verticesSorteados = new LinkedList<>();
    private static final Map<Double, GuardaTempo> resultadosAra = new HashMap<>();
    
    public static void openFileSolucao()
    {
        try
        {
            arquivoSolucao = new Formatter("ResultadosAEstrelaSolucao.csv");
            arquivoSolucao.format( "Nome Instância;Número de Vértices;Número de Arestas;Qualidade Solução%n");
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Erro ao abrir o arquivo de escrita \"ResultadosAEstrelaSolucao.csv\".");
            System.exit( 1 );
        }
        catch ( SecurityException e )
        {
            System.err.println("Error de permissão de escrita no arquivo \"ResultadosAEstrelaSolucao.csv\".");
            System.exit( 1 );
        }
    }
    
    public static void openFileTestesARA()
    {
        try
        {
            arquivoTestesARA = new Formatter("ResultadosARA.csv");
            arquivoTestesARA.format( "Nome Instância;Número de Vértices;Número de Arestas;Epsisolon;NVA A*;Tempo médio A*;NVA ARA*;Tempo médio ARA*;Ganho de tempo com realção ao A*");
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Erro ao abrir o arquivo de escrita \"ResultadosARA.csv\".");
            System.exit( 1 );
        }
        catch ( SecurityException e )
        {
            System.err.println("Error de permissão de escrita no arquivo \"ResultadosARA.csv\".");
            System.exit( 1 );
        }
    }
    
    public static void openFileVerticesAbertos()
    {
        try
        {
            arquivoVertices = new Formatter("ResultadosAEstrelaVertices.csv");
            arquivoVertices.format("Nome Instância;Número de Vértices;Número de Arestas;NVA Dijkstra Adptado;NVA A*;NVA A* Manhattan%n" );
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Erro ao abrir o arquivo de escrita \"ResultadosAEstrelaVertices.csv\".");
            System.exit( 1 );
        }
        catch ( SecurityException e )
        {
            System.err.println("Error de permissão de escrita no arquivo \"ResultadosAEstrelaVertices.csv\".");
            System.exit( 1 );
        }
    }
    
    public static void escreveDadosVertices( String nomeInstancia, int numeroVertices, int numeroArestas, long dijkstraAdptado, long aEstrela, long aManhattan )
    {
        try
        {
            arquivoVertices.format("%s;%d;%d;%d;%d;%d%n", nomeInstancia, numeroVertices, numeroArestas, dijkstraAdptado, aEstrela, aManhattan );
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
    
    public static void escreveDadosSolucao( String nomeInstancia, int numeroVertices, int numeroArestas, double porcentagem )
    {
        try
        {
            String porcentagemTexto = NumberFormat.getPercentInstance().format(porcentagem);
            arquivoSolucao.format("%s;%d;%d;%s%n", nomeInstancia, numeroVertices, numeroArestas, porcentagemTexto );
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
    
    public static void escreveDadosTestesARA( String nomeInstancia, int numeroVertices, int numeroArestas, boolean escreveNome, double epsiolon, long tempoARA, long tempoA, long nvaA, long nvaARA )
    {
        try
        {
            
            if ( escreveNome )
                arquivoTestesARA.format("%s;;;;;;;;", nomeInstancia);
            else
            {
                String porcentagem = NumberFormat.getPercentInstance().format( (double)(tempoA / tempoARA) );
                arquivoTestesARA.format("%s;%d;%d;%f;%d;%d;%d;%d;%s", nomeInstancia, numeroVertices, numeroArestas, epsiolon, nvaA, tempoA, nvaARA, tempoARA, porcentagem );
            }
            
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
    
    public static void fechaArquivo( Formatter arquivo )
    {
        if ( arquivo != null )
            arquivo.close();
    }
    
    
    public static void main(String[] args) 
    {
        SecureRandom randomNumbers = new SecureRandom();
        
        openFileSolucao();
        openFileTestesARA();
        openFileVerticesAbertos();
        
        try ( DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes") ) )
        {
            paths.forEach(filePath -> 
            {
                long tempoDijsktra = 0;
                
                long tempoLocalARA = 0;
                long tempoLocalAEstrela = 0;
                long tempoLocalAManhattan = 0;
                
                long tempoGlobalARA = 0;
                long tempoGlobalAEstrela = 0;
                long tempoGlobalAManhattan = 0; 
                
                long verticesAbertosARA = 0;
                long verticesAbertosAEstrela = 0;
                long verticesAbertosAManhattan = 0;
                
                long custoNaoAdmissivel = 0;
                long custoAdmissivel = 0;
                double porcentagemCusto = 0;
                
                double episolon = EPISOLON_INICIAL;
                
                if ( !Files.isDirectory(filePath) )
                {
                    System.out.println("Processando: " + filePath.getFileName().toString() );
                    Grafo g = new Grafo();
                    g.leArquivoEntrada(filePath.toString());
                    
                    String arquivoDeCordenadas = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/Cordenadas/" + (filePath.getFileName().toString().replace(".gr", ".co"));
                    g.leArquivoDeCordenadas(arquivoDeCordenadas);
                    
                    for ( int i = 0; i < NUM_VERTICES_ESCOLHIDOS_ALEATORIOS; i++ )
                    {
                        tempoLocalARA = 0;
                        tempoLocalAEstrela = 0;
                        tempoLocalAManhattan = 0;
                        
                        int verticeEscolhido;
                        do
                        {
                            verticeEscolhido = randomNumbers.nextInt( g.getNumeroVertices() );
                        } while ( verticesSorteados.contains( verticeEscolhido ) || verticeEscolhido == 0 );
                        
                        verticesSorteados.add(verticeEscolhido);
                                                
                        // Algortimo A*
                        for ( int j = 0; j < NUM_RODADAS; j++ )
                        {
                            Instant startAEstrela = Instant.now();
                            g.algoritmoAEstrela( 0 ,  verticeEscolhido, false, false );
                            Instant endAEstrela = Instant.now();
                            
                            tempoLocalAEstrela += Duration.between(startAEstrela, endAEstrela).toMillis();
                        }
                        tempoLocalAEstrela /= NUM_RODADAS;
                        tempoGlobalAEstrela += tempoLocalAEstrela;
                         
                        while ( episolon >= 1 )
                        {
                            // Algortimo ARA*
                            for ( int j = 0; j < NUM_RODADAS; j++ )
                            {
                                Instant startARA = Instant.now();
                                g.medeTempoAnytimeSearchAEstrela( 0, verticeEscolhido, episolon );
                                Instant endARA = Instant.now();
                            
                                tempoLocalARA += Duration.between(startARA, endARA).toMillis();
                            }
                            tempoLocalARA /= NUM_RODADAS;
                            //tempoGlobalARA += tempoLocalARA;
                                                
                            // Contagem de vértices abertos ARA
                            verticesAbertosARA += g.calculaVerticesAbertosAnytimeSearchAEstrela( 0, verticeEscolhido, episolon );
                            if ( !resultadosAra.containsKey( episolon ) )
                                resultadosAra.put(episolon, new GuardaTempo(episolon, tempoLocalARA, verticesAbertosARA ) );
                            else
                                resultadosAra.get( episolon ).adicionaATempoExistente( tempoLocalARA );
                            
                            
                            episolon -= FATOR_DE_CORTE;                            
                        }
                        // Contagem de vértices abertos AEstrela
                        verticesAbertosAEstrela += g.contaNumeroDeVerticesAbertosAEstrela( 0, verticeEscolhido );
                                                
                    }
                    verticesSorteados.clear();
                    
                    // Tirando a média dos tempos globais
                    tempoGlobalAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    tempoGlobalAManhattan /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    tempoGlobalARA /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    
                    // Tirando a média do número de vértices abertos
                    verticesAbertosARA /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    verticesAbertosAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    verticesAbertosAManhattan /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    
                    porcentagemCusto /= (double)NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    
                    //escreveDadosTestesARA(filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), tempoDijsktra, tempoGlobalARA, tempoGlobalAEstrela, tempoGlobalAManhattan );
                    escreveDadosVertices(filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), verticesAbertosARA, verticesAbertosAEstrela, verticesAbertosAManhattan );
                    escreveDadosSolucao(filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), porcentagemCusto );
                }
            }
            );
            
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
        
        fechaArquivo(arquivoTestesARA);
        fechaArquivo(arquivoVertices);
        fechaArquivo(arquivoSolucao);
        
        System.out.println("SUCESS");
        
    }
    
    private static class GuardaTempo
    {
        private long tempoAssociado;
        private final double episolon;
        private final long verticesAberto;
        
        public GuardaTempo( double episolon, long tempoAssociado, long verticesAbertos )
        {
            this.episolon = episolon;
            this.tempoAssociado = tempoAssociado;
            this.verticesAberto = verticesAbertos;
        }
        
        public void adicionaATempoExistente( long novoTempo )
        {
            this.tempoAssociado += novoTempo;
        }

        /**
         * @return the tempoAssociado
         */
        public long getTempoAssociado() {
            return tempoAssociado;
        }
    }
    
}
