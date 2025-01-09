package assignments.ex2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ex2Test {
    @Test
    void computeFormulaTest()
    {
        String[] testFormula = {"1", "1.2", "0.2"," 1+2", "1+2*3", "1+2*3-1"};
        double[] formulaAns = {1, 1.2, 0.2, 3,7,6};
        double ans = -1;
        for (int i = 0; i < testFormula.length; i++)
        {
            ans= Ex2Sheet.computeFormula(testFormula[i]);
            assertEquals(ans, formulaAns[i]);
        }

    }
    @Test
    void isNumberTest()
    {
        String[] numberTest= {"1","4","16","-15","0.2","68","1.2","-6.5","666"};
        boolean isNumber = false;
        for (int i = 0; i < numberTest.length; i++)
        {
            isNumber = Ex2Sheet.isNumber(numberTest[i]);
            assertEquals(isNumber, true);
        }
        String[] notNumberTest= {"1.1.1","G","+2","5-9","/"};
        for (int i = 0; i < notNumberTest.length; i++)
        {
            isNumber = Ex2Sheet.isNumber(notNumberTest[i]);
            assertEquals(isNumber, false);
        }

    }
    @Test
    void cutParenthesesTest()
    {

        String[] formulaWithParentheses= {"=(1+2)*((3))-1","=(3+4)*6","=(5-3)+(8/2)", "=(2+4)*(7-3)", "=(9/3)+(6*2)",  "=(10-2)*(5+3)", "=(8/4)+7", "=(6*3)-(2+4)", "=(12/4)*(3+5)", "=(3+1)*(9-6)"
        };
        String[] formulaWithoutParentheses= {"3.0*3.0-1", "7.0*6","2.0+4.0", "6.0*4.0", "3.0+12.0",  "8.0*8.0", "2.0+7", "18.0-6.0", "3.0*8.0", "4.0*3.0"};
        for (int i = 0; i < formulaWithParentheses.length; i++)
        {
            String solution= Ex2Sheet.cutParentheses(formulaWithParentheses[i]);
            assertEquals(solution, formulaWithoutParentheses[i]);
        }

    }

    @Test
    void replaceCoordTest() {
        // Step 1: Create an instance of the class
        Ex2Sheet sheet = new Ex2Sheet(5,5);
        sheet.set(1,2, "5");
        sheet.set(2,3, "10");

        // ✅ **Test Case 1: Basic Formula Replacement**
        String formula1 = "=(A2+3) * (2+B3)/A2";
        String expected1 = "=(5+3) * (2+10)/5";
        assertEquals(expected1, sheet.replaceCoord(formula1));

        // ✅ **Test Case 2: Formula with Only One Reference**
        String formula2 = "=A2 * 2";
        String expected2 = "=5 * 2";
        assertEquals(expected2, sheet.replaceCoord(formula2));

        // ✅ **Test Case 3: Multiple Same References**
        String formula3 = "=A2 + A2 + A2";
        String expected3 = "=5 + 5 + 5";
        assertEquals(expected3, sheet.replaceCoord(formula3));

        // ✅ **Test Case 4: Different Cell References**
        String formula4 = "=A2 * B3 + 1";
        String expected4 = "=5 * 10 + 1";
        assertEquals(expected4, sheet.replaceCoord(formula4));

        // ✅ **Test Case 5: Cell Reference at Start and End**
        String formula5 = "A2 + B3 + A2";
        String expected5 = "5 + 10 + 5";
        assertEquals(expected5, sheet.replaceCoord(formula5));

        // ✅ **Test Case 6: No Cell References**
        String formula6 = "=5+3*2";
        String expected6 = "=5+3*2"; // Should remain unchanged
        assertEquals(expected6, sheet.replaceCoord(formula6));

        // ✅ **Test Case 7: Empty String Input**
        String formula7 = "";
        String expected7 = "";
        assertEquals(expected7, sheet.replaceCoord(formula7));

        // ✅ **Test Case 8: Cell Reference That Doesn't Exist**
        String formula8 = "=C5 + A2";
        String expected8 = "=0 + 5"; // Assuming missing values default to "0"
        assertEquals(expected8, sheet.replaceCoord(formula8));

        // ✅ **Test Case 9: Formula with Spaces and References**
        String formula9 = "  = A2  +  B3 ";
        String expected9 = "  = 5  +  10 ";
        assertEquals(expected9, sheet.replaceCoord(formula9));

        // ✅ **Test Case 10: Complex Expression with Multiple Operators**
        String formula10 = "=(A2*B3)-(B3/A2)+B3";
        String expected10 = "=(5*10)-(10/5)+10";
        assertEquals(expected10, sheet.replaceCoord(formula10));
    }

    @Test
    void getData()
    {
        Ex2Sheet sheet = new Ex2Sheet(5,5);
        sheet.set(1,2, "5");
        sheet.set(2,3, "10");


        Cell a= sheet.get("A2");
        Cell b= sheet.get("B3");
        Cell c= sheet.get(1,2);
        Cell d= sheet.get(2,3);

        String stra = a.toString();
        String strb = b.toString();
        String strc = c.getData();
        String strd = d.getData();

        assertEquals(stra, "5");
        assertEquals(strb, "10");
        assertEquals(strc, "5");
        assertEquals(strd, "10");
    }
}

