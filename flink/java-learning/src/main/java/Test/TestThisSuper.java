package Test;

public class TestThisSuper {
    int id;
    String name;
    boolean man;
    double width;
    public TestThisSuper(){
        System.out.println("无参数构造器");
    }

    public TestThisSuper(int number,String name){
        this();
        System.out.println("传递int和String形式参数构造器");
    }

    public TestThisSuper(int number,String name,double _width){
        this(number,name);
        this.width = _width;
        System.out.println("传递int, String, double形式参数构造器");
    }

    public TestThisSuper(int number,String name,char a){
        System.out.println("传递int, String, char形式参数构造器");
    }

    public static void main(String[] args) {
        TestThisSuper t1 = new TestThisSuper();
        TestThisSuper t2 = new TestThisSuper(1000,"Tom");
        TestThisSuper t3 = new TestThisSuper(2000,"long",3.14);
        TestThisSuper t4 = new TestThisSuper(2000,"long",'a');
    }
}