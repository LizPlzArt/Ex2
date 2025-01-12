package assignments.ex2;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private static Cell[][] table;


    static char[] supportedOperators = {'+', '-', '*', '/'};

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;

        Cell c = get(x, y);
        if (c != null) {
            ans = c.toString();
        }

        return ans;
    }

    public static int getX(String coord) {
        int x = -1;
        if (!coord.matches("[A-Z]+\\d+")) {
            System.out.println("invalid input");
            return x;
        }
        char xChar = coord.charAt(0);
        x = xChar - 65;
        if (x < 0 || x > 25) {
            System.out.println("invalid input");
        }
        return x;
    }

    public int getY(String coord) {
        int Y = -1;
        if (!coord.matches("[A-Z]+\\d+")) {
            System.out.println("invalid input");
            return Y;
        }
        String[] parts = coord.split("(?<=\\D)(?=\\d)");
        Y = Integer.parseInt(parts[1]);
        return Y;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;

        int x = getX(cords);
        int y = getY(cords);

        ans = get(x, y);
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;

        return ans;
    }

    //////TODO the algorithm is written in the Q&A section
    @Override
    public int[][] depth() {
        int[][] depthArr = new int[width()][height()];
        init1(depthArr);

        int depth = 0;
        int max = width() * height();
        boolean flagC = true;

        while (depth < max && flagC) {
            flagC = false;
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++) {
                    if (depthArr[x][y] == -1 && canBeComputedNow(x, y, depth, depthArr)) {
                        depthArr[x][y] = depth;
                        flagC = true;
                    }
                }
            }
            depth++;
        }


        return depthArr;
    }

    @Override
    public void load(String fileName) throws IOException {
        List<String> fileContent = Files.readAllLines(Paths.get(fileName));

        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                table[x][y].setData("");
            }

        }
        for (int i = 1; i < fileContent.size(); i++)
        {
            String[] split = fileContent.get(i).split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            table[x][y].setData(split[2]);
        }

    }

    @Override
    public void save(String fileName) throws IOException{
        String tableContent = "";
        for (int x = 0; x < width(); x++)
        {
            for (int y = 0; y < height(); y++)
            {
                String cellContent = table[x][y].toString();
                if (!cellContent.isEmpty())
                {
                    tableContent = tableContent + x +"," + y + "," + cellContent + "\n";
                }
            }
        }
        FileWriter myWriter = new FileWriter(fileName);
        myWriter.write(tableContent);
        myWriter.close();
    }

    @Override
    public String eval(int x, int y) {
        Cell c = get(x, y);
        int[][] depth = depth();
        if (depth[x][y] == -1) {
            return Ex2Utils.ERR_CYCLE;
        }
        return eval(c);
    }

    public String eval(Cell c) {
        String ans = "";

        if (c != null) {
            ans = c.getData();
        }
        if (isFormula(ans)) {
            return eval(ans);
        }
        return ans;

    }

    public String eval(String formula) {
        String ans = formula;
        String ans1 = cutParentheses(ans);
        String ans2 = replaceCoord(ans1);
        String ans3 = String.valueOf(computeFormula(ans2));
        if (ans2.equals("-2")) {
            ans3 = Ex2Utils.ERR_FORM;
        }

        return ans3;

    }

    @Override
    public void eval() {
        int[][] dd = depth();


        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {

                eval(x, y);
            }
        }

    }

    public static boolean isNumber(String s) {
        return s.matches("^-?\\d+(\\.\\d+)?$");
    }

    public static boolean isCoordinate(String s) {
        return s.matches("\\b[A-Z]+[0-9]+\\b");
    }

    public boolean isText(String s) {
        boolean ans = true;
        if (Ex2Sheet.isNumber(s)) {
            ans = false;
        } else if (Ex2Sheet.isCoordinate(s)) {
            ans = false;
        } else if (Ex2Sheet.isFormula(s)) {
            ans = false;
        }
        return ans;
    }

    public static boolean isFormula(String formula) {
        boolean ans = false;
        String formulaRegex = "^=\\s*([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\))([+\\-*/]([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\)))*$";
        if (formula.matches(formulaRegex)) {
            ans = true;
        }
        return ans;
    }

    public static double computeFormula(String formula) {
        double ans = -1;
        if (Objects.equals(formula, "")) {
            return -2;
        }

        if (isNumber(formula)) {
            return Double.parseDouble(formula);
        } else {
            int operatorIndex = -1;
            for (int i = 0; i < supportedOperators.length && operatorIndex == -1; i++) {
                operatorIndex = formula.indexOf(supportedOperators[i]);
            }

            String a = "";
            String b = "";
            String operator = "";

            if (operatorIndex != -1) {
                a = formula.substring(0, operatorIndex);
                b = formula.substring(operatorIndex + 1);
                operator = String.valueOf(formula.charAt(operatorIndex));
            }

            a = a.trim();
            b = b.trim();
            operator = operator.trim();

            double resultA = computeFormula(a);
            double resultB = computeFormula(b);

            ans = switch (operator) {
                case "+" -> resultA + resultB;
                case "-" -> resultA - resultB;
                case "*" -> resultA * resultB;
                case "/" -> resultA / resultB;
                default -> ans;
            };
        }

        return ans;
    }

    public static boolean containsCoords(String formula) {
        boolean ans = false;
        Matcher matcher = getCoordsMatcher(formula);
        if (matcher.find()) {
            ans = true;
        }
        return ans;
    }

    public static Matcher getCoordsMatcher(String formula) {
        Pattern coordPattern = Pattern.compile("\\b[A-Z]+[0-9]+\\b");
        Matcher matcher = coordPattern.matcher(formula);
        return matcher;
    }

    public static String extractCoords(String formula) {
        Matcher matcher = getCoordsMatcher(formula);
        matcher.find();
        String ans = matcher.group();
        return ans;
    }

    public static String[] extractAllCoords(String formula) {
        Matcher matcher = getCoordsMatcher(formula);
        int counter = 0;
        for (int i = 0; i < formula.length(); i++) {
            if (matcher.find()) {
                counter++;
            }
        }

        Matcher newMatcher = getCoordsMatcher(formula);

        String[] ans = new String[counter];
        for (int i = 0; i < formula.length(); i++) {
            if (newMatcher.find()) {
                ans[i] = newMatcher.group();
            }
        }
        return ans;
    }

    public String replaceCoord(String formula) {
        String cellCoord = "";

        Matcher matcher = getCoordsMatcher(formula);

        while (containsCoords(formula)) {
            //get the coordinates of the cell
            cellCoord = extractCoords(formula);
            Cell c = get(cellCoord);

            //get the value of the cells content
            String replacement = (String.valueOf(c.getData()));
            if (!(isFormula(replacement) || isNumber(replacement)) || replacement.isEmpty()) {
                return "-2";
            }
            replacement = eval(replacement);

            //replace the reference with the value=
            formula = formula.replace(cellCoord, replacement);
        }

        //transfer string buffer type to string type
        return formula;
    }

    public static String cutParentheses(String formula) {
        String ans = "";
        int startIndex = 0;
        int endIndex = 0;

        if (formula.matches("^=.*$")) {
            formula = formula.substring(1);
        }


        while (formula.contains("(") && formula.contains(")")) {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    startIndex = i;
                }
            }
            for (int i = startIndex; i < formula.length(); i++) {
                if (formula.charAt(i) == ')') {
                    endIndex = i + 1;
                    break;
                }
            }
            String inParentheses = formula.substring(startIndex + 1, endIndex - 1);
            double replacement = computeFormula(inParentheses);
            formula = formula.substring(0, startIndex) + replacement + formula.substring(endIndex);

        }

        ans = formula;
        return ans;
    }

    public void init1(int[][] ans) {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                ans[x][y] = -1;
            }
        }
    }

    public boolean canBeComputedNow(int x, int y, int myDepth, int[][] depthArr) {
        boolean ans = false;
        String content = value(x, y);

        if (!isFormula(content)) {
            if (myDepth == 0 && (isNumber(content) || isText(content))) {
                ans = true;
            } else {
//////TODO then wtf is it
            }
        } else if (!containsCoords(content)) {
            if (myDepth == 1 && isFormula(content)) {
                ans = true;
            }
        } else {
            String[] coordsArr = extractAllCoords(content);
            for (int i = 0; i < coordsArr.length; i++) {
                int coordX = getX(coordsArr[i]);
                int coordY = getY(coordsArr[i]);
                int depthOfCoord = depthArr[coordX][coordY];
                if (depthOfCoord == -1) {
                    return false;
                }

            }
            return true;
        }

        return ans;
    }

}
