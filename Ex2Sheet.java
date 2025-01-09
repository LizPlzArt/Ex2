package assignments.ex2;
import java.io.IOException;
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

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;

        //format validation
        if (!cords.matches("[A-Z]+\\d+")) {
            System.out.println("invalid input");
            return ans;
        }

        char xChar = cords.charAt(0);
        int x = xChar - 64;
        if (x <= 0 || x > 26) {
            System.out.println("invalid input");
            return ans;
        }

        //split the input to letter and number
        String[] parts = cords.split("(?<=\\D)(?=\\d)");
        int y = Integer.parseInt(parts[1]);
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
        int[][] ans = new int[width()][height()];
        init1(ans);

        int depth=0;
        int count = 0;
        int max = width()*height();
        boolean flagC = true;

        while(count<max&&flagC)
        {
            flagC=false;
            for (int x = 0; x < width(); x++)
            {
                for (int y = 0; y < height(); y++)
                {
                    if(canBeComputed(x,y))
                    {
                        ans[x][y] = depth;
                        count++;
                        flagC=true;
                    }
                }
            }
            depth++;
        }




        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y)
    {
        Cell c = get(x, y);
        return eval(c);
    }

    public String eval(Cell c)
    {
        String ans = "";

        if (c != null)
        {
            ans = c.getData();
        }
        if (isFormula(ans))
        {
            return eval(ans);
        }
        if (isCoordinate(ans))
        {
            return replaceCoord(ans);
        }
        else
        {
            return ans;
        }

    }

    public String eval(String formula)
    {
        String ans = formula;
        String ans1 = cutParentheses(ans);
        String ans2 = replaceCoord(ans1);
        String ans3 = String.valueOf(computeFormula(ans2));
        return ans3;

    }

    @Override
    public void eval() {
        int[][] dd = depth();
        for (int x = 0; x < width(); x++)
        {
            for (int y = 0; y < height(); y++)
            {
                eval(x, y);
            }
        }
    }


    public static boolean isNumber(String s) {
        return s.matches("^-?\\d+(\\.\\d+)?$");
    }

    public static boolean isCoordinate(String s)
    {
        return s.matches("\\b[A-Z]+[0-9]+\\b");
    }

    public boolean isText(String s)
    {
        boolean ans = true;
        if(Ex2Sheet.isNumber(s))
        {
            ans=false;
        }
        else if (Ex2Sheet.isCoordinate(s))
        {
            ans=false;
        }
        else if(Ex2Sheet.isFormula(s))
        {
            ans=false;
        }
        return ans;
    }

    public static boolean isFormula(String formula)
    {
        boolean ans= false;
        String formulaRegex = "^=\\s*([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\))([+\\-*/]([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\)))*$";
        if (formula.matches(formulaRegex))
        {
            ans = true;
        }
        return ans;
    }

    public static double computeFormula(String formula) {
        double ans = -1;
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

            if (operatorIndex != -1)
            {
                a = formula.substring(0, operatorIndex);
                b = formula.substring(operatorIndex + 1);
                operator = String.valueOf(formula.charAt(operatorIndex));
            }

            a= a.trim();
            b= b.trim();
            operator= operator.trim();

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

/////TODO works in tests, doesnt work in spreadsheet    
    public String replaceCoord(String formula)
    {
        String cellCoord="";
        Pattern coordPattern = Pattern.compile("\\b[A-Z]+[0-9]+\\b");
        Matcher matcher = coordPattern.matcher(formula);

        StringBuffer newFormula = new StringBuffer();
        while (matcher.find())
        {
            //get the coordinates of the cell
            cellCoord = matcher.group();
            Cell c= get(cellCoord);
            String b = c.getData();
////////TODO found the problem. it gets fucked up if the cell doesnt exist

            //get the value of the cells content
            String replacement = String.valueOf(c.getData());

            //replace the reference with the value
            matcher.appendReplacement(newFormula, replacement);
        }

        //transfer string buffer type to string type
        formula= String.valueOf(matcher.appendTail(newFormula));
        return formula;
    }

    public static String cutParentheses(String formula) {
        String ans = "";
        int startIndex = 0;
        int endIndex = 0;

        if(formula.matches("^=.*$"))
        {
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
            String inParentheses = formula.substring(startIndex+1, endIndex-1);
            double replacement = computeFormula(inParentheses);
            formula =formula.substring(0, startIndex) + replacement + formula.substring(endIndex);

        }

        ans = formula;
        return ans;
    }

    public void init1(int[][] ans)
    {
        for (int x = 0; x < width(); x++)
        {
            for(int y = 0; y < height(); y++)
            {
                ans[x][y] = -1;
            }
        }
    }

    public boolean canBeComputed(int x,int y)
    {
        boolean ans = false;
        if(isNumber(value(x,y)) || isNumber(value(x,y)))
        {
            ans = true;
        }
        return ans;
    }

}
