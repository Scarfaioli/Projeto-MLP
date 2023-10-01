import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    public Amostra[] readDataSet() {
        Amostra amostras[] = new Amostra[272];
        double input[] = new double[7];
        double output[] = new double[3];

        try {
            FileReader file = new FileReader("ecoli.data");
            BufferedReader fr = new BufferedReader(file);
            String line;
            int amostra = 0;
            while ((line = fr.readLine()) != null) {
                String[] s = line.split("\\s+");
               
                for(int i=1; i<s.length; i++){
                    if(i==(s.length-1)){
                        switch(s[i]){
                            case "cp":
                                output[0]=1;
                                output[1]=0;
                                output[2]=0;
                                break;
                            case "pp":
                                output[0]=0;
                                output[1]=1;
                                output[2]=0;
                                break;
                            case "im":
                                output[0]=0;
                                output[1]=0;
                                output[2]=1;
                                break;
                        }
                    }else{
                        input[i-1] = Double.parseDouble(s[i]);
                    }
                }
                amostras[amostra] = new Amostra(input, output);
                amostra++;
            }

            file.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        return amostras;
    }
}
