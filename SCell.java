package assignments.ex2;
// Add your documentation below:

public class SCell implements Cell {
    private String line;
    private int type;


    public SCell(String s) {

        setData(s);
    }

    @Override
    public int getOrder() {
        int order = 0;
        if(Ex2Sheet.isFormula(line))
        {
            order = 2;
        }
        else if(Ex2Sheet.isNumber(line))
        {

        }

        return order;

    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        line = s;
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }

}
