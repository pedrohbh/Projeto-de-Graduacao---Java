/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.beta;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class AEstrela
{
    private static long xCordenadas[];
    private static long yCordenadas[];
    private static Scanner input;
    private static Grafo grafo = new Grafo();
    
    private static void abreArquivo( String nomeArquivo )
    {
        try
        {
            input = new Scanner(Paths.get(nomeArquivo));            
        }
        catch ( IOException e )
        {
            System.err.println("Erro ao abrir arquico de entrada \"" + nomeArquivo + "\". Certifique-se de que ele existe ou o seu endereço foi digitado corretamente.%nEncerrando programa" );
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void leDados( String nome )
    {
        System.out.print("Iniciando a leitura de cordenadas... ");
        try
        {
            while ( input.hasNext() )
            {
                String avaliador = input.next();
                if ( avaliador.equals( "v" ) )
                {
                    int indice = input.nextInt() - 1;
                    xCordenadas[ indice ] = input.nextLong();
                    yCordenadas[ indice ] = input.nextLong();
                }
                else
                    avaliador = input.nextLine();
            }
            System.out.println("Feito.");
        }
        catch ( Exception e )
        {
            System.err.println("Erro ao ler os dados de entrada do arquivos de cordenadas. Encerrando o programa");
            e.printStackTrace();
            System.exit( 1 );
        }
        
    }
    
    private static void fechaArquivo()
    {
        if ( input != null )
            input.close();
    }
    
    
    public static void leArquivoDeCordenadas( String nome )
    {
        xCordenadas = new long[ grafo.getNumeroVertices() ];
        yCordenadas = new long[ grafo.getNumeroVertices() ];
        
        abreArquivo(nome);
        leDados(nome);
        fechaArquivo();
        
    }
    
    
    public static void main(String[] args) 
    {
        String caminho = "/home/administrador/Documentos/Trabalhos/Projeto de Graduação/PG-Codigo/Testes/";
        String nomeEntrada = "USA-road-d.NY.gr";
        String nomeCordenadas = caminho + "USA-road-d.NY.co";
        caminho = caminho + nomeEntrada;
        
        grafo.leArquivoEntrada( caminho );
        leArquivoDeCordenadas( nomeCordenadas );
        System.out.println("OK");
        
    }
    
}
