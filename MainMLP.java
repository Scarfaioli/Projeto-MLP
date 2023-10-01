
import java.util.Arrays;
import java.util.Random;

public class MainMLP {

	public static void main(String[] args) {
		Reader r = new Reader();
		Amostra[] database = r.readDataSet();

		MLP rna = new MLP(database[0].input.length,
			(int) (database[0].input.length + database[0].output.length) / 2,
			database[0].output.length, 0.3);

		randomizarBase(database);
			
		for (int e = 0; e < 10000; e++) {
			double erroApEpocaTreino = 0;
			double erroClEpocaTreino = 0;

			double erroApEpocaTeste = 0;
			double erroClEpocaTeste = 0;

			
			for (int a = 0; a < database.length; a++) {
				double[] x = database[a].input;
				double[] y = database[a].output;
				
				/*
				  100 treino 43 teste cp || 54 treino 23 teste im || 35 treino 17 teste pp
				*/
				if(a > 100 && a < 143 || a > 197 && a < 220 || a > 255){
					double[] out = rna.teste(x, y);
					double erroApAmostra = 0;

					for (int j = 0; j < out.length; j++) {
						erroApAmostra +=  Math.abs(y[j] - out[j]);
					}
					
					double erroClAmostra = 0;
					double[] ot = MainMLP.getOutThreshold(out);
					double soma = 0;
					for (int j = 0; j < out.length; j++) {
						soma += Math.abs(y[j] - ot[j]);
					}
					if (soma > 0){
						erroClAmostra = 1;
					}
					erroApEpocaTeste += erroApAmostra;
					erroClEpocaTeste += erroClAmostra;
				}else{
					double[] out = rna.treinar(x, y);
					double erroApAmostra = 0;

					for (int j = 0; j < out.length; j++) {
						erroApAmostra +=  Math.abs(y[j] - out[j]);
					}
					
					double erroClAmostra = 0;
					double[] ot = MainMLP.getOutThreshold(out);
					double soma = 0;
					for (int j = 0; j < out.length; j++) {
						soma += Math.abs(y[j] - ot[j]);
					}
					if (soma > 0){
						erroClAmostra = 1;
					}
					erroApEpocaTreino += erroApAmostra;
					erroClEpocaTreino += erroClAmostra;
				}
			}



			System.out.printf("Epoca: %6d - erroApEpocaTreino: %.10f - erroClEpocaTreino: %d"+
			 		"- erroApEpocaTeste: %.10f - erroClEpocaTeste: %d \n", e, erroApEpocaTreino, 
					(int) erroClEpocaTreino, erroApEpocaTeste, (int) erroClEpocaTeste);
		}
	}

	private static void randomizarBase(Amostra[] baseTreino) {
		Random random = new Random();
		int[][] ordenacp = new int[143][2];
		int[][] ordenaim = new int[77][2];
		int[][] ordenapp = new int[52][2];
		Amostra cp[] = Arrays.copyOfRange(baseTreino, 0, 143);
		Amostra im[] = Arrays.copyOfRange(baseTreino, 143, 220);
		Amostra pp[] = Arrays.copyOfRange(baseTreino, 220, 272);
		
		for(int i=0; i<ordenacp.length; i++){
			ordenacp[i][0] = i;
			ordenacp[i][1] = random.nextInt(300);
		}

		for(int i=0; i<ordenaim.length; i++){
			ordenaim[i][0] = i;
			ordenaim[i][1] = random.nextInt(300);
		}

		for(int i=0; i<ordenapp.length; i++){
			ordenapp[i][0] = i;
			ordenapp[i][1] = random.nextInt(300);
		}

		for(int i=0; i<ordenacp.length; i++){
			for(int j=i; j<ordenacp.length-1; j++){
				if(ordenacp[j][1] <= ordenacp[j+1][1]){
					int aux0 = ordenacp[j][0];
					int aux1 = ordenacp[j][1];
					ordenacp[j][0] = ordenacp[j+1][0];
					ordenacp[j][1] = ordenacp[j+1][1];
					ordenacp[j+1][0] = aux0;
					ordenacp[j+1][1] = aux1;
				}
			}
		}

		for(int i=0; i<ordenaim.length; i++){
			for(int j=i; j<ordenaim.length-1; j++){
				if(ordenaim[j][1] <= ordenaim[j+1][1]){
					int aux0 = ordenaim[j][0];
					int aux1 = ordenaim[j][1];
					ordenaim[j][0] = ordenaim[j+1][0];
					ordenaim[j][1] = ordenaim[j+1][1];
					ordenaim[j+1][0] = aux0;
					ordenaim[j+1][1] = aux1;
				}
			}
		}

		for(int i=0; i<ordenapp.length; i++){
			for(int j=i; j<ordenapp.length-1; j++){
				if(ordenapp[j][1] <= ordenapp[j+1][1]){
					int aux0 = ordenapp[j][0];
					int aux1 = ordenapp[j][1];
					ordenapp[j][0] = ordenapp[j+1][0];
					ordenapp[j][1] = ordenapp[j+1][1];
					ordenapp[j+1][0] = aux0;
					ordenapp[j+1][1] = aux1;
				}
			}
		}

		for(int i=0; i<cp.length; i++){
			Amostra aux = cp[i];
			cp[i] = cp[ordenacp[i][0]];
			cp[ordenacp[i][0]] = aux;
		}

		for(int i=0; i<im.length; i++){
			Amostra aux = im[i];
			im[i] = im[ordenaim[i][0]];
			im[ordenaim[i][0]] = aux;
		}

		for(int i=0; i<pp.length; i++){
			Amostra aux = pp[i];
			pp[i] = pp[ordenapp[i][0]];
			pp[ordenapp[i][0]] = aux;
		}
		
		for(int i=0; i<cp.length; i++){
			baseTreino[i] = cp[i];
		}

		for(int i=0; i<im.length; i++){
			baseTreino[i+143] = im[i];
		}

		for(int i=0; i<pp.length; i++){
			baseTreino[i+220] = pp[i];
		}

	}

	private static double[] getOutThreshold(double[] out) {
		double[] ret = new double[out.length];
		for (int i = 0; i < ret.length; i++) {
			if (out[i] >= 0.5) {
				ret[i] = 1;
			} else {
				ret[i] = 0;
			}
		}
		return ret;
	}

}
