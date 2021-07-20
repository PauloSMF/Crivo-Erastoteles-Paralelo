import java.util.Scanner;

public class Crivo_Thread extends Thread{
    private int index;  //Primo referente a operação da thread
    private int n;  //Tamanho do vetor
    private Memoria memoria;    //Instanciando memória para operções na classe

    //Construtor padrão
    public Crivo_Thread(int n, Memoria memoria){
        this.n = n;
        this.index = -1;    //Index começa com -1, será manipulado com auxílio de um semáforo
        this.memoria = memoria;
    }

    //Método para obtenção do primo concorrente, fazendo o uso de semáforos
    private void SetIndex() throws InterruptedException{
        memoria.Bloquear();//Bloqueando a região crítica para obter o primo concorrente
         
        do{ //Enquanto não se encontra uma posição não marcada (próximo primo concorrente) 
            //e o múltiplo desta está no vetor
            if(index == -1 && memoria.GetPosicao(memoria.GetPrimoConcorrente())){ //Caso o número não está marcado
                index = memoria.GetPrimoConcorrente();  //A thread pega o primo concorrente
                memoria.AtualizarPrimoConcorrente();    //e atualiza para a próxima
            }
            else
                memoria.AtualizarPrimoConcorrente();  //Continua procurando por possições vazias
        }while(index == -1 && memoria.GetPrimoConcorrente()*memoria.GetPrimoConcorrente() <= n);

        memoria.Desbloquear();  //Desbloqueando a região crítica
    }

    //Algoritmo executado pela thread
    public void run(){
        while(index*index <= n){    //Enquanto o múltiplo do index recebido está no vetor 
            try{ SetIndex();    //Procurando um novo índice para marcar os múltiplos
            }catch(InterruptedException e) {e.printStackTrace();}

            if(index != -1){    //Caso o index não retornou com -1
                //Procurando por múltiplos do index começando por index^2
                for(int j = index*index; j <= n; j = j + index)
                    memoria.SetPosicaoFalso(j); //Marcando o múltiplo encontrado
            }
            else    //Caso o index tenha retornado com -1 
                break;  //finaliza a operação

            index = -1; //Definindo o index para -1 para uma nova consulta
        }
    }

    public static void main(String[] args) throws InterruptedException{
        Scanner teclado = new Scanner(System.in);
        int num_threads;    //Número de threads definido por usuário20
        int num;    //Limite do vetor de números para se crivar
        int imprimir;   //Opção do usuário para imprimir ou não os números primos
                        //1-Sim e 2-Não
        System.out.println("Entre com a quantidade de threads: ");
		num_threads = teclado.nextInt();

        System.out.println("Entre com a quantidade de números: ");
		num = teclado.nextInt();
        //Criando a memória compartilhada
        Memoria memoria = new Memoria(num);
        //Criando as threads
        Crivo_Thread[] threads = new Crivo_Thread[num_threads];
        for(int j = 0; j < num_threads; j++) 
            threads[j] = new Crivo_Thread(num, memoria);

        long tempo1 = System.nanoTime();    //Tempo do início da operação
		//Iniciando as threads
		for(int j = 0; j < num_threads; j++)
			threads[j].start();
		//Definindo um ponto de sincronismo (barreira) 
		//Aguardando o termino do processamento de todas as threads
		for(int j = 0; j < num_threads; j++)
			threads[j].join();

        long tempo2 = System.nanoTime();    //Tempo do fim da operação
        //Impressão do tempo
        System.out.println("\nTempo = " + String.valueOf((tempo2 - tempo1)/1000000) + " ms");
        //Impressão dos números primos
        System.out.println("\nDeseja imprimir os números primos? (1-Sim/2-Não) ");
        imprimir = teclado.nextInt();

        teclado.close();

        if(imprimir == 1)
            memoria.Imprimir();
    }
}
