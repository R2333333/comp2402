package comp2402a2;

import java.util.AbstractList;

public class test {
    public static void main(String args[]){
        //Class<String> t;

        AbstractList<String> strings = new Treque<>(String.class);


        strings.add(0,"x");


        strings.add(1,"y");


        strings.add(1,"i1");

        //System.out.println(strings.get(1));

        strings.add(1,"n1");

        //System.out.println(strings.get(1));


        strings.add(1,"y2");

        //System.out.println(strings.get(1));

        for(int i=0; i<strings.size(); ++i)
            System.out.print(strings.get(i)+" ");

    }
}
