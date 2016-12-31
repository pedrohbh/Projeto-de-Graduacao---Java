/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedro.pg.utilitario;

import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import pedro.pg.grafo.Grafo;

/**
 *
 * @author administrador
 */
public class GraphViz 
{
    private static Formatter output;
    
    private static void openFile( String nome )
    {
        try
        {
            output = new Formatter( nome );
        }
        catch ( SecurityException e )
        {
            System.err.println("Permissão de escrita não concendida. Verifique os privilégios de uso do arquivo");
            e.printStackTrace();
            System.exit( 1 );
        }
        catch ( FileNotFoundException f )
        {
            System.err.println("Erro ao abrir arquivo de entrada.");
            f.printStackTrace();
            System.exit( 1 );
        }
        
    }
    
    private static void closeFile()
    {
        if ( output != null )
            output.close();
    }
    
    
    public static void desenhaGrafo( String nomeArquivo, Grafo g )
    {
        String []split = null;
        String caminho = "";
        if ( nomeArquivo.contains("/") )
        {
            split = nomeArquivo.split("/");
            nomeArquivo = split[ split.length - 1 ];
            for ( int i = 0; i < split.length - 1; i++ )
                caminho += split[ i ] + "/";
            
        }
        split = nomeArquivo.split("\\.");
        String novoNome = split[ 0 ];
        novoNome += ".dot";
        novoNome = caminho + novoNome;
        openFile(novoNome);
        try
        {
            output.format("digraph {%n" );
            output.format("  rankdir=LR%n");
            output.format("  size=\"4,3\"%n");
            output.format("  ratio=\"fill\"%n");
            output.format("  edge[style=\"bold\"]%n" + "  node[shape=\"circle\"]%n");
            for ( Grafo.Vertice v : g.getVerticesGrafo() )
            {
                for ( Grafo.Aresta a : v.getArestasAdjacentes() )
                {
                    output.format("%d -> %d[label=\"%d\"];%n", a.getIdVeticeOrigem(), a.getIdVerticeDestino(), a.getPeso(), a.getPeso());
                }
            }
            
            output.format("}");
        }
        catch ( FormatterClosedException e )
        {
            System.err.println("Erro ao escreve em arquivo \"" + nomeArquivo + "\". Encerrando o programa");
            System.exit( 1 );
        }
        
        closeFile();
        
        
        
        
    }
    
}
