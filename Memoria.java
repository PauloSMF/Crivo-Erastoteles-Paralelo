import java.util.concurrent.Semaphore;

public class Memoria{
    private boolean numeros_primos[];   //Vetor de números primos
    private int primo_concorrente;  //Primo concorrente atual
    private Semaphore semaforo;     //Semáforo para controle do primo concorrente
    private int n;  //Tamanho do vetor

    //Construtor padrão
    public Memoria(int n){
        this.n = n;
        this.primo_concorrente = 2;  //Primo concorrente inicia em 2
        this.semaforo = new Semaphore(1); //Iniciando o semáforo
        
        //Iniciando o vetor de números
        numeros_primos = new boolean[n+1];
        for(int i = 0; i <= n; i++){
            if(i != 0 && i != 1)
                numeros_primos[i] = true;
            else
                numeros_primos[i] = false;
        }
    }

    //Getter e atualizar referentes ao primo concorrente
    public int GetPrimoConcorrente(){return primo_concorrente;}
    public void AtualizarPrimoConcorrente(){primo_concorrente = primo_concorrente + 1;}

    //Bloqueio e desbloqueio do semáforo
    public void Bloquear() throws InterruptedException{semaforo.acquire();}
    public void Desbloquear(){semaforo.release();}

    //Getter e setter para o vetor booleano
    public boolean GetPosicao(int i){return numeros_primos[i];}
    public void SetPosicaoFalso(int i){numeros_primos[i] = false;}

    //Impressão do vetor com os números primos
    public void Imprimir(){
        System.out.print("\nNúmeros primos: ");
        for(int i = 0; i <= n; i++){
            if(numeros_primos[i] == true)
                System.out.print(i + " ");
        }
        System.out.print("\n\n");
    }
}
