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
public class TesteAD
{
    private static Formatter arquivoTestesARA;
    private static Formatter arquivoTestesAD;
    private static Formatter arquivoSolucao;
    private static ModosAD modoAtual = ModosAD.NORMAL;
    private static final ModosAD []modosPossiveis = { ModosAD.NORMAL, ModosAD.DIMINUI, ModosAD.AUMENTAR };
    private static final double []porcentagem = { 2.5, 20.0, 50.0, 70.0 };
    private static final int NUM_RODADAS = 3;
    private static final int NUM_VERTICES_ESCOLHIDOS_ALEATORIOS = 10;
    private static final int NUM_INSTANCIAS_CALCULADAS = 15;
    private static final double EPISOLON_INICIAL = 2.0;
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
            arquivoTestesAD.format("Nome Instância;Número Vértices;Número Arestas;Modo;Porcentagem de mudança de vértices;Tempo A*;NVA A*;Tempo AD*;NVA AD*;Ganho em relação ao A*%n");
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
            arquivoTestesAD.format("%s;%d;%d;%s;%s;%d;%d;%d;%d;%s%n", nomeInstancia, numeroVertices, numeroArestas, modo, porcentagemModo, tempoAEstrela, nvaA, tempoAD, nvaAD, porcentagemTempo );
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
        openFileTestesAD();
        
        try ( DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes") ) )
        {
            paths.forEach(filePath -> 
            {
                              
                long tempoLocalAEstrelaDinamico = 0;
                long tempoLocalAD = 0;
                
                long tempoGlobalAEstrelaDinamico = 0;                
                                
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
                        tempoLocalAEstrelaDinamico = 0;
                        
                        int verticeEscolhido;
                        do
                        {
                            verticeEscolhido = randomNumbers.nextInt( g.getNumeroVertices() );
                        } while ( verticesSorteados.contains( verticeEscolhido ) || verticeEscolhido == 0 );
                        
                        verticesSorteados.add(verticeEscolhido);                       
                        
                        Grafo grafoOrignal = new Grafo( g );
                        // Algoritmo AD* e A*
                        for ( int j = 0; j < modosPossiveis.length; j++ )
                        {                            
                            modoAtual = modosPossiveis[ j ];
                            if ( modoAtual == ModosAD.NORMAL )
                            {                               
                                // Parte A*
                                for ( int k = 0; k < NUM_INSTANCIAS_CALCULADAS; k++ )
                                {
                                    tempoLocalAEstrelaDinamico = 0;
                                    for ( int l = 0; l < NUM_RODADAS; l++ )
                                    {
                                        Instant startAEstrelaDinamico = Instant.now();
                                        g.algoritmoAEstrela( 0 , verticeEscolhido, false, false );
                                        Instant endAEstrelaDinamico = Instant.now();
                                        
                                        tempoLocalAEstrelaDinamico += Duration.between(startAEstrelaDinamico, endAEstrelaDinamico).toNanos();
                                    }
                                    tempoLocalAEstrelaDinamico /= NUM_RODADAS;
                                    tempoGlobalAEstrelaDinamico = tempoLocalAEstrelaDinamico;
                                    
                                    long numeroVerticesAbertos = g.contaNumeroDeVerticesAbertosAEstrela( 0 , verticeEscolhido );
                                    
                                    if ( !resultadosADinamico.containsKey( (double)0 ) )
                                        resultadosADinamico.put( (double)0 , new GuardaTempo( (double)0, tempoGlobalAEstrelaDinamico, numeroVerticesAbertos) );
                                    else
                                    {
                                        resultadosADinamico.get( (double)0 ).adicionaATempoExistente( tempoGlobalAEstrelaDinamico );
                                        resultadosADinamico.get( (double)0 ).adicionaAVerticesAbertosExistentes(numeroVerticesAbertos);
                                    }
                                }
                                
                                Instant startAD = Instant.now();
                                long tempoASerDebitadoAD = g.dynamicSearchAEstrela( 0, verticeEscolhido, EPISOLON_INICIAL, FATOR_DE_CORTE, NUM_INSTANCIAS_CALCULADAS, false, false, 0.0, false );
                                Instant endAD = Instant.now();
                                tempoLocalAD = Duration.between(startAD, endAD).toNanos();
                                tempoLocalAD -= tempoASerDebitadoAD;
                                if ( !temposADDinamico.containsKey( (double)0 ) )
                                    temposADDinamico.put( (double)0, new GuardaTempo( (double)0, tempoLocalAD, 0) );
                                else
                                    temposADDinamico.get( (double)0 ).adicionaATempoExistente(tempoLocalAD);
                                // Parte AD* Normal começa aqui
                            }
                            // Parte de diminuir o peso das arestas do grafo
                            else if ( modoAtual == ModosAD.DIMINUI )
                            {
                                tempoGlobalAEstrelaDinamico = 0;
                                tempoLocalAEstrelaDinamico = 0;
                                for ( int m = 0; m < porcentagem.length; m++ )
                                {
                                    double porcentagemAtual = porcentagem[ m ];
                                    tempoLocalAEstrelaDinamico = 0;
                                    tempoGlobalAEstrelaDinamico = 0;
                                    for ( int k = 0; k < NUM_INSTANCIAS_CALCULADAS; k++ )
                                    {
                                        tempoLocalAEstrelaDinamico = 0;
                                        porcentagemAtual /= 100;
                                        int numeroVezes = (int) (g.getNumeroVertices() * porcentagemAtual);
                                        for ( int n = 0; n < numeroVezes; n++ )
                                            g.alteraPesosArestasGrafoPublico( false, false );
                                        porcentagemAtual *= 100;
                                        for ( int l = 0; l < NUM_RODADAS; l++ )
                                        {
                                            Instant startAEstrelaDinamico = Instant.now();
                                            g.algoritmoAEstrela( 0 , verticeEscolhido, false, false );
                                            Instant endAEstrelaDinamico = Instant.now();
                                            tempoLocalAEstrelaDinamico += Duration.between(startAEstrelaDinamico, endAEstrelaDinamico).toNanos();
                                        }
                                        tempoLocalAEstrelaDinamico /= NUM_RODADAS;
                                        tempoGlobalAEstrelaDinamico = tempoLocalAEstrelaDinamico;
                                        
                                        long numeroVerticesAbertos = g.contaNumeroDeVerticesAbertosAEstrela( 0 , verticeEscolhido );
                                        
                                        if ( !resultadosADinamico.containsKey( porcentagemAtual ) )
                                            resultadosADinamico.put( porcentagemAtual, new GuardaTempo( porcentagemAtual, tempoGlobalAEstrelaDinamico, numeroVerticesAbertos ) );
                                        else
                                        {
                                            resultadosADinamico.get( porcentagemAtual ).adicionaATempoExistente( tempoGlobalAEstrelaDinamico );
                                            resultadosADinamico.get( porcentagemAtual ).adicionaAVerticesAbertosExistentes( numeroVerticesAbertos );
                                        }
                                    }
                                    g.recuperaGrafoOriginal( grafoOrignal );
                                      
                                    Instant startAD = Instant.now();
                                    long tempoASerDebitadoAD = g.dynamicSearchAEstrela( 0, verticeEscolhido, EPISOLON_INICIAL, FATOR_DE_CORTE, NUM_INSTANCIAS_CALCULADAS, true, false, porcentagemAtual, false );
                                    Instant endAD = Instant.now();
                                    tempoLocalAD = Duration.between( startAD, endAD ).toNanos();
                                    tempoLocalAD -= tempoASerDebitadoAD;
                                    
                                    if ( !temposADDinamico.containsKey( porcentagemAtual ) )
                                        temposADDinamico.put( porcentagemAtual, new GuardaTempo( porcentagemAtual, tempoLocalAD, 0) );
                                    else
                                        temposADDinamico.get( porcentagemAtual ).adicionaATempoExistente( tempoLocalAD );
                                        
                                    g.recuperaGrafoOriginal( grafoOrignal );
                                }
                            }
                            else if ( modoAtual == ModosAD.AUMENTAR )
                            {
                                tempoGlobalAEstrelaDinamico = 0;
                                tempoLocalAEstrelaDinamico = 0;
                                for ( int m = 0; m < porcentagem.length; m++ )
                                {
                                    double porcentagemAtual = porcentagem[ m ];
                                    tempoLocalAEstrelaDinamico = 0;
                                    tempoGlobalAEstrelaDinamico = 0;
                                    for ( int k = 0; k < NUM_INSTANCIAS_CALCULADAS; k++ )
                                    {
                                        tempoLocalAEstrelaDinamico = 0;
                                        porcentagemAtual /= 100;
                                        int numeroVezes = (int) (g.getNumeroVertices() * porcentagemAtual);
                                        for ( int n = 0; n < numeroVezes; n++ )
                                            g.alteraPesosArestasGrafoPublico( true, false );
                                        porcentagemAtual *= 100;
                                        for ( int l = 0; l < NUM_RODADAS; l++ )
                                        {
                                            Instant startAEstrelaDinamico = Instant.now();
                                            g.algoritmoAEstrela( 0 , verticeEscolhido, false, false );
                                            Instant endAEstrelaDinamico = Instant.now();
                                        
                                            tempoLocalAEstrelaDinamico += Duration.between(startAEstrelaDinamico, endAEstrelaDinamico).toNanos();
                                        }
                                        tempoLocalAEstrelaDinamico /= NUM_RODADAS;
                                        tempoGlobalAEstrelaDinamico = tempoLocalAEstrelaDinamico;
                                    
                                        long numeroVerticesAbertos = g.contaNumeroDeVerticesAbertosAEstrela( 0 , verticeEscolhido );
                                        // Para o caso do modo de aumentar o peso das Arestas, o valor da chave será multiplicado por 100 para diferenciar das chvaves de diminuir o peso
                                        double chaveRegistradora = porcentagemAtual * 100;
                                        if ( !resultadosADinamico.containsKey( chaveRegistradora ) )
                                            resultadosADinamico.put( chaveRegistradora, new GuardaTempo( chaveRegistradora, tempoGlobalAEstrelaDinamico, numeroVerticesAbertos ) );
                                        else
                                        {
                                            resultadosADinamico.get( chaveRegistradora ).adicionaATempoExistente( tempoGlobalAEstrelaDinamico );
                                            resultadosADinamico.get( chaveRegistradora ).adicionaAVerticesAbertosExistentes(numeroVerticesAbertos);
                                        }
                                    }                                        
                                    g.recuperaGrafoOriginal( grafoOrignal );
                                    
                                    Instant startAD = Instant.now();
                                    long tempoASerDebitadoAD = g.dynamicSearchAEstrela( 0, verticeEscolhido, EPISOLON_INICIAL, FATOR_DE_CORTE, NUM_INSTANCIAS_CALCULADAS, true, true, porcentagemAtual, false );
                                    Instant endAD = Instant.now();
                                    tempoLocalAD = Duration.between( startAD, endAD ).toNanos();
                                    tempoLocalAD -= tempoASerDebitadoAD;
                                    
                                    double chaveRegistradora = porcentagemAtual * 100;
                                    if ( !temposADDinamico.containsKey( chaveRegistradora ) )
                                        temposADDinamico.put( chaveRegistradora, new GuardaTempo( chaveRegistradora, tempoLocalAD, 0) );
                                    else
                                        temposADDinamico.get( chaveRegistradora ).adicionaATempoExistente( tempoLocalAD );
                                        
                                    g.recuperaGrafoOriginal( grafoOrignal );
                                }
                            }
                            g.recuperaGrafoOriginal( grafoOrignal );
                        }
                        
                        g.recuperaGrafoOriginal(grafoOrignal);
                    }
                    
                    Set<Double> chavesAD = temposADDinamico.keySet();
                    for ( Double e: chavesAD )
                    {
                        long tempoTotalAD = temposADDinamico.get(e).getTempoAssociado() / NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                        long tempoTotalAEstrelaDinamico = resultadosADinamico.get(e).getTempoAssociado() / NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                        long nvaA = resultadosADinamico.get(e).getVerticesAberto() / NUM_VERTICES_ESCOLHIDOS_ALEATORIOS;
                        String modo;
                        if ( e == 0 )
                            modo = "Normal";
                        else if ( e  < 100 )
                            modo = "Diminui";
                        else
                        {
                            modo = "Aumenta";
                            e /= 100;
                        }
                        e /= 100;
                        escreveDadosTestesAD( filePath.getFileName().toString(), g.getNumeroVertices(), g.getNumeroArestas(), e, modo, tempoTotalAEstrelaDinamico, nvaA, tempoTotalAD, 0);
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
