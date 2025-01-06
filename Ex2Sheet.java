package assignments.ex2;

import java.util.Dictionary;
import java.io.IOException;
import java.util.Hashtable;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // Add your code here

    // ///////////////////
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
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;

        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
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

    String formulaRegex = "^=\\s*([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\))([+\\-*/]([A-Z]+\\d+|\\d+(\\.\\d+)?|\\([\\s\\S]+\\)))*$";
    String coordRegex = "^[A-Z]+[1-9]\\d*$";

    @Override
    public String eval(int x, int y) {

        Cell c = get(x, y);
        String ans = null;
        if (c != null) {
            ans = c.toString();
        } else {
            return ans;
        }
        if (ans.matches(formulaRegex)) {
            //is a formula so it needs to be calculated
        }

        return ans;
    }

    public boolean isNumber(String s)
    {
        return s.matches("[^[-+]?\\d+(\\.\\d+)?$]");
    }

    public double computeFormula(String formula)
    {

        return 0;
    }

    public double cutParentheses(String formula) {
        double ans = 0;
        int startIndex = 0;
        int endIndex = 0;
        String[] formulaParts = new String[10];
////////TODO replace any position string "A14" with evaL for a specific coordinate
        while (formula.contains("(") && formula.contains(")")) {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    startIndex = i;
                }
            }
            for (int i = startIndex; i < formula.length(); i++) {
                if (formula.charAt(i) == ')') {
                    endIndex = i+1;
                    break;
                }
            }
            formulaParts[0] = formula.substring(startIndex, endIndex);
        }

        return ans;
    }

}
