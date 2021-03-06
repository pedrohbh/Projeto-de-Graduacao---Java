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
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class TesteARA 
{
    private static Formatter arquivoTestesARA;
    private static Formatter arquivoTestesAD;
    private static Formatter arquivoSolucao;
    private static ModosAD modoAtual = ModosAD.NORMAL;
    private static final ModosAD []modosPossiveis = { ModosAD.NORMAL, ModosAD.DIMINUI, ModosAD.AUMENTAR };
    private static final double []porcentagem = { 2.5, 20.0, 50.0, 70.0 };
    private static final int NUM_RODADAS = 5;
    private static final int NUM_VERTICES_ESCOLHIDOS_ALEATORIOS = 10;
    private static final double EPISOLON_INICIAL = 4.0;
    private static final double FATOR_DE_CORTE = 0.5;
    private static final Set<Integer> verticesSorteados = new HashSet<>();
    private static final Map<Double, GuardaTempo> resultadosAra = new HashMap<>();
    private static final Map<Double, GuardaTempo> resultadosADinamico = new HashMap<>();
    private static final Map<Double, GuardaTempo> temposADDinamico = new HashMap<>(); // Troquei a forma dos nomes para não errar na hora de acionar a variável
    
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
            arquivoTestesARA.format( "Nome Instância;Número de Vértices;Número de Arestas;Epsisolon;NVA A*;Tempo médio A*;NVA ARA*;Tempo médio ARA*;Ganho de tempo com realção ao A*%n");
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
    
    public static void openFileTestesAD()
    {
        try
        {
            arquivoTestesAD = new Formatter("ResultadosAD.csv");
            arquivoTestesAD.format("Nome Instância;Número Vértices;Número Arestas;Porcentagem de mudança de vértices;Modo;Tempo A*;NVA A*;Tempo AD*;NVA AD*;Ganho em relação ao A*%n");
        }
        catch ( FileNotFoundException e )
        {
            System.err.println("Erro ao abrir o arquivo de escrita \"ResultadosAD.csv\".");
            System.exit( 1 );
        }
        catch ( SecurityException e )
        {
            System.err.println("Error de permissão de escrita no arquivo \"ResultadosAD.csv\".");
            System.exit( 1 );
        }
    }
    
    public static void escreveDadosTestesAD( String nomeInstancia, int numeroVertices, int numeroArestas,double porcentagem, String modo, long tempoAEstrela, long nvaA, long tempoAD, long nvaAD )
    {
        try
        {
            String porcentagemTempo = NumberFormat.getPercentInstance().format( (double)(tempoAEstrela / tempoAD) );
            String porcentagemModo = NumberFormat.getPercentInstance().format(porcentagem);
            //arquivoTestesAD.format("%s;%d;%d;%d;%d;%d%n", nomeInstancia, numeroVertices, numeroArestas, dijkstraAdptado, aEstrela, aManhattan );
            arquivoTestesAD.format("%s;%d;%d;%s;%s;%d;%d;%d;%s%n", nomeInstancia, numeroVertices, numeroArestas, modo, porcentagemModo, tempoAEstrela, nvaA, tempoAD, nvaAD, porcentagemTempo );
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
    
    public static void escreveDadosTestesARA( String nomeInstancia, int numeroVertices, int numeroArestas, double epsiolon, long tempoARA, long tempoA, long nvaA, long nvaARA )
    {
        try
        {            
            String porcentagem = NumberFormat.getPercentInstance().format( (double)(tempoA / tempoARA) );
            arquivoTestesARA.format("%s;%d;%d;%s;%d;%d;%d;%d;%s%n", nomeInstancia, numeroVertices, numeroArestas, epsiolon, nvaA, tempoA, nvaARA, tempoARA, porcentagem );                     
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
        
        openFileTestesARA();        
        
        try ( DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes") ) )
        {
            paths.forEach(filePath -> 
            {
                long tempoDijsktra = 0;
                
                long tempoLocalARA = 0;
                long tempoLocalAEstrela = 0;
                long tempoLocalAEstrelaDinamico = 0;
                long tempoLocalAD = 0;
                
                long tempoGlobalARA = 0;
                long tempoGlobalAEstrela = 0;
                long tempoGlobalAEstrelaDinamico = 0; 
                
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
                        tempoLocalAEstrelaDinamico = 0;
                        
                        int verticeEscolhido;
                        do
                        {
                            verticeEscolhido = randomNumbers.nextInt( g.getNumeroVertices() );
                        } while ( verticesSorteados.contains( verticeEscolhido ) || verticeEscolhido == 0 );
                        
                        verticesSorteados.add(verticeEscolhido);                       
                        
                        // ------------- PARTE ARA*
                        
                        // Algortimo A*
                        for ( int j = 0; j < NUM_RODADAS; j++ )
                        {
                            Instant startAEstrela = Instant.now();
                            g.algoritmoAEstrela( 0 ,  verticeEscolhido, false, false );
                            Instant endAEstrela = Instant.now();
                            
                            tempoLocalAEstrela += Duration.between(startAEstrela, endAEstrela).toNanos();
                        }
                        tempoLocalAEstrela /= NUM_RODADAS;
                        tempoGlobalAEstrela += tempoLocalAEstrela;
                        
                        // Algortimo ARA*
                        while ( episolon >= 1 )
                        {                            
                            for ( int j = 0; j < NUM_RODADAS; j++ )
                            {
                                Instant startARA = Instant.now();
                                g.medeTempoAnytimeSearchAEstrela( 0, verticeEscolhido, episolon );
                                Instant endARA = Instant.now();
                            
                                tempoLocalARA += Duration.between(startARA, endARA).toNanos();
                            }
                            tempoLocalARA /= NUM_RODADAS;
                                                
                            // Contagem de vértices abertos ARA
                            verticesAbertosARA = g.calculaVerticesAbertosAnytimeSearchAEstrela( 0, verticeEscolhido, episolon );
                            if ( !resultadosAra.containsKey( episolon ) )
                                resultadosAra.put(episolon, new GuardaTempo(episolon, tempoLocalARA, verticesAbertosARA ) );
                            else
                            {
                                resultadosAra.get( episolon ).adicionaATempoExistente( tempoLocalARA );
                                resultadosAra.get( episolon ).adicionaAVerticesAbertosExistentes(custoAdmissivel);
                            }
                            
                            
                            episolon -= FATOR_DE_CORTE;
                            tempoLocalARA = 0;
                        }
                        // Contagem de vértices abertos AEstrela
                        verticesAbertosAEstrela += g.contaNumeroDeVerticesAbertosAEstrela( 0, verticeEscolhido );
                                                
                    }                   
                    // Tirando a média dos tempos globais
                    tempoGlobalAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                                        
                    // Tirando a média do número de vértices abertos
                    verticesAbertosAEstrela /= NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                    
                    episolon = EPISOLON_INICIAL;
                    while ( episolon >= 1 )
                    {
                        tempoGlobalARA = resultadosAra.get( episolon ).getTempoAssociado() / NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                        verticesAbertosARA = resultadosAra.get( episolon ).getVerticesAberto() / NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                        escreveDadosTestesARA(filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), episolon, tempoGlobalARA, tempoGlobalAEstrela, verticesAbertosAEstrela, verticesAbertosARA);
                        episolon -= FATOR_DE_CORTE;
                    }                  
                    
                    temposADDinamico.clear();
                    resultadosADinamico.clear();
                    resultadosAra.clear();
                    verticesSorteados.clear();
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
        fechaArquivo(arquivoTestesAD);
        fechaArquivo(arquivoSolucao);
        
        System.out.println("SUCESS");
        
    }
    
    private static class GuardaTempo
    {
        private long tempoAssociado;
        private final double episolon;
        private long verticesAberto;
        
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
        
        public void adicionaAVerticesAbertosExistentes( long novoVertices )
        {
            this.verticesAberto += novoVertices;
        }

        /**
         * @return the tempoAssociado
         */
        public long getTempoAssociado() {
            return tempoAssociado;
        }

        /**
         * @return the episolon
         */
        public double getEpisolon() {
            return episolon;
        }

        /**
         * @return the verticesAberto
         */
        public long getVerticesAberto() {
            return verticesAberto;
        }
    }
    
}
