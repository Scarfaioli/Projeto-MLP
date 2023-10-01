public class MLP {
	
	private double [][] wh;
	private double [][] wo;
	
	private int qtdIn, qtdH, qtdOut;
	
	private double ni;
	
	public MLP(int qtdIn, int qtdH, int qtdOut, double ni) {
		this.qtdIn = qtdIn;
		this.qtdH = qtdH;
		this.qtdOut = qtdOut;
		this.ni = ni;
		
		this.wh = new double [this.qtdIn+1][this.qtdH];
		this.wo = new double [this.qtdH+1][this.qtdOut];
		
		setPesosAleatorios(this.wh);
		setPesosAleatorios(this.wo);
	}
	
	private void setPesosAleatorios(double [][] w) {
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[0].length; j++) {
				w[i][j] = ((Math.random() * 0.3) - 0.3);
			}
		}
	}
	
	public double[] treinar(double [] xIn, double [] y) {
		//Acrescentar o bias
		double [] x = new double[xIn.length + 1];
		for (int i = 0; i < xIn.length; i++) {
			x[i] = xIn[i];
		}
		x[x.length-1] = 1;
		
		
		//Calcular a saida da camada H
		double [] H = new double[this.qtdH+1];
		for (int j = 0; j < H.length-1; j++) {
			double soma = 0;
			for (int i = 0; i < x.length; i++) {
				soma = soma + wh[i][j] * x[i];
			}
			H[j] = 1 / (1 + Math.exp(-soma));
		}
		H[H.length-1] = 1;
		
		
		//Calcular a saida da camada out
		double [] out = new double[this.qtdOut];
		for (int j = 0; j < out.length; j++) {
			double soma = 0;
			for (int i = 0; i < H.length; i++) {
				soma = soma + wo[i][j] * H[i];
			}
			out[j] = 1 / (1 + Math.exp(-soma));
		}

		//Calcular os deltas da camada out
		double [] Do = new double[this.qtdOut];
		for (int j = 0; j < Do.length; j++) {
			Do[j] = out[j] * (1 - out[j]) * (y[j] - out[j]);
		}
		
		double [] Dh = new double[this.qtdH];
		for (int i = 0; i < Dh.length; i++) {
			double soma = 0;
			for (int j = 0; j < Do.length; j++) {
				soma = soma + Do[j] * wo[i][j];
			}
			
			Dh[i] = H[i] * (1 - H[i]) * soma;
		}
		
		for (int i = 0; i < H.length; i++)
			for (int j = 0; j < out.length; j++)
				wo[i][j] = wo[i][j] + ni * Do[j] * H[i];

		for (int i = 0; i < x.length; i++)
			for (int j = 0; j < H.length-1; j++)
				wh[i][j] = wh[i][j] + ni * Dh[j] * x[i];
		
		return out;
	}

	 public double[] teste(double[] xIn, double[] y){
            
        //Faz acrescimo do bias
        double[] x = new double[xIn.length+1];
        for (int i = 0; i < xIn.length; i++) {
            x[i] = xIn[i];
        }
        x[x.length-1] = 1;

        //Calcula a saída da camada h
        double[] h = new double[this.qtdH+1]; 
        for(int i=0; i<h.length-1; i++){
            for (int j = 0; j < x.length; j++) {
                h[i] += x[j] *wh[j][i];
            }
            h[i] = 1/(1+ Math.exp(-h[i]));
        }
        h[this.qtdH] = 1;

        //Calcula a saída da camada out
        double[] out = new double[this.qtdOut];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < h.length; j++) {
                out[i] += h[j]*wo[j][i];
            }
            out[i] = 1/(1+ Math.exp(-out[i]));
        }

        return out;
    }
}
