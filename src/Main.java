import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.StringTokenizer;

public class Main {


    private static final URI FILE_NAME = URI.create("file:///Gcode.nc");

    private static Double zAxis(double x1, double x2, double y1, double y2) {
        Double x = x2 - x1, y = y2 - y1;
        Double z = 0d;
        if (x > 0 & y > -0) {
            z = Math.atan(y / x);
        } else if (x > 0 & y < 0) {
            z = Math.atan(y / x) + 2 * Math.PI;
        } else if (x < 0) {
            z = Math.atan(y / x) + Math.PI;
        } else if (x == 0 & y > 0) {
            z = Math.PI / 2;
        } else if (x == 0 & y < 0) {
            z = 3 * Math.PI / 2;
        } else z = 0d;

        return Math.toDegrees(z);
    }


    public static void main(String[] args) throws IOException {
        NumberFormat nf = new DecimalFormat("#.###");
        //System.out.println(nf.format(0.012124));

        Double X1 = 0D,
                X2 = 0D,
                Y1 = 0D,
                Y2 = 0D,
                Z = 0D,
                R = 0D;
        String moveType = "Rapid";

        String raw;
        List<String> lines = Files.readAllLines(Paths.get(FILE_NAME), StandardCharsets.UTF_8);
        for (String line : lines) {
            X2 = X1;
            Y2 = Y1;
            StringTokenizer st = new StringTokenizer(line, " ");
            while (st.hasMoreTokens()) {
                // Выводим лексемы в консоль
                raw = st.nextToken().replaceFirst("^0+(?!$)", "");
                raw = raw.replaceAll("G0.", "G" + raw.charAt(raw.length() - 1));
                //System.out.println(raw);

                switch (raw.charAt(0)) {
                    case 'G':
                        switch (raw.charAt(1)) {
                            case '0':
                                moveType = "Rapid";
                                break;
                            case '1':
                                moveType = "Feed";
                                break;
                            case '2':
                                moveType = "CW";
                                break;
                            case '3':
                                moveType = "CCW";
                                break;
                        }
                        break;
                    case 'X':

                        X1 = Double.parseDouble(raw.substring(1));
                        //System.out.println("X= "+X1);
                        break;
                    case 'Y':
                        Y1 = Double.parseDouble(raw.substring(1));
                        //System.out.println("Y= "+raw.substring(1));
                        break;
                    case 'Z':
                        Z = Double.parseDouble(raw.substring(1));
                        //System.out.println("Z= "+raw.substring(1));
                        break;
                    case 'R':
                        R = Double.parseDouble(raw.substring(1));
                        //System.out.println("R= "+raw.substring(1));
                        break;
                }

            }
            System.out.println(moveType);
            System.out.println("X " + nf.format(X1));
            //System.out.println("X2 "+X2);
            System.out.println("Y " + nf.format(Y1));
            //System.out.println("Y2 "+Y2);
            System.out.println("Z " + nf.format(zAxis(X2, X1, Y2, Y1)));
            System.out.println("R " + nf.format(R));
            System.out.println("Конец строки");

        }
    }
}
