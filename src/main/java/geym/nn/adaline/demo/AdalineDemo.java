package geym.nn.adaline.demo;

import geym.nn.adaline.Adaline;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.IterativeLearning;

import java.util.Arrays;

public class AdalineDemo implements LearningEventListener {

    public final static int CHAR_WIDTH = 5;
    public final static int CHAR_HEIGHT = 7;

    public static String[][] DIGITS = {
            { " OOO ",
              "O   O",
              "O   O",
              "O   O",
              "O   O",
              "O   O",
              " OOO "  },

            { "  O  ",
              " OO  ",
              "O O  ",
              "  O  ",
              "  O  ",
              "  O  ",
              "  O  "  },

            { " OOO ",
              "O   O",
              "    O",
              "   O ",
              "  O  ",
              " O   ",
              "OOOOO"  },

            { " OOO ",
              "O   O",
              "    O",
              " OOO ",
              "    O",
              "O   O",
              " OOO "  },

            { "   O ",
              "  OO ",
              " O O ",
              "O  O ",
              "OOOOO",
              "   O ",
              "   O "  },

            { "OOOOO",
              "O    ",
              "O    ",
              "OOOO ",
              "    O",
              "O   O",
              " OOO "  },

            { " OOO ",
              "O   O",
              "O    ",
              "OOOO ",
              "O   O",
              "O   O",
              " OOO "  },

            { "OOOOO",
              "    O",
              "    O",
              "   O ",
              "  O  ",
              " O   ",
              "O    "  },

            { " OOO ",
              "O   O",
              "O   O",
              " OOO ",
              "O   O",
              "O   O",
              " OOO "  },

            { " OOO ",
              "O   O",
              "O   O",
              " OOOO",
              "    O",
              "O   O",
              " OOO "  } };

    public static DataSetRow createTrainDataRow(String[] image, int idealValue){
        double[] output=new double[DIGITS.length];
        Arrays.fill(output, -1);

        double[] input=image2data(image);

        output[idealValue]=1;
        return new DataSetRow(input, output);
    }

    private static double[] image2data(String[] image) {
        double[] input=new double[CHAR_WIDTH*CHAR_HEIGHT];
        for (int row=0; row<CHAR_HEIGHT;row++){
            for (int col=0;col<CHAR_WIDTH;col++){
                int index=(row*CHAR_WIDTH)+col;
                char ch=image[row].charAt(col);
                input[index]=ch=='O'?1:-1;
            }
        }

        return input;
    }

    public static int maxIndex(double[] data){
        int result=-1;
        for (int i=0; i<data.length;i++){
            if (result==-1||data[i]>data[result]){
                result = i;
            }
        }

        return result;
    }

    public static void printDigit(String[] digit){
        for (int i=0; i<digit.length; i++){
            if (i==digit.length-1){
                System.out.print(digit[i]+"===>");
            }else{
                System.out.println(digit[i]);
            }
        }
    }

    public static void main(String[] args) {
        Adaline ada=new Adaline(CHAR_HEIGHT*CHAR_WIDTH, DIGITS.length);

        DataSet ds=new DataSet(CHAR_HEIGHT*CHAR_WIDTH, DIGITS.length);

        for (int i=0; i<DIGITS.length;i++){
            ds.add(createTrainDataRow(DIGITS[i],i));
        }

        ada.getLearningRule().addListener(new AdalineDemo());
        ada.learn(ds);

        for (String[] digit : DIGITS) {
            ada.setInput(image2data(digit));
            ada.calculate();
            printDigit(digit);
            System.out.println(maxIndex(ada.getOutput()));
            System.out.println();
        }
    }

    public void handleLearningEvent(LearningEvent event) {
        IterativeLearning bp=(IterativeLearning)event.getSource();
        System.out.println("Iterate:"+bp.getCurrentIteration());
        System.out.println(Arrays.toString(bp.getNeuralNetwork().getWeights()));
    }
}
